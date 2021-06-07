#!/bin/bash
echo "Decoding started..."
image_num="0"
for filename in *
do
    if [[ $filename == exfiltrated* ]]; then
        cat $filename | base64 -d > "image_$image_num".jpg
        echo "Image $((image_num++)) decoded!"
    fi
done