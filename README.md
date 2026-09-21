# `uni-sms` — Android Native Virtual OTP Receiver

[![Android CI/CD Build & Release](https://github.com/Stillalv/uni-sms/actions/workflows/android-build.yml/badge.svg)](https://github.com/Stillalv/uni-sms/actions/workflows/android-build.yml)
![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B%20(API%2026%2B)-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?logo=kotlin&logoColor=white)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2B%20Material%203-4285F4?logo=jetpackcompose&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean%20Architecture-blue)
![License](https://img.shields.io/badge/License-MIT-green)

**`uni-sms`** adalah aplikasi Android Native berkinerja tinggi, ringan, dan modern untuk membeli nomor virtual sementara (*disposable virtual numbers*) dan menerima kode verifikasi OTP (One-Time Password) secara real-time dari ribuan platform (WhatsApp, Telegram, Google, OpenAI, TikTok, Instagram, Twitter/X, dll) menggunakan gateway API **SMSBower** (`smsbower.page`).

---

## ✨ Fitur Utama

- 🔐 **Penyimpanan Kredensial Aman:** API Key disimpan secara lokal di perangkat menggunakan **Android Keystore** & `EncryptedSharedPreferences` (AES-256 GCM). Tidak ada data akun atau API key yang dikirim ke server pihak ketiga manapun.
- 💰 **Header Saldo Real-Time:** Menampilkan saldo aktif SMSBower dengan tombol refresh cepat 1-ketuk.
- 🌐 **Katalog Layanan & Negara Terlengkap:**
  - Pilihan layanan populer (WhatsApp, Telegram, OpenAI/ChatGPT, Google, TikTok, dll) dengan filter pencarian instan.
  - Daftar ratusan negara diurutkan dari stok terbanyak & harga termurah lengkap dengan bendera emoji, kode dial internasional, dan sisa stok (*pcs*).
  - Rekomendasi 10 negara berkinerja terbaik via `getTopCountriesByService`.
- ⚡ **Layar Verifikasi OTP Real-Time (Core Feature):**
  - Tampilan nomor virtual besar dengan tombol **"Salin Nomor"** satu sentuhan.
  - **Cancellation Lock 2 Menit (120 Detik):** Mencegah error `EARLY_CANCEL_DENIED` dengan progress bar countdown 120 detik. Setelah 2 menit, tombol berubah menjadi aktif merah: *"Batalkan & Refund"*.
  - **Session Timer 20 Menit:** Timer mundur total sesi nomor. Jika waktu habis tanpa SMS masuk, saldo dikembalikan secara penuh (*No Code No Pay*).
  - **Animasi Radar Pulsing:** Indikator visual aktivitas polling berlatensi rendah (interval aman 3.5 detik).
  - **Hero OTP Display:** Begitu kode SMS tiba, getar haptic dan notifikasi sistem berbunyi, menampilkan kode OTP berukuran besar lengkap dengan tombol satu sentuhan **"Salin Kode"**.
  - Kontrol aktivasi: Tombol **"Selesai"** (`status=6`) dan **"Minta SMS Kedua"** (`status=3`).
- 📜 **Riwayat Transaksi Offline (Room DB):** Menyimpan seluruh catatan nomor yang pernah dibeli, tanggal, biaya, status, serta kode OTP yang diterima.
- ⚙️ **Pengaturan & Dukungan Lengkap:** Toggle notifikasi & getaran, info IP whitelist (`167.235.198.205`), dan link langsung ke Dokumentasi Resmi serta Bot Support Telegram SMSBower.

---

## 🛠️ Tech Stack & Arsitektur

- **Bahasa:** Kotlin 2.0.0
- **UI Toolkit:** Jetpack Compose + Material 3 + Material Icons Extended
- **Arsitektur:** MVVM (Model-View-ViewModel) + Clean Architecture (Data, Domain, Presentation Layers)
- **State Reaktif:** Kotlin Coroutines & `StateFlow`
- **Networking:** Retrofit 2 + OkHttp 3 + Scalars Converter + Kotlinx Serialization & JSON Parsing
- **Keamanan:** AndroidX Security Crypto (`MasterKey` & `EncryptedSharedPreferences`)
- **Penyimpanan Lokal:** Room Database 2.6.1 + KSP
- **Target SDK:** Android 15 (API 35) | **Min SDK:** Android 8.0 (API 26)

---

## 🚀 CI/CD & Unduh APK

Seluruh proses kompilasi Gradle dan pembuatan APK dieksekusi secara otomatis di Cloud melalui **GitHub Actions Runner (`ubuntu-latest`)**:

1. Buka tab **[Actions](https://github.com/Stillalv/uni-sms/actions)** pada repositori ini.
2. Pilih workflow run terbaru pada branch `main`.
3. Pada bagian **Artifacts**, unduh file **`uni-sms-debug-apk`**.
4. Ekstrak zip dan pasang `app-debug.apk` langsung ke perangkat Android Anda.

---

## 📡 Upstream SMSBower Gateway

- **Gateway Endpoint:** `https://smsbower.page/stubs/handler_api.php`
- **Webhook Whitelist IP:** `167.235.198.205`
- **Dokumentasi Resmi Client:** [https://smsbower.app/api?page=client](https://smsbower.app/api?page=client)
- **Postman API Collection:** [https://documenter.getpostman.com/view/16514200/2sAYdkFTue](https://documenter.getpostman.com/view/16514200/2sAYdkFTue)
- **Telegram Support Bot:** [@smsbower_support_bot](https://t.me/smsbower_support_bot)

---

## 📄 Lisensi
Didistribusikan di bawah lisensi MIT.
