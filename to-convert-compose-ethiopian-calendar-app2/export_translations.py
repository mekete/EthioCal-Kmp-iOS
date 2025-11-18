import os
import xml.etree.ElementTree as ET
import csv
import json

# ==== CONFIGURATION ====
# Path to your res folder
RES_DIR = "./app/src/main/res"

# Output file names
OUTPUT_CSV = "translations.csv"
OUTPUT_JSON = "translations.json"


def parse_strings_xml(file_path):
    """Parse a single strings.xml file and return {name: text} map."""
    tree = ET.parse(file_path)
    root = tree.getroot()
    strings = {}
    for string in root.findall('string'):
        name = string.get('name')
        if name:  # skip malformed entries
            strings[name] = string.text or ""
    return strings


def get_locale(folder_name):
    """Extract locale from folder name (values, values-fr, etc.)."""
    if folder_name == "values":
        return "en"
    if "-" in folder_name:
        return folder_name.split("-", 1)[1]
    return folder_name


def collect_translations(res_dir):
    """Collect translations from all values-xx folders."""
    translations = {}
    for folder in os.listdir(res_dir):
        if folder.startswith("values"):
            xml_file = os.path.join(res_dir, folder, "strings.xml")
            if os.path.exists(xml_file):
                locale = get_locale(folder)
                translations[locale] = parse_strings_xml(xml_file)
    return translations


def export_to_csv(translations, output_path):
    """Export translations to CSV file."""
    all_keys = sorted({k for t in translations.values() for k in t.keys()})
    locales = list(translations.keys())

    with open(output_path, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["key"] + locales)
        for key in all_keys:
            row = [key] + [translations[loc].get(key, "") for loc in locales]
            writer.writerow(row)


def export_to_json(translations, output_path):
    """Export translations to JSON file."""
    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(translations, f, ensure_ascii=False, indent=2)


if __name__ == "__main__":
    translations = collect_translations(RES_DIR)
    if not translations:
        print("⚠️  No translations found. Check your RES_DIR path.")
    else:
        export_to_csv(translations, OUTPUT_CSV)
        export_to_json(translations, OUTPUT_JSON)
        print(f"✅ Exported to {OUTPUT_CSV} and {OUTPUT_JSON}")
