import os
import urllib.request
import xml.etree.ElementTree as ET

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DRAWABLE_DIR = os.path.join(BASE_DIR, "app", "src", "main", "res", "drawable")
os.makedirs(DRAWABLE_DIR, exist_ok=True)

icons = [
    ("ic_lucide_wallet", "wallet"),
    ("ic_lucide_refresh_cw", "rotate-cw"),
    ("ic_lucide_settings", "settings"),
    ("ic_lucide_sliders", "sliders-horizontal"),
    ("ic_lucide_shopping_cart", "shopping-cart"),
    ("ic_lucide_copy", "copy"),
    ("ic_lucide_search", "search"),
    ("ic_lucide_x", "x"),
    ("ic_lucide_check_circle", "check-circle-2"),
    ("ic_lucide_check", "check"),
    ("ic_lucide_arrow_left", "arrow-left"),
    ("ic_lucide_key_round", "key-round"),
    ("ic_lucide_bell", "bell"),
    ("ic_lucide_vibrate", "vibrate"),
    ("ic_lucide_external_link", "external-link"),
    ("ic_lucide_history", "history"),
    ("ic_lucide_trash_2", "trash-2"),
    ("ic_lucide_info", "info"),
    ("ic_lucide_message_square", "message-square"),
    ("ic_lucide_clipboard", "clipboard"),
    ("ic_lucide_chevron_right", "chevron-right"),
    ("ic_lucide_chevron_left", "chevron-left"),
    ("ic_lucide_send", "send"),
    ("ic_lucide_book_open", "book-open"),
    ("ic_lucide_credit_card", "credit-card"),
    ("ic_lucide_compass", "compass"),
    ("ic_lucide_radio", "radio"),
    ("ic_lucide_shield_check", "shield-check")
]

def convert_svg_to_vector_drawable(svg_str):
    root = ET.fromstring(svg_str)
    paths = []
    
    for elem in root.iter():
        tag = elem.tag.split("}")[-1]
        if tag == "path":
            d = elem.attrib.get("d", "")
            if d:
                paths.append(d)
        elif tag == "line":
            x1 = elem.attrib.get("x1", "0")
            y1 = elem.attrib.get("y1", "0")
            x2 = elem.attrib.get("x2", "0")
            y2 = elem.attrib.get("y2", "0")
            paths.append(f"M{x1},{y1}L{x2},{y2}")
        elif tag == "circle":
            cx = float(elem.attrib.get("cx", "0"))
            cy = float(elem.attrib.get("cy", "0"))
            r = float(elem.attrib.get("r", "0"))
            paths.append(f"M{cx-r},{cy}A{r},{r} 0 1,0 {cx+r},{cy}A{r},{r} 0 1,0 {cx-r},{cy}")
        elif tag == "rect":
            x = float(elem.attrib.get("x", "0"))
            y = float(elem.attrib.get("y", "0"))
            w = float(elem.attrib.get("width", "0"))
            h = float(elem.attrib.get("height", "0"))
            rx = float(elem.attrib.get("rx", "0"))
            if rx > 0:
                paths.append(f"M{x+rx},{y}h{w-2*rx}a{rx},{rx} 0 0 1 {rx},{rx}v{h-2*rx}a{rx},{rx} 0 0 1 -{rx},{rx}h-{w-2*rx}a{rx},{rx} 0 0 1 -{rx},-{rx}v-{h-2*rx}a{rx},{rx} 0 0 1 {rx},-{rx}z")
            else:
                paths.append(f"M{x},{y}h{w}v{h}h-{w}z")
        elif tag == "polyline":
            pts = elem.attrib.get("points", "").strip().split()
            if pts:
                d = "M" + pts[0]
                for p in pts[1:]:
                    d += "L" + p
                paths.append(d)
    
    xml = """<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="24dp"
    android:height="24dp"
    android:viewportWidth="24"
    android:viewportHeight="24">
"""
    for p in paths:
        xml += f"""    <path
        android:strokeColor="#FFFFFFFF"
        android:strokeWidth="2"
        android:strokeLineCap="round"
        android:strokeLineJoin="round"
        android:pathData="{p}" />
"""
    xml += "</vector>\n"
    return xml

for drw_name, svg_name in icons:
    url = f"https://unpkg.com/lucide-static@latest/icons/{svg_name}.svg"
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        svg_str = urllib.request.urlopen(req, timeout=5).read().decode("utf-8")
        vec_xml = convert_svg_to_vector_drawable(svg_str)
        dest_file = os.path.join(DRAWABLE_DIR, f"{drw_name}.xml")
        with open(dest_file, "w", encoding="utf-8") as f:
            f.write(vec_xml)
        print(f"Generated {drw_name}.xml")
    except Exception as e:
        print(f"Error {drw_name}: {e}")

print("All Lucide vector drawables generated successfully!")
