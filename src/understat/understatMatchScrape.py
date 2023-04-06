import requests
import sys
import warnings
from bs4 import BeautifulSoup

def get_data(url):
    warnings.filterwarnings("ignore")
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')
    return soup

if len (sys.argv) != 2:
    print ('Invalid argument list')
else:
    inputFile = open(sys.argv[1], 'r')
    for line in inputFile:
        matchId = line.strip()[-5:]
        if matchId.isdigit():
            outputFile = open(matchId + '.html', 'w')
            outputFile.write(str(get_data(line.strip())))
            outputFile.close()
    inputFile.close()