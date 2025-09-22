from io import BytesIO
import xml.etree.ElementTree as ET
import json
import hashlib
import re
import os
import requests
import boto3
from botocore.client import Config
import uuid

R2_ACCESS_KEY_ID = ""
R2_SECRET_ACCESS_KEY = ""
R2_BUCKET_NAME = "images"
R2_ENDPOINT_URL = ""
R2_REGION = "auto"

s3 = boto3.client(
    's3',
    region_name=R2_REGION,
    endpoint_url=R2_ENDPOINT_URL,
    aws_access_key_id=R2_ACCESS_KEY_ID,
    aws_secret_access_key=R2_SECRET_ACCESS_KEY,
    config=Config(signature_version="s3v4")
)

KML_NETWORK_URL = "https://www.google.com/maps/d/kml?forcekml=1&mid=1pwX0dt196mEPkrDBsrooXOa0-Pvqols"

response = requests.get(KML_NETWORK_URL)
response.raise_for_status()

tree = ET.parse(BytesIO(response.content))
root = tree.getroot()
output_path = "biblioteczki.json"

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

    hash_input = f"{name.text.strip()}_{lon}_{lat}"
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
                    image_keys.append(key)

                    try:
                        s3.head_object(Bucket=R2_BUCKET_NAME, Key=key)
                        print(f"🔁 Obrazek już istnieje w R2: {key} – pomijam upload.")
                    except s3.exceptions.ClientError as e:
                        if e.response['Error']['Code'] == '404':
                            s3.put_object(
                                Bucket=R2_BUCKET_NAME,
                                Key=key,
                                Body=response.content,
                                ContentType="image/jpeg"
                            )
                            print(f"✔️ Obrazek wysłany do R2: {key}")
                        else:
                            print(f"❌ Błąd sprawdzania istnienia {key}: {e}")
                else:
                    print(f"❌ Nie udało się pobrać: {url}")
            except Exception as e:
                print(f"⚠️ Błąd podczas pobierania {url}: {e}")

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

print(f"✅ Zapisano dane do: {output_path}")