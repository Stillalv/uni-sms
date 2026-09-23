import os
import json
import urllib.request
import concurrent.futures

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS_DIR = os.path.join(BASE_DIR, "app", "src", "main", "assets")
COUNTRIES_DIR = os.path.join(ASSETS_DIR, "countries")
SERVICES_DIR = os.path.join(ASSETS_DIR, "services")

os.makedirs(COUNTRIES_DIR, exist_ok=True)
os.makedirs(SERVICES_DIR, exist_ok=True)

# 1. Load countries data
scratch_countries = os.path.join(r"C:\Users\uni\.gemini\antigravity\brain\8002d438-3ee7-4006-b6f2-1da1a4c3f2ca\scratch", "smsbower_countries_getlist.json")
if os.path.exists(scratch_countries):
    with open(scratch_countries, "r", encoding="utf-8") as f:
        countries_data = json.load(f)
else:
    req = urllib.request.Request("https://smsbower.app/countries/getList", headers={"User-Agent": "Mozilla/5.0"})
    countries_data = json.loads(urllib.request.urlopen(req).read().decode("utf-8"))

# 2. Load services data
scratch_services = os.path.join(r"C:\Users\uni\.gemini\antigravity\brain\8002d438-3ee7-4006-b6f2-1da1a4c3f2ca\scratch", "services_list.json")
if os.path.exists(scratch_services):
    with open(scratch_services, "r", encoding="utf-8") as f:
        services_raw = json.load(f)
        services_data = services_raw.get("services", [])
else:
    services_data = []

print(f"Downloading {len(countries_data)} countries and {len(services_data)} services...")

def download_country_svg(c):
    iso = (c.get("iso") or "").strip().lower()
    if not iso:
        return
    dest = os.path.join(COUNTRIES_DIR, f"{iso}.svg")
    if os.path.exists(dest) and os.path.getsize(dest) > 100:
        return
    url = f"https://smsbower.app/img/svg/countries/{iso}.svg"
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        content = urllib.request.urlopen(req, timeout=8).read()
        if len(content) > 100:
            with open(dest, "wb") as f:
                f.write(content)
    except Exception:
        pass

def download_service_svg(s):
    code = (s.get("code") or "").strip().lower()
    if not code:
        return
    dest = os.path.join(SERVICES_DIR, f"{code}.svg")
    if os.path.exists(dest) and os.path.getsize(dest) > 50:
        return
    url = f"https://smsbower.app/img/svg/services/{code}.svg"
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        content = urllib.request.urlopen(req, timeout=8).read()
        if len(content) > 50:
            with open(dest, "wb") as f:
                f.write(content)
    except Exception:
        pass

# Also download top essential services explicitly
essential_services = [
    "wa", "tg", "go", "kt", "oi", "lf", "ig", "fb", "tw", "ds", 
    "nf", "mb", "am", "wb", "vi", "ub", "sn", "ot", "ap", "vk", 
    "ok", "ka", "me", "ya", "pm", "hw", "ts", "qq", "bl", "gr",
    "nv", "li", "tt", "yt", "rd", "pt", "sc", "ab", "ac", "ad"
]
for es in essential_services:
    if not any(s.get("code") == es for s in services_data):
        services_data.append({"code": es, "name": es})

# Run parallel downloads
with concurrent.futures.ThreadPoolExecutor(max_workers=16) as executor:
    list(executor.map(download_country_svg, countries_data))

with concurrent.futures.ThreadPoolExecutor(max_workers=16) as executor:
    list(executor.map(download_service_svg, services_data))

country_files = [f for f in os.listdir(COUNTRIES_DIR) if f.endswith(".svg")]
service_files = [f for f in os.listdir(SERVICES_DIR) if f.endswith(".svg")]

print(f"Downloaded {len(country_files)} country flags and {len(service_files)} service logos successfully!")
