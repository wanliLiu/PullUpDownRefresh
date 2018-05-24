#!/bin/bash

#rm -rf $HOME/.gradle/caches
#rm -rf .gradle

echo "开始编译上传"

#./gradlew clean build uploadArchives

./gradlew clean build bintrayUpload