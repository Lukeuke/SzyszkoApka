import xml.etree.ElementTree as ET
import json
import hashlib

tree = ET.parse("Biblioteczki-Plenerowe.kml")
root = tree.getroot()
output_path = "biblioteczki.json"

ns = {"kml": "http://www.opengis.net/kml/2.2"}

entries = []

for placemark in root.findall(".//kml:Placemark", ns):
    name = placemark.find("kml:name", ns)
    desc = placemark.find("kml:description", ns)
    coords = placemark.find(".//kml:coordinates", ns)

    if coords is not None:
        coord_parts = coords.text.strip().split(",")
        if len(coord_parts) >= 2:
            lon = coord_parts[0].strip()
            lat = coord_parts[1].strip()
        else:
            lon, lat = "", ""
    else:
        lon, lat = "", ""

    hash_input = f"{name}_{lon}_{lat}"
    external_key = hashlib.sha1(hash_input.encode("utf-8")).hexdigest()

    entries.append({
        "title": name.text if name is not None else "",
        "description": desc.text if desc is not None else "",
        "lon": float(lon),
        "lat": float(lat),
        "approved": True,
        "external_key": external_key
    })

with open(output_path, "w", encoding="utf-8") as f:
    json.dump(entries, f, ensure_ascii=False, indent=2)

print(f"Zapisano dane do: {output_path}")