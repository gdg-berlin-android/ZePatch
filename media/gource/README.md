# GENERATE UFOS AND VIDEOS FROM THIS REPOSITORY

[Gource](gource.io) is used to create fancy videos of this repository.

Use `pip install -r requirements.txt` to install all python deps. 

Install `brew imagemagick` for ufos.

Run `./fetch*py` to download faces from the repository and convert them to ufos, because why not.

Finally `gource --config gource.conf -o - | ffmpeg -y -r 60 -f image2pipe -vcodec ppm -i - -vcodec libx264 -preset ultrafast -pix_fmt yuv420p -crf 1 -threads 0 -bf 0 gource.mp4`.

Fun.

> UFO taken from: https://openmoji.org/library/emoji-1F6F8
