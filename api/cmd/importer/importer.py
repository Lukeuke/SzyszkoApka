import xml.etree.ElementTree as ET
import json
import hashlib
import re
import os
import requests

tree = ET.parse("Biblioteczki-Plenerowe.kml")
root = tree.getroot()
output_path = "biblioteczki.json"
images_dir = "images"

os.makedirs(images_dir, exist_ok=True) 

ns = {"kml": "http://www.opengis.net/kml/2.2"}

entries = []

img_url_regex = re.compile(r'<img\s+[^>]*src="([^"]+)"')

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

    image_keys = []

    clean_description = ""
    if desc is not None and desc.text:
        img_urls = img_url_regex.findall(desc.text)
        
        for index, url in enumerate(img_urls):
            try:
                response = requests.get(url, timeout=10)
                if response.status_code == 200:
                    key = f"{external_key}-{index}.jpg"
                    image_path = os.path.join(images_dir, key)
                    with open(image_path, "wb") as img_file:
                        img_file.write(response.content)
                    print(f"Pobrano: {image_path}")

                    image_keys.append(key)
                else:
                    print(f"Nie udało się pobrać: {url}")
            except Exception as e:
                print(f"Błąd podczas pobierania {url}: {e}")

        clean_description = re.sub(r"<[^>]+>", "", desc.text).strip()

    entries.append({
        "title": name.text if name is not None else "",
        "description": clean_description,
        "lon": float(lon),
        "lat": float(lat),
        "approved": True,
        "external_key": external_key,
        "images": image_keys
    })

with open(output_path, "w", encoding="utf-8") as f:
    json.dump(entries, f, ensure_ascii=False, indent=2)

print(f"Zapisano dane do: {output_path}")