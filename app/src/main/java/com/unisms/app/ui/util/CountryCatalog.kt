package com.unisms.app.ui.util

import com.unisms.app.data.model.CountryItem

object CountryCatalog {
    data class CountryMeta(
        val name: String,
        val flag: String,
        val dialCode: String
    )

    private val catalog = mapOf(
        "0" to CountryMeta("Russia", "🇷🇺", "+7"),
        "1" to CountryMeta("Ukraine", "🇺🇦", "+380"),
        "2" to CountryMeta("Kazakhstan", "🇰🇿", "+7"),
        "3" to CountryMeta("China", "🇨🇳", "+86"),
        "4" to CountryMeta("Philippines", "🇵🇭", "+63"),
        "5" to CountryMeta("Myanmar", "🇲🇲", "+95"),
        "6" to CountryMeta("Indonesia", "🇮🇩", "+62"),
        "7" to CountryMeta("Malaysia", "🇲🇾", "+60"),
        "8" to CountryMeta("Kenya", "🇰🇪", "+254"),
        "9" to CountryMeta("Tanzania", "🇹🇿", "+255"),
        "10" to CountryMeta("Vietnam", "🇻🇳", "+84"),
        "11" to CountryMeta("Kyrgyzstan", "🇰🇬", "+996"),
        "12" to CountryMeta("USA", "🇺🇸", "+1"),
        "13" to CountryMeta("Israel", "🇮🇱", "+972"),
        "14" to CountryMeta("Hong Kong", "🇭🇰", "+852"),
        "15" to CountryMeta("Poland", "🇵🇱", "+48"),
        "16" to CountryMeta("United Kingdom", "🇬🇧", "+44"),
        "17" to CountryMeta("Madagascar", "🇲🇬", "+261"),
        "18" to CountryMeta("Congo", "🇨🇬", "+242"),
        "19" to CountryMeta("Nigeria", "🇳🇬", "+234"),
        "20" to CountryMeta("Macau", "🇲🇴", "+853"),
        "21" to CountryMeta("Egypt", "🇪🇬", "+20"),
        "22" to CountryMeta("India", "🇮🇳", "+91"),
        "23" to CountryMeta("Ireland", "🇮🇪", "+353"),
        "24" to CountryMeta("Cambodia", "🇰🇭", "+855"),
        "25" to CountryMeta("Laos", "🇱🇦", "+856"),
        "26" to CountryMeta("Haiti", "🇭🇹", "+509"),
        "27" to CountryMeta("Ivory Coast", "🇨🇮", "+225"),
        "28" to CountryMeta("Gambia", "🇬🇲", "+220"),
        "29" to CountryMeta("Serbia", "🇷🇸", "+381"),
        "30" to CountryMeta("Yemen", "🇾🇪", "+967"),
        "31" to CountryMeta("South Africa", "🇿🇦", "+27"),
        "32" to CountryMeta("Romania", "🇷🇴", "+40"),
        "33" to CountryMeta("Colombia", "🇨🇴", "+57"),
        "34" to CountryMeta("Estonia", "🇪🇪", "+372"),
        "35" to CountryMeta("Azerbaijan", "🇦🇿", "+994"),
        "36" to CountryMeta("Canada", "🇨🇦", "+1"),
        "37" to CountryMeta("Morocco", "🇲🇦", "+212"),
        "38" to CountryMeta("Ghana", "🇬🇭", "+233"),
        "39" to CountryMeta("Argentina", "🇦🇷", "+54"),
        "40" to CountryMeta("Uzbekistan", "🇺🇿", "+998"),
        "41" to CountryMeta("Cameroon", "🇨🇲", "+237"),
        "42" to CountryMeta("Chad", "🇹🇩", "+235"),
        "43" to CountryMeta("Germany", "🇩🇪", "+49"),
        "44" to CountryMeta("Lithuania", "🇱🇹", "+370"),
        "45" to CountryMeta("Croatia", "🇭🇷", "+385"),
        "46" to CountryMeta("Sweden", "🇸🇪", "+46"),
        "47" to CountryMeta("Iraq", "🇮🇶", "+964"),
        "48" to CountryMeta("Netherlands", "🇳🇱", "+31"),
        "49" to CountryMeta("Latvia", "🇱🇻", "+371"),
        "50" to CountryMeta("Austria", "🇦🇹", "+43"),
        "51" to CountryMeta("Belarus", "🇧🇾", "+375"),
        "52" to CountryMeta("Thailand", "🇹🇭", "+66"),
        "53" to CountryMeta("Saudi Arabia", "🇸🇦", "+966"),
        "54" to CountryMeta("Mexico", "🇲🇽", "+52"),
        "55" to CountryMeta("Taiwan", "🇹🇼", "+886"),
        "56" to CountryMeta("Spain", "🇪🇸", "+34"),
        "57" to CountryMeta("Iran", "🇮🇷", "+98"),
        "58" to CountryMeta("Algeria", "🇩🇿", "+213"),
        "59" to CountryMeta("Slovenia", "🇸🇮", "+386"),
        "60" to CountryMeta("Bangladesh", "🇧🇩", "+880"),
        "61" to CountryMeta("Senegal", "🇸🇳", "+221"),
        "62" to CountryMeta("Turkey", "🇹🇷", "+90"),
        "63" to CountryMeta("Czech Republic", "🇨🇿", "+420"),
        "64" to CountryMeta("Sri Lanka", "🇱🇰", "+94"),
        "65" to CountryMeta("Peru", "🇵🇪", "+51"),
        "66" to CountryMeta("Pakistan", "🇵🇰", "+92"),
        "67" to CountryMeta("New Zealand", "🇳🇿", "+64"),
        "68" to CountryMeta("Guinea", "🇬🇳", "+224"),
        "69" to CountryMeta("Mali", "🇲🇱", "+223"),
        "70" to CountryMeta("Venezuela", "🇻🇪", "+58"),
        "71" to CountryMeta("Ethiopia", "🇪🇹", "+251"),
        "72" to CountryMeta("Mongolia", "🇲🇳", "+976"),
        "73" to CountryMeta("Brazil", "🇧🇷", "+55"),
        "74" to CountryMeta("Afghanistan", "🇦🇫", "+93"),
        "75" to CountryMeta("Uganda", "🇺🇬", "+256"),
        "76" to CountryMeta("Angola", "🇦🇴", "+244"),
        "77" to CountryMeta("Cyprus", "🇨🇾", "+357"),
        "78" to CountryMeta("France", "🇫🇷", "+33"),
        "79" to CountryMeta("Papua New Guinea", "🇵🇬", "+675"),
        "80" to CountryMeta("Mozambique", "🇲🇿", "+258"),
        "86" to CountryMeta("Italy", "🇮🇹", "+39"),
        "87" to CountryMeta("Paraguay", "🇵🇾", "+595"),
        "88" to CountryMeta("Honduras", "🇭🇳", "+504"),
        "90" to CountryMeta("Tunisia", "🇹🇳", "+216"),
        "91" to CountryMeta("Nicaragua", "🇳🇮", "+505"),
        "92" to CountryMeta("Timor-Leste", "🇹🇱", "+670"),
        "93" to CountryMeta("Bolivia", "🇧🇴", "+591"),
        "94" to CountryMeta("Costa Rica", "🇨🇷", "+506"),
        "95" to CountryMeta("Guatemala", "🇬🇹", "+502"),
        "96" to CountryMeta("UAE", "🇦🇪", "+971"),
        "97" to CountryMeta("Zimbabwe", "🇿🇼", "+263"),
        "101" to CountryMeta("Singapore", "🇸🇬", "+65"),
        "117" to CountryMeta("Portugal", "🇵🇹", "+351"),
        "120" to CountryMeta("Japan", "🇯🇵", "+81"),
        "128" to CountryMeta("Georgia", "🇬🇪", "+995"),
        "131" to CountryMeta("Jordan", "🇯🇴", "+962"),
        "138" to CountryMeta("Australia", "🇦🇺", "+61"),
        "148" to CountryMeta("South Korea", "🇰🇷", "+82"),
        "187" to CountryMeta("USA (Virtual)", "🇺🇸", "+1")
    )

    fun getCountry(id: String, cost: Double = 0.0, count: Int = 0): CountryItem {
        val meta = catalog[id]
        return if (meta != null) {
            CountryItem(
                id = id,
                name = meta.name,
                flagEmoji = meta.flag,
                dialCode = meta.dialCode,
                cost = cost,
                count = count
            )
        } else {
            CountryItem(
                id = id,
                name = "Country #$id",
                flagEmoji = "🌐",
                dialCode = "+",
                cost = cost,
                count = count
            )
        }
    }

    fun getCountryName(id: String): String = catalog[id]?.name ?: "Country #$id"
    fun getCountryFlag(id: String): String = catalog[id]?.flag ?: "🌐"
}
