package com.unisms.app.ui.util

import com.unisms.app.data.model.ServiceItem

object ServiceCatalog {
    val defaultServices = listOf(
        ServiceItem("wa", "WhatsApp", "Messaging"),
        ServiceItem("tg", "Telegram", "Messaging"),
        ServiceItem("go", "Google / Gmail", "Tech"),
        ServiceItem("oi", "OpenAI / ChatGPT", "AI"),
        ServiceItem("lf", "TikTok", "Social"),
        ServiceItem("ig", "Instagram", "Social"),
        ServiceItem("fb", "Facebook", "Social"),
        ServiceItem("tw", "Twitter / X", "Social"),
        ServiceItem("ds", "Discord", "Gaming"),
        ServiceItem("nf", "Netflix", "Entertainment"),
        ServiceItem("mb", "Microsoft", "Tech"),
        ServiceItem("am", "Amazon", "Shopping"),
        ServiceItem("wb", "WeChat", "Messaging"),
        ServiceItem("vi", "Viber", "Messaging"),
        ServiceItem("ub", "Uber", "Transport"),
        ServiceItem("sn", "Snapchat", "Social"),
        ServiceItem("ot", "Other (Any Service)", "General")
    )

    private val serviceNameMap = defaultServices.associate { it.code to it.name }

    fun getServiceName(code: String): String = serviceNameMap[code] ?: code.uppercase()
}
