# nextCP/2

nextCP/2 is a Java and Typescript web-based UPnP control point.

Screenshots and basic usage is shown in [GitHub Pages](https://sf666.github.io/).

Please see the [Wiki](https://github.com/sf666/nextcp2/wiki) for some documentation.

## installation

- Download from the [release](https://github.com/sf666/nextcp2/releases) page the latest nextcp2.jar file and optinally device drivers.
- optionally create a [config file](https://github.com/sf666/nextcp2/wiki/config-file). If no config file is specified, a default config file will be created next to the jar file.
- start the application

```
java -jar [-DconfigFile=path_to_config_file] nextcp2.jar
```

If no config file is given or found, a config-file will be generated next to the JAR file on the first startup. In this case the application will complain about missing configuration elements. Stop the application, adopt the config to your system and start it again. The configiguration file is [documented here](https://github.com/sf666/nextcp2/wiki/config-file).

## system requirements

- JDK 8 (for dependent libraries)
- JDK 11
- maven 3.6
- npm 6.14
- GIT client

## browser requirements

- Web browser needs support for server-sent-events

## Install maven dependencies

```diff
! ATTENTION: Lemma needs JDK 8 to build!
```
Before calling the `./build_dependencies.sh` script or installing build dependencies manually, switch to JDK 8.

Switching between Java versions can be done with SDKMAN : https://sdkman.io/install

list available JDK installations with

```
sdk list java
```

and select one Java 8 version by typing

```
sdk use java <identifier>
```

### automated dependencies installation


Install dependent libraries by calling the script 

```
./build_dependencies.sh
```


### manual installation

Install dependent libraries by checking out the repositories in a different directory, following the build instructions of each library. Install each library by calling

```
mvn install
```

Repositories to clone:
 
```
git clone https://github.com/sf666/lemma.git
git clone https://github.com/sf666/seamless.git
git clone https://github.com/sf666/cling.git
git clone https://bitbucket.org/ijabz/jaudiotagger.git
```

## build instructions

Before calling the `./build.sh` script or doing manual build steps, switch to JDK 11.

Switching between Java versions can be done with SDKMAN : https://sdkman.io/install

list available JDK installations with

```
sdk list java
```

select one Java 11 or higher Java version by typing

```
sdk use java <identifier>
```

### automated build

call `build.sh`

Build artifacts are located in the `build` directory.

### manual building

Since the frontend is deployed into the backend, it has to be build first.

#### build frontend

```
cd frontend/nextcp-ui
./ng build
```

UI will be build into the backend folder : `backend/nextcp2-runtime/src/main/resources/static`

```diff
! Since this directory contains generated content, do not add it to the repository.
```

#### build backend

```
cd backend/
mvn clean
mvn install
mvn package
```

### build artifact

Build artifacts are located in the maven `target` directories. 

- The runnable application jar is build in the module `backend/nextcp2-assembly/target`
- Device Driver are build in the modules below `backend/nextcp2-device-driver`


#### main application

After a successfull build, the main application build artifact will be located here `backend/nextcp2-assembly/target`

#### McIntosch device driver

This device driver controls (bi-directional) a McIntosch device connected to a RS232/TCP-IP transceiver.

Current implemented features:

- power control
- volume control

After a successfull build, the device driver (tested with McIntosh MA9000 amplifier) is located here: `backend/nextcp2-device-driver/nextcp2-ma9000/target/`.


# running the application

To run the snapshot call :

```
java -jar [-DconfigFile=path_to_config_file] nextcp2.jar
```

or if build manually, replace `nextcp2.jar` with the your current version of the `nextcp2-assembly-spring-boot` file, i.e.

```
java -jar [-DconfigFile=path_to_config_file] nextcp2-assembly-spring-boot-2.0.0-SNAPSHOT.jar
```

By default the application will start on the current interface on port `8085`. 

Open your browser and connect to the application:

```
http://localhost:8085
```

If nextcp runs on a remote maschine, replace `localhost` by the IP address of your remote maschine.


## config file 

The application tries to load the config file from the following locations in this order:

1. file provided by system-property 'configFile'
2. file located '/etc/nextcp2/nextcp2Config.json'
3. file located 'USER_HOME/nextcp2Config.json'
4. file located 'WORK_DIR/nextcp2Config.json'

If no config file is found, a config file will be generated at this location : 'WORK_DIR/nextcp2Config.json'.

See the Wiki [config file page](https://github.com/sf666/nextcp2/wiki/config-file) for documentation.

# developer notice

## debugging

For debugging within an IDE start the backend first. The main Spring-Boot startup class is

```
backend/nextcp2-assembly/src/main/java/nextcp/NextcpApplicationStartup
```

To start the front-end in Visual Studio Code switch to TERMINAL change into the directory `nextcp2/frontend/nextcp-ui` and start the front-end by typing

```
npm start
```

Launch your favorite Browser from the Visual Studio Code debug perspective.
