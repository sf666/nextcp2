#!/bin/bash

if [ ! -d backend/nextcp2-runtime/src/main/resources/static ]; then
  echo "creating static folder ..."
  mkdir backend/nextcp2-runtime/src/main/resources/static
fi

cd frontend/nextcp-ui/
npm install
./ng build
cd ../../backend/

mvn clean
mvn install
mvn package

rm -rf ../build
mkdir ../build
cp nextcp2-assembly/target/nextcp2-assembly-spring-boot-2.0.0-SNAPSHOT.jar ../build/nextcp2.jar
cp nextcp2-device-driver/nextcp2-ma9000/target/nextcp2-ma9000-2.0.0-SNAPSHOT.jar ../build/nextcp2-ma9000.jar

