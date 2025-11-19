import os
import json
import xml.etree.ElementTree as ET
from xml.dom import minidom

# ==== CONFIGURATION ====
RES_DIR = "./app/src/main/res"
INPUT_JSON = "translations.json"


def ensure_dir(path):
    """Create directory if it doesn't exist."""
    if not os.path.exists(path):
        os.makedirs(path)


def prettify_xml(elem):
    """Return a pretty-printed XML string for Element."""
    rough_string = ET.tostring(elem, encoding="utf-8")
    reparsed = minidom.parseString(rough_string)
    return reparsed.toprettyxml(indent="    ", encoding="utf-8")


def read_existing_strings(file_path):
    """Read an existing strings.xml into a dict."""
    if not os.path.exists(file_path):
        return {}
    tree = ET.parse(file_path)
    root = tree.getroot()
    strings = {}
    for string in root.findall("string"):
        name = string.get("name")
        if name:
            strings[name] = string.text or ""
    return strings


def merge_translations(existing, new_translations):
    """Merge JSON translations into existing ones, without removing old keys."""
    merged = dict(existing)  # start from existing
    for key, value in new_translations.items():
        merged[key] = value  # update or add
    return merged


def write_strings_xml(strings, output_path):
    """Write merged translations back to strings.xml safely."""
    resources = ET.Element("resources")
    for key, value in sorted(strings.items()):
        el = ET.SubElement(resources, "string")
        el.set("name", key)
        if value:
            el.text = value
    xml_bytes = prettify_xml(resources)
    with open(output_path, "wb") as f:
        f.write(xml_bytes)


def locale_to_folder(locale):
    """Convert locale code (en, am, fr, etc.) to folder name."""
    if locale == "en":
        return "values"
    return f"values-{locale}"


def import_translations(input_json, res_dir):
    """Merge JSON translations into XML files."""
    with open(input_json, "r", encoding="utf-8") as f:
        data = json.load(f)

    for locale, new_translations in data.items():
        folder = os.path.join(res_dir, locale_to_folder(locale))
        ensure_dir(folder)

        xml_path = os.path.join(folder, "strings.xml")
        existing = read_existing_strings(xml_path)
        merged = merge_translations(existing, new_translations)
        write_strings_xml(merged, xml_path)
        print(f"✅ Merged and updated: {xml_path}")


if __name__ == "__main__":
    if not os.path.exists(INPUT_JSON):
        print(f"⚠️  File not found: {INPUT_JSON}")
    else:
        import_translations(INPUT_JSON, RES_DIR)
        print("🎉 All translations merged successfully!")
