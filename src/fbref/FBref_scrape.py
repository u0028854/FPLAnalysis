import requests
from bs4 import BeautifulSoup
import warnings

def get_player_data(x):
    warnings.filterwarnings("ignore")
    url = x
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')
    return soup

with open('c:\\EPLJava\\stat_tables\\fbref_team.html', 'w') as f:
    f.write(str(get_player_data("http://fbref.com/en/comps/9/Premier-League-Stats").encode("utf-8")))

with open('c:\\EPLJava\\stat_tables\\fbref.html', 'w') as f:
    f.write(str(get_player_data("https://fbref.com/en/comps/9/stats/Premier-League-Stats").encode("utf-8")))
