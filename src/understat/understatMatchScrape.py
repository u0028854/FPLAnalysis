## Process file that contains links to understat matches, downloads the file from URL, and outputs to html file locally in format of matchID.html. Input arg is file that contains match IDs.
import requests
import sys
import warnings
import os
from bs4 import BeautifulSoup
from jproperties import Properties

def get_data(url):
    warnings.filterwarnings("ignore")
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')
    return soup

# Will process an input file of Understat mathc URLs. Expected format is that the URL ends with a 5 digit matchID. This ID is used to name the downloaded file on local disk. If that file already exists, the code will skip it and not redownload.
if len (sys.argv) != 2:
    print ('Invalid argument list')
else:
    if sys.argv[1] == 'understatBase':
        configs = Properties()
        with open('../fplProps.properties', 'rb') as config_file:
            configs.load(config_file)
            inputFileName = configs.get('matchListFile').data
            argumentSource = 'props'
    else:
        inputFileName = sys.argv[1]
    
    inputFile = open(inputFileName, 'r')
    for line in inputFile:
        matchId = line.strip()[-5:]
        if matchId.isdigit():
            try:
                directoryPath = configs.get('uStatMatchesDirectory').data
                if(not os.path.exists(directoryPath)):
                    os.makedirs(directoryPath)
            except Exception as e:
                print(e)
            matchFileName = os.path.join(directoryPath, matchId + '.html')
            try:
                open(matchFileName, 'x')
            except FileExistsError:
                print(matchFileName + 'already exists. Skipping download.')
            else:
                outputFile = open(matchFileName, 'w')
                outputFile.write(str(get_data(line.strip())))
                outputFile.close()
    inputFile.close()