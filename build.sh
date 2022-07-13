#!/bin/bash

rm -rf backend/nextcp2-runtime/src/main/resources/static/

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
cp nextcp2-assembly/target/nextcp2.jar ../build
cp nextcp2-assembly/target/nextcp2.exe ../build
cp nextcp2-device-driver/nextcp2-ma9000/target/device_driver_ma9000.jar ../build/nextcp2-ma9000.jar

