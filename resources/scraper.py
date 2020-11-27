import requests
import re
import json
import sys
import datetime
import pytz
from io import StringIO

def get_html(url):
    response = requests.get(url)
    if not response.ok:
        print(f'Code: {response.status_code}, url: {url}')
    return response.text


def get_patterns():
    return ['"video":{"_id":"(.*?)"', '"owner":\[{"_id":"(.*?)"', '"title":"(.*?)"', '"name":"(.*?)"','"lastName":"(.*?)"', '"creationDate":"(.*?)"', '"duration":(.*?),']


def get_data(patterns, html):
    data=[]
    for pattern in patterns:
         data.append(list(re.findall(pattern, html)))
    return data

def get_videos(html):
    patterns = get_patterns()
    data =get_data(patterns, html)
    keys = ['owner', 'title', 'name', 'lastName', 'creationDate', 'duration']

    videos = {}
    for i in range(len(data[0])):
        videos[data[0][i]]={}
        for j in range(len(keys)):
            videos[data[0][i]].update({keys[j]:data[j+1][i]})
    return videos

def save_query_time_results(query_time_results):
    io = StringIO()
    json.dump(query_time_results, io)
    print(io.getvalue())


def main(query, itemsPerPage):
    url = f'https://media.upv.es/rest/plugins/portal/search?itemsPerPage={itemsPerPage}&q={query}'
    timeEM = str(datetime.datetime.now(pytz.timezone('Europe/Madrid')))
    html = get_html(url)
    videos = get_videos(html)
    output = {query: [timeEM,videos]}
    save_query_time_results(output)

main(sys.argv[1],sys.argv[2])