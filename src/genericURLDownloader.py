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
    outputFileName = sys.argv[1][sys.argv[1].index('//') + 2:].replace('/', '-')
    print (outputFileName)
    outputFile = open(outputFileName + '.html', 'w')
    outputFile.write(str(get_data(sys.argv[1])))
    outputFile.close()