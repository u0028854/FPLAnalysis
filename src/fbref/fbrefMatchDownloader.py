import requests, sys, re, warnings, codecs
from bs4 import BeautifulSoup, NavigableString

base_url = 'https://fbref.com';

def get_player_data(x):
    warnings.filterwarnings("ignore")
    url = x
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')
    return soup

fullMatchList = open('test.html', 'r').read().split('\n')
processedMatchList = open('test2.html', 'r').read().split('\n')

# print(len(fullMatchList))
# print(len(processedMatchList))

for processedMatch in processedMatchList:
    fullMatchList.remove(processedMatch)

# print(fullMatchList)

for fullMatch in fullMatchList:
    print(fullMatch)
    matchURLs = fullMatch.split('/')
    # print(matchURL.pop(len(matchURL) - 1))
    with open(matchURLs.pop(len(matchURLs) - 1), 'w') as f:
        f.write(str(get_player_data(fullMatch).encode("utf-8")))
