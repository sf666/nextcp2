#!/bin/bash
rm -rf deps
mkdir deps
cd deps
git clone https://github.com/sf666/lemma.git
cd lemma
mvn install
cd ..
git clone https://github.com/sf666/seamless.git
cd seamless
mvn install
cd ..
git clone https://github.com/sf666/cling.git
cd cling
mvn install
cd ..
git clone https://bitbucket.org/ijabz/jaudiotagger.git
cd jaudiotagger
mvn install
cd ../..
git clone https://github.com/sf666/musicbrainz.git
cd musicbrainz
mvn install
cd ../..

