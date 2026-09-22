package com.unisms.app.data.api

import android.os.Build
import com.unisms.app.data.model.ActivationOrderV2
import com.unisms.app.data.model.CountryItem
import com.unisms.app.data.model.OtpStatus
import com.unisms.app.data.model.ProviderItem
import com.unisms.app.data.model.Resource
import com.unisms.app.data.model.ServiceItem
import com.unisms.app.ui.util.CountryCatalog
import com.unisms.app.ui.util.ServiceCatalog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.ByteArrayInputStream
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object ApiClient {
    private const val BASE_URL = "https://smsbower.page/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "UniSmsApp/1.2.0 (Android)")
                .build()
            chain.proceed(request)
        }
        .also { configureLegacyTlsSupport(it) }
        .build()

    private fun configureLegacyTlsSupport(builder: OkHttpClient.Builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            return
        }
        try {
            val certFactory = CertificateFactory.getInstance("X.509")
            val isrgRootX1Pem = """
                -----BEGIN CERTIFICATE-----
                MIIFazCCA1OgAwIBAgIRAIIQz7DSQONZRGPgu2OCiwAwDQYJKoZIhvcNAQELBQAw
                TzELMAkGA1UEBhMCVVMxKTAnBgNVBAoTIEludGVybmV0IFNlY3VyaXR5IFJlc2Vh
                cmNoIEdyb3VwMRUwEwYDVQQDEwxJU1JHIFJvb3QgWDEwHhcNMTUwNjA0MTEwNDM4
                WhcNMzUwNjA0MTEwNDM4WjBPMQswCQYDVQQGEwJVUzEpMCcGA1UEChMgSW50ZXJu
                ZXQgU2VjdXJpdHkgUmVzZWFyY2ggR3JvdXAxFTATBgNVBAMTDElTUkcgUm9vdCBY
                MTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBAK3oJHP0FDfzm54rVygc
                h77ct984kIxuPOZXoHj3dcKi/vVqbvYATyjb3miGbESTtrFj/RQSa78f0uoxmyF+
                0TM8ukj13Xnfs7j/EvEhmkvBioZxaUpmZmyPfjxwv60pIgbz5MDmgK7iS4+3mX6U
                A5/TR5d8mUgjU+g4rk8Kb4Mu0UlXjIB0ttov0DiNewNwIRt18jA8+o+u3dpjq+sW
                T8KOEUt+zwvo/7V3LvSye0rgTBIlDHCNAymg4VMk7BPZ7hm/ELNKjD+Jo2FR3qyH
                B5T0Y3HsLuJvW5iB4YlcNHlsdu87kGJ55tukmi8mxdAQ4Q7e2RCOFvu396j3x+UC
                B5iPNgiV5+I3lg02dZ77DnKxHZu8A/lJBdiB3QW0KtZB6awBdpUKD9jf1b0SHzUv
                KBds0pjBqAlkd25HN7rOrFleaJ1/ctaJxQZBKT5ZPt0m9STJEadao0xAH0ahmbWn
                OlFuhjuefXKnEgV4We0+UXgVCwOPjdAvBbI+e0ocS3MFEvzG6uBQE3xDk3SzynTn
                jh8BCNAw1FtxNrQHusEwMFxIt4I7mKZ9YIqioymCzLq9gwQbooMDQaHWBfEbwrbw
                qHyGO0aoSCqI3Haadr8faqU9GY/rOPNk3sgrDQoo//fb4hVC1CLQJ13hef4Y53CI
                rU7m2Ys6xt0nUW7/vGT1M0NPAgMBAAGjQjBAMA4GA1UdDwEB/wQEAwIBBjAPBgNV
                HRMBAf8EBTADAQH/MB0GA1UdDgQWBBR5tFnme7bl5AFzgAiIyBpY9umbbjANBgkq
                hkiG9w0BAQsFAAOCAgEAVR9YqbyyqFDQDLHYGmkgJykIrGF1XIpu+ILlaS/V9lZL
                ubhzEFnTIZd+50xx+7LSYK05qAvqFyFWhfFQDlnrzuBZ6brJFe+GnY+EgPbk6ZGQ
                3BebYhtF8GaV0nxvwuo77x/Py9auJ/GpsMiu/X1+mvoiBOv/2X/qkSsisRcOj/KK
                NFtY2PwByVS5uCbMiogziUwthDyC3+6WVwW6LLv3xLfHTjuCvjHIInNzktHCgKQ5
                ORAzI4JMPJ+GslWYHb4phowim57iaztXOoJwTdwJx4nLCgdNbOhdjsnvzqvHu7Ur
                TkXWStAmzOVyyghqpZXjFaH3pO3JLF+l+/+sKAIuvtd7u+Nxe5AW0wdeRlN8NwdC
                jNPElpzVmbUq4JUagEiuTDkHzsxHpFKVK7q4+63SM1N95R1NbdWhscdCb+ZAJzVc
                oyi3B43njTOQ5yOf+1CceWxG1bQVs5ZufpsMljq4Ui0/1lvh+wjChP4kqKOJ2qxq
                4RgqsahDYVvTH9w7jXbyLeiNdd8XM2w9U/t7y0Ff/9yi0GE44Za4rF2LN9d11TPA
                mRGunUHBcnWEvgJBQl9nJEiU0Zsnvgc/ubhPgXRR4Xq37Z0j4r7g1SgEEzwxA57d
                emyPxgcYxn/eR44/KJ4EBs+lVDR3veyJm+kXQ99b21/+jh5Xos1AnX5iItreGCc=
                -----END CERTIFICATE-----
            """.trimIndent()

            val isrgCert = certFactory.generateCertificate(ByteArrayInputStream(isrgRootX1Pem.toByteArray()))
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
                load(null, null)
                setCertificateEntry("isrg_root_x1", isrgCert)
            }

            val defaultTmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(null as KeyStore?)
            }
            val defaultTrustManager = defaultTmf.trustManagers.filterIsInstance<X509TrustManager>().firstOrNull()
            defaultTrustManager?.acceptedIssuers?.forEachIndexed { index, cert ->
                keyStore.setCertificateEntry("system_ca_$index", cert)
            }

            val customTmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }
            val customTrustManager = customTmf.trustManagers.filterIsInstance<X509TrustManager>().first()

            val sslContext = SSLContext.getInstance("TLS").apply {
                init(null, arrayOf(customTrustManager), SecureRandom())
            }

            builder.sslSocketFactory(sslContext.socketFactory, customTrustManager)
        } catch (_: Exception) {
            // Graceful fallback to default SSLSocketFactory
        }
    }

    val apiService: SmsBowerApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(SmsBowerApiService::class.java)
    }
}

object SmsBowerResponseParser {

    fun parseBalance(raw: String): Resource<Double> {
        val trimmed = raw.trim()
        return when {
            trimmed.startsWith("ACCESS_BALANCE:") -> {
                val balanceStr = trimmed.substringAfter("ACCESS_BALANCE:").trim()
                val balance = balanceStr.toDoubleOrNull() ?: 0.0
                Resource.Success(balance)
            }
            trimmed == "BAD_KEY" -> Resource.Error("API Key tidak valid. Silakan periksa di Pengaturan.", "BAD_KEY")
            trimmed == "SERVER_ERROR" -> Resource.Error("Server SMSBower mengalami gangguan. Coba lagi.", "SERVER_ERROR")
            trimmed.contains("\"message\":\"No access\"") -> Resource.Error("Akses ditolak. API Key Anda salah.", "BAD_KEY")
            else -> Resource.Error("Gagal mengambil saldo: $trimmed")
        }
    }

    fun parsePrices(raw: String, serviceCode: String): Resource<List<CountryItem>> {
        val trimmed = raw.trim()
        if (trimmed == "BAD_KEY") return Resource.Error("API Key tidak valid.", "BAD_KEY")
        if (trimmed == "NO_BALANCE") return Resource.Error("Saldo tidak mencukupi.", "NO_BALANCE")

        return try {
            val root = JSONObject(trimmed)
            val countries = mutableListOf<CountryItem>()

            val countryKeys = root.keys()
            while (countryKeys.hasNext()) {
                val countryId = countryKeys.next()
                val countryObj = root.optJSONObject(countryId) ?: continue
                val serviceObj = countryObj.optJSONObject(serviceCode) ?: continue

                val cost = serviceObj.optDouble("cost", 0.0)
                val count = serviceObj.optInt("count", 0)

                if (cost > 0.0 || count > 0) {
                    countries.add(CountryCatalog.getCountry(countryId, cost, count))
                }
            }

            // Sort: available count > 0 first, then by cheapest cost
            countries.sortWith(
                compareByDescending<CountryItem> { it.count > 0 }
                    .thenBy { it.cost }
                    .thenBy { it.name }
            )

            Resource.Success(countries)
        } catch (e: Exception) {
            Resource.Error("Format katalog harga tidak dikenali: ${e.localizedMessage}")
        }
    }

    fun parseTopCountries(raw: String): List<String> {
        val trimmed = raw.trim()
        val list = mutableListOf<String>()
        try {
            if (trimmed.startsWith("[")) {
                val array = JSONArray(trimmed)
                for (i in 0 until array.length()) {
                    list.add(array.getString(i))
                }
            } else if (trimmed.startsWith("{")) {
                val obj = JSONObject(trimmed)
                val keys = obj.keys()
                while (keys.hasNext()) {
                    list.add(keys.next())
                }
            }
        } catch (_: Exception) {}
        return list
    }

    fun parseProvidersV3(raw: String, countryId: String, serviceCode: String): List<ProviderItem> {
        val trimmed = raw.trim()
        if (!trimmed.startsWith("{")) return emptyList()
        val list = mutableListOf<ProviderItem>()
        try {
            val root = JSONObject(trimmed)
            val countryObj = root.optJSONObject(countryId) ?: return emptyList()
            val serviceObj = countryObj.optJSONObject(serviceCode) ?: return emptyList()

            val providerKeys = serviceObj.keys()
            while (providerKeys.hasNext()) {
                val pKey = providerKeys.next()
                val pObj = serviceObj.optJSONObject(pKey) ?: continue
                val providerId = pObj.optString("provider_id", pKey)
                val count = pObj.optInt("count", 0)
                val price = pObj.optDouble("price", 0.0)

                if (count > 0 || price > 0.0) {
                    list.add(
                        ProviderItem(
                            id = providerId,
                            name = "Operator #$providerId",
                            count = count,
                            price = price
                        )
                    )
                }
            }
            list.sortBy { it.price }
        } catch (_: Exception) {}
        return list
    }

    fun parseBuyNumber(raw: String): Resource<ActivationOrderV2> {
        val trimmed = raw.trim()
        return when {
            trimmed == "NO_NUMBERS" -> Resource.Error("Stok nomor untuk negara ini sedang habis. Silakan pilih negara lain.", "NO_NUMBERS")
            trimmed == "NO_BALANCE" -> Resource.Error("Saldo Anda tidak mencukupi untuk membeli nomor ini.", "NO_BALANCE")
            trimmed == "BAD_KEY" -> Resource.Error("API Key tidak valid.", "BAD_KEY")
            trimmed == "BAD_SERVICE" -> Resource.Error("Layanan tidak didukung atau sedang gangguan.", "BAD_SERVICE")
            trimmed == "BAD_ACTION" -> Resource.Error("Aksi pemesanan nomor tidak valid.", "BAD_ACTION")
            trimmed.startsWith("{") -> {
                try {
                    val obj = JSONObject(trimmed)
                    if (obj.has("activationId") && obj.has("phoneNumber")) {
                        val order = ActivationOrderV2(
                            activationId = obj.getLong("activationId"),
                            phoneNumber = obj.getString("phoneNumber"),
                            activationCost = obj.optString("activationCost", "0.00"),
                            countryCode = obj.optString("countryCode", null),
                            canGetAnotherSms = obj.optString("canGetAnotherSms", "0"),
                            activationTime = obj.optString("activationTime", null),
                            activationOperator = if (obj.isNull("activationOperator")) null else obj.optString("activationOperator")
                        )
                        Resource.Success(order)
                    } else if (obj.has("error")) {
                        Resource.Error(obj.getString("error"))
                    } else {
                        Resource.Error("Format pemesanan tidak dikenali.")
                    }
                } catch (e: Exception) {
                    Resource.Error("Gagal membaca hasil aktivasi: ${e.localizedMessage}")
                }
            }
            else -> Resource.Error("Gagal membeli nomor: $trimmed")
        }
    }

    fun parseOtpStatus(raw: String): Resource<OtpStatus> {
        val trimmed = raw.trim()
        return when {
            trimmed == "STATUS_WAIT_CODE" -> Resource.Success(OtpStatus.WaitingCode)
            trimmed.startsWith("STATUS_OK:") -> {
                val code = trimmed.substringAfter("STATUS_OK:").trim()
                Resource.Success(OtpStatus.CodeReceived(code))
            }
            trimmed.startsWith("STATUS_WAIT_RETRY:") -> {
                val code = trimmed.substringAfter("STATUS_WAIT_RETRY:").trim()
                Resource.Success(OtpStatus.RetryWaiting(code))
            }
            trimmed == "STATUS_CANCEL" -> Resource.Success(OtpStatus.Cancelled)
            trimmed == "NO_ACTIVATION" -> Resource.Error("Aktivasi tidak ditemukan atau telah berakhir.", "NO_ACTIVATION")
            trimmed == "SERVER_ERROR" -> Resource.Error("Server sedang sibuk.", "SERVER_ERROR")
            trimmed == "BAD_KEY" -> Resource.Error("API Key tidak valid.", "BAD_KEY")
            else -> Resource.Error("Status tidak dikenali: $trimmed")
        }
    }

    fun parseSetStatusResponse(raw: String, requestedStatus: Int): Resource<String> {
        val trimmed = raw.trim()
        return when (trimmed) {
            "ACCESS_READY" -> Resource.Success("Siap menerima SMS.")
            "ACCESS_RETRY_GET" -> Resource.Success("Menunggu SMS kedua.")
            "ACCESS_ACTIVATION" -> Resource.Success("Aktivasi berhasil diselesaikan.")
            "ACCESS_CANCEL" -> Resource.Success("Nomor berhasil dibatalkan dan saldo dikembalikan.")
            "EARLY_CANCEL_DENIED" -> Resource.Error("Nomor belum dapat dibatalkan. Menunggu kunci 2 menit.", "EARLY_CANCEL_DENIED")
            "BAD_STATUS" -> Resource.Error("Kode status tidak valid.", "BAD_STATUS")
            "NO_ACTIVATION" -> Resource.Error("Aktivasi tidak ditemukan.", "NO_ACTIVATION")
            else -> Resource.Error("Respon: $trimmed")
        }
    }

    fun parseServices(raw: String): List<ServiceItem> {
        val trimmed = raw.trim()
        if (trimmed.startsWith("{")) {
            try {
                val root = JSONObject(trimmed)
                val servicesArray = root.optJSONArray("services")
                if (servicesArray != null) {
                    val list = mutableListOf<ServiceItem>()
                    for (i in 0 until servicesArray.length()) {
                        val item = servicesArray.getJSONObject(i)
                        val code = item.optString("code")
                        val name = item.optString("name", code)
                        if (code.isNotEmpty()) {
                            list.add(ServiceItem(code, name))
                        }
                    }
                    if (list.isNotEmpty()) return list
                }
            } catch (_: Exception) {}
        }
        return ServiceCatalog.defaultServices
    }
}
