import os
import json

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
TARGET_FILE = os.path.join(BASE_DIR, "app", "src", "main", "java", "com", "unisms", "app", "ui", "util", "CountryCatalog.kt")

scratch_countries = os.path.join(r"C:\Users\uni\.gemini\antigravity\brain\8002d438-3ee7-4006-b6f2-1da1a4c3f2ca\scratch", "smsbower_countries_getlist.json")
with open(scratch_countries, "r", encoding="utf-8") as f:
    data = json.load(f)

dial_codes = {
    'ID': '+62', 'KR': '+82', 'US': '+1', 'UV': '+1', 'JP': '+81', 'GB': '+44', 'RU': '+7',
    'KZ': '+7', 'UA': '+380', 'CN': '+86', 'PH': '+63', 'MM': '+95', 'MY': '+60', 'KE': '+254',
    'TZ': '+255', 'VN': '+84', 'KG': '+996', 'IL': '+972', 'HK': '+852', 'PL': '+48', 'MG': '+261',
    'CG': '+242', 'NG': '+234', 'MO': '+853', 'EG': '+20', 'IN': '+91', 'IE': '+353', 'KH': '+855',
    'LA': '+856', 'HT': '+509', 'CI': '+225', 'GM': '+220', 'RS': '+381', 'YE': '+967', 'ZA': '+27',
    'RO': '+40', 'CO': '+57', 'EE': '+372', 'AZ': '+994', 'CA': '+1', 'MA': '+212', 'GH': '+233',
    'AR': '+54', 'UZ': '+998', 'CM': '+237', 'TD': '+235', 'DE': '+49', 'LT': '+370', 'HR': '+385',
    'SE': '+46', 'IQ': '+964', 'NL': '+31', 'LV': '+371', 'AT': '+43', 'BY': '+375', 'TH': '+66',
    'SA': '+966', 'MX': '+52', 'TW': '+886', 'ES': '+34', 'IR': '+98', 'DZ': '+213', 'SI': '+386',
    'BD': '+880', 'SN': '+221', 'TR': '+90', 'CZ': '+420', 'LK': '+94', 'PE': '+51', 'PK': '+92',
    'NZ': '+64', 'GN': '+224', 'ML': '+223', 'VE': '+58', 'ET': '+251', 'MN': '+976', 'BR': '+55',
    'AF': '+93', 'UG': '+256', 'AO': '+244', 'CY': '+357', 'FR': '+33', 'PG': '+675', 'MZ': '+258',
    'IT': '+39', 'PY': '+595', 'HN': '+504', 'TN': '+216', 'NI': '+505', 'TL': '+670', 'BO': '+591',
    'CR': '+506', 'GT': '+502', 'AE': '+971', 'ZW': '+263', 'SG': '+65', 'PT': '+351', 'GE': '+995',
    'JO': '+962', 'AU': '+61', 'BM': '+1441', 'XK': '+383', 'LI': '+423', 'SX': '+1721', 'VU': '+678',
    'GL': '+299', 'SD': '+249', 'MQ': '+596', 'SY': '+963', 'CL': '+56', 'ZM': '+260', 'NA': '+264',
    'NP': '+977', 'PA': '+507', 'EC': '+593', 'DO': '+1809', 'SV': '+503', 'UY': '+598', 'HU': '+36',
    'GR': '+30', 'BE': '+32', 'BG': '+359', 'CH': '+41', 'DK': '+45', 'FI': '+358', 'NO': '+47'
}

def iso_to_flag(iso):
    if not iso or len(iso) != 2:
        return '🌐'
    try:
        return chr(127397 + ord(iso[0].upper())) + chr(127397 + ord(iso[1].upper()))
    except Exception:
        return '🌐'

entries = []
seen_codes = set()
for c in data:
    org_code = c.get('activate_org_code')
    if org_code and str(org_code) not in seen_codes:
        seen_codes.add(str(org_code))
        iso = (c.get('iso') or '').strip().upper()
        title = c.get('title', f'Country #{org_code}').replace('"', '\\"')
        dial = dial_codes.get(iso, '+')
        flag = iso_to_flag(iso)
        entries.append(f'        "{org_code}" to CountryMeta("{title}", "{iso.lower()}", "{flag}", "{dial}")')

# Also ensure "12" is United States (Virtual) and "0" is Russia
if "12" not in seen_codes:
    entries.append('        "12" to CountryMeta("USA (Virtual)", "us", "🇺🇸", "+1")')
if "0" not in seen_codes:
    entries.append('        "0" to CountryMeta("Russia", "ru", "🇷🇺", "+7")')

lines_kt = "\n".join(entries)

content = f"""package com.unisms.app.ui.util

import com.unisms.app.data.model.CountryItem

object CountryCatalog {{
    data class CountryMeta(
        val name: String,
        val iso: String,
        val flag: String,
        val dialCode: String
    )

    private val catalog = mapOf(
{lines_kt}
    )

    fun getCountry(id: String, cost: Double = 0.0, count: Int = 0): CountryItem {{
        val meta = catalog[id]
        return if (meta != null) {{
            CountryItem(
                id = id,
                name = meta.name,
                flagEmoji = meta.flag,
                dialCode = meta.dialCode,
                cost = cost,
                count = count,
                isoCode = meta.iso
            )
        }} else {{
            CountryItem(
                id = id,
                name = "Country #$id",
                flagEmoji = "🌐",
                dialCode = "+",
                cost = cost,
                count = count,
                isoCode = null
            )
        }}
    }}

    fun getCountryName(id: String): String = catalog[id]?.name ?: "Country #$id"
    fun getCountryFlag(id: String): String = catalog[id]?.flag ?: "🌐"
    fun getCountryIso(id: String): String? = catalog[id]?.iso
}}
"""

with open(TARGET_FILE, "w", encoding="utf-8") as f:
    f.write(content)

print(f"Generated CountryCatalog.kt with {len(seen_codes)} countries!")
