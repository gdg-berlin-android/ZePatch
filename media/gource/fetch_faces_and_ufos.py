#!/usr/bin/env python3

from os import environ
from requests import get
import sys
import subprocess

try:
    token = environ['GH_TOKEN']
    headers = {"Accept": "application/vnd.github+json","X-GitHub-Api-Version": "2022-11-28","Authorization": f"Bearer {token}"}
except:
    print('No GH_TOKEN in environment, you might get rate limited.')
    headers = None

contributors = get('https://api.github.com/repos/gdg-berlin-android/zepatch/contributors',headers=headers)
print(contributors)
print()

if not contributors.ok:
    print(f"no dice, '{contributors.reason}'.")
else:

    for c in contributors.json():
        print(c['id'])
        print(c['login'])

        name = get(f"https://api.github.com/user/{c['id']}",headers = headers)
        if name.ok:
            name = name.json()['name']
            print(name)
            
            picture = get(c['avatar_url'])
            if picture.ok:
                with open(f"faces/{name}.jpg", 'wb') as file:
                    file.write(picture.content)

                command = ['magick', '-gravity', 'center', 'ufo.png', '(', f'faces/{name}.jpg', '-resize', '256x', '-background', 'None', '-vignette', '8x64', ')', '-composite', f'ufos/{name}.png']
                print(" ".join(command))
                subprocess.run(command)
        else:
            print('noname')



