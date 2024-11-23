import argparse
import os
import requests
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
# run as "python translator.py", args: api_key, input_language, output_languages, input_folder

API_KEY = ""
OUTPUT_LANGUAGES = ""
INPUT_FILE = ""
VALUES_FOLDER = ""
INPUT_LANGUAGE = "EN"

parser = argparse.ArgumentParser(description='Translate script')
parser.add_argument('--api_key', type=str, help='API Key for translation')
parser.add_argument('--input_language', type=str, help='Language of the input file')
parser.add_argument('--output_languages', type=str, help='ISO codes for output languages')
parser.add_argument('--input_folder', type=str, help='Folder path of the file you want translate')
args = parser.parse_args()

if args.api_key:
    API_KEY = args.api_key

if args.input_language:
    INPUT_LANGUAGE = args.input_language

if args.output_languages:
    OUTPUT_LANGUAGES = args.output_languages

if args.input_folder:
    VALUES_FOLDER = args.input_folder
    INPUT_FILE = VALUES_FOLDER + '/strings.xml'


def create_directory_if_not_exists(directory_name):
    if not os.path.exists(directory_name):
        os.makedirs(directory_name)


def create_directories(dir_language):
    file_directory = VALUES_FOLDER + "-" + dir_language.lower()

    create_directory_if_not_exists(file_directory)
    return file_directory

languages_from_translate = INPUT_LANGUAGE
languages_to_translate = OUTPUT_LANGUAGES.split(",")

for language_name in languages_to_translate:
    language_to_translate = language_name.strip()

    translated_file_directory = create_directories(language_to_translate)
    print(" -> " + language_to_translate + " =========================")

    tree = ElementTree.parse(INPUT_FILE)
    root = tree.getroot()
    for i in range(len(root)):
        if 'translatable' not in root[i].attrib:
            value = root[i].text
            context = root[i].attrib.get('comment')
            if value is not None:
                params = {
                    'auth_key' : API_KEY,
                    'text' : value,
                    'formality': 'less',
                    'source_lang' : languages_from_translate,
                    "target_lang": language_to_translate
                }
                if context is not None:
                    params['context'] = context
                request = requests.post("https://api.deepl.com/v2/translate", data=params)
                result = request.json()

                translated_text = result["translations"][0]["text"].strip()
                root[i].text = translated_text.replace("'", "\\'")
                print(value + "-->" + root[i].text)

    translated_file = translated_file_directory + "/strings.xml"

    tree.write(translated_file, encoding='utf-8')
