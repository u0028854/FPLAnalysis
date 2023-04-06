import pandas as pd
import xarray as xr

teamHTML = pd.read_html('https://fbref.com/en/comps/9/Premier-League-Stats')
df = pd.DataFrame(teamHTML[0])
# print (df.loc[:,['Squad', 'xG', 'xGA']])

playerHTML = pd.read_html('https://fbref.com/en/comps/9/shooting/Premier-League-Stats#stats_shooting')
df = pd.DataFrame(playerHTML[1])

# for idx,table in enumerate(teamHTML):
#  print(idx)
#  print(table.encode("utf-8"))

print (df.to_csv(index=False).encode("utf-8"))

# print(f'Total tables: {len(playerHTML)}')
