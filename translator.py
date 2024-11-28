import argparse
import os
import requests
import json
import xml.etree.ElementTree as ElementTree

# MIT License

# Copyright (c) 2018 Cuneyt AYYILDIZ

# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:

# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.

# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

# PERSONAL ADAPTATION BY jmanrique
# run as "python translator.py", args: api_key, input_language, output_languages, input_folder, platform

API_KEY = ""
OUTPUT_LANGUAGES = ""
INPUT_FILE = ""
VALUES_FOLDER = ""
INPUT_LANGUAGE = "EN"
PLATFORM = ""

parser = argparse.ArgumentParser(description='Translate script')
parser.add_argument('--api_key', type=str, help='API Key for translation')
parser.add_argument('--input_language', type=str, help='Language of the input file')
parser.add_argument('--output_languages', type=str, help='ISO codes for output languages')
parser.add_argument('--input_folder', type=str, help='Folder path of the file you want translate')
parser.add_argument('--platform', type=str, help='Target phone operative system, might be android, ios or common')
args = parser.parse_args()

if args.api_key:
    API_KEY = args.api_key

if args.input_language:
    INPUT_LANGUAGE = args.input_language

if args.output_languages:
    OUTPUT_LANGUAGES = args.output_languages

PLATFORM = args.platform

if args.input_folder:
    VALUES_FOLDER = args.input_folder
    if PLATFORM == 'ios':
        INPUT_FILE = VALUES_FOLDER + '/Localizable.xcstrings'
    else:
        INPUT_FILE = VALUES_FOLDER + '/strings.xml'


def create_directory_if_not_exists(directory_name):
    if not os.path.exists(directory_name):
        os.makedirs(directory_name)


def create_ios_directory(language_code):
    directory = os.path.join(VALUES_FOLDER, f"{language_code}.lproj")
    create_directory_if_not_exists(directory)
    return directory


def create_android_directories(dir_language):
    file_directory = VALUES_FOLDER + "-" + dir_language.lower()

    create_directory_if_not_exists(file_directory)
    return file_directory


languages_from_translate = INPUT_LANGUAGE
languages_to_translate = OUTPUT_LANGUAGES.split(",")
languages_supporting_formality = ["DE", "FR", "IT", "ES", "NL", "PL", "PT-BR", "PT-PT", "JA", "RU"]


def translate_ios_infoplist_strings():
    with open(INPUT_FILE, 'r', encoding='utf-8') as file:
        strings = file.readlines()

    for language_name in languages_to_translate:
        language_code = language_name.strip().lower()
        translated_directory = create_ios_directory(language_code)
        translated_file_path = os.path.join(translated_directory, 'InfoPlist.strings')

        with open(translated_file_path, 'w', encoding='utf-8') as translated_file:
            for line in strings:
                if '=' in line:
                    key, value = line.split('=', 1)
                    key = key.strip()
                    value = value.strip().strip('"').strip(';')

                    params = {
                        'auth_key': API_KEY,
                        'text': value,
                        'source_lang': INPUT_LANGUAGE,
                        'target_lang': language_code.upper()
                    }

                    if language_code.upper() in languages_supporting_formality:
                        params["formality"] = "less"

                    response = requests.post("https://api.deepl.com/v2/translate", data=params)
                    result = response.json()

                    translated_text = result.get("translations", [{}])[0].get("text", "").strip()
                    translated_line = f'{key} = "{translated_text}";'
                    translated_file.write(translated_line)
                    print(f"{value} --> {translated_text}")
                else:
                    translated_file.write(line)


def translate_ios_strings():
    # Load the existing JSON from Localizable.xcstrings
    with open(INPUT_FILE, 'r', encoding='utf-8') as f:
        localization_data = json.load(f)

    # Iterate through each target language and add translations
    for language_name in languages_to_translate:
        language_to_translate = language_name.strip().lower()

        print(f" -> Translating to {language_to_translate} =========================")

        # Iterate through all strings in the JSON structure
        for key, value in localization_data['strings'].items():
            if 'en' in value['localizations']:
                # Get the English value to translate
                english_value = value['localizations']['en']['stringUnit']['value']
                context = value.get('comment', None)

                # Set up parameters for DeepL API
                params = {
                    'auth_key': API_KEY,
                    'text': english_value,
                    'source_lang': languages_from_translate,
                    "target_lang": language_to_translate.upper()
                }

                if context is not None:
                    params['context'] = context  # Optional: Provide context if needed

                if language_to_translate.upper() in languages_supporting_formality:
                    params["formality"] = "less"  # Optional: Set informal tone for supported languages

                # Send translation request to DeepL API
                try:
                    request = requests.post("https://api.deepl.com/v2/translate", data=params)
                    request.raise_for_status()  # Raise an error for bad responses
                    result = request.json()

                    # Extract the translated text
                    translated_text = result["translations"][0]["text"].strip()

                    # Add the translation to the localization data
                    if 'localizations' not in value:
                        value['localizations'] = {}
                    value['localizations'][language_to_translate] = {
                        "stringUnit": {
                            "state": "translated",
                            "value": translated_text
                        }
                    }

                    # Debug print
                    print(f'{english_value} --> {translated_text}')

                except requests.exceptions.RequestException as e:
                    print(f"Error during translation for key '{key}': {e}")

    # Write the updated JSON to Localizable.xcstrings
    translated_file_path = os.path.join(VALUES_FOLDER, 'Localizable.xcstrings')
    with open(translated_file_path, 'w', encoding='utf-8') as f:
        json.dump(localization_data, f, ensure_ascii=False, indent=2)

    print(f"Translations written to: {translated_file_path}")


def translate_android_or_common_strings():
    for language_name in languages_to_translate:
        language_to_translate = language_name.strip()
        translated_file_directory = create_android_directories(language_to_translate)
        print(" -> " + language_to_translate + " =========================")
        tree = ElementTree.parse(INPUT_FILE)
        root = tree.getroot()

        for i in range(len(root)):
            if 'translatable' not in root[i].attrib:
                value = root[i].text
                context = root[i].attrib.get('comment')
                if value is not None:
                    params = {
                        'auth_key': API_KEY,
                        'text': value,
                        'source_lang': languages_from_translate,
                        "target_lang": language_to_translate
                    }
                    if context is not None:
                        params['context'] = context
                    if language_to_translate in languages_supporting_formality:
                        params["formality"] = "less"
                    request = requests.post("https://api.deepl.com/v2/translate", data=params)
                    result = request.json()

                    translated_text = result["translations"][0]["text"].strip()
                    root[i].text = translated_text.replace("'", "\\'")
                    print(value + "-->" + root[i].text)

        translated_file = translated_file_directory + "/strings.xml"
        tree.write(translated_file, encoding='utf-8')


if PLATFORM != 'ios':
    translate_android_or_common_strings()
else:
    translate_ios_infoplist_strings()
    translate_ios_strings()
