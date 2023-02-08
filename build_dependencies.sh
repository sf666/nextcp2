#!/bin/bash
rm -rf deps
mkdir deps
cd deps
git clone https://bitbucket.org/ijabz/jaudiotagger.git
cd jaudiotagger
mvn install
cd ../..
git clone https://github.com/sf666/musicbrainz.git
cd musicbrainz
mvn install
cd ../..

