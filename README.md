# nextCP/2

nextCP/2 is a Java and Typescript web-based UPnP control point.

Screenshots and basic usage is shown in [GitHub Pages](https://sf666.github.io/).

Please see the [Wiki](https://github.com/sf666/nextcp2/wiki) for some documentation.

## installation

- Download prebuild binaries from the [release](https://github.com/sf666/nextcp2/releases) page (JAR files) and put them in a directory with write permissions.
- optionally create a [config file](https://github.com/sf666/nextcp2/wiki/config-file).
- start the application

```
java -jar [-DconfigFile=path_to_config_file] nextcp2.jar
```

If no config file is given or found, a config-file will be generated next to the JAR file on the first startup. In this case the application might complain about incorrect or missing (default) configuration. In this case stop the application, adopt the config to your system and start it again. The configiguration file is [documented here](https://github.com/sf666/nextcp2/wiki/config-file).

## system requirements

- minimum JDK 8
- maven 3.6
- npm 6.14
- GIT client

## browser requirements

- Web browser needs support for server-sent-events

## Install maven dependencies

```diff
! ATTENTION: Lemma needs JDK 8 to build. JDK 9 and higher will not work!
```

Before calling the `./build_dependencies.sh` script or installing build dependencies manually, switch to JDK 8.

Switching between Java versions can be done with SDKMAN : https://sdkman.io/install

list available JDK installations with

```bash
sdk list java
```

and select one Java 8 version by typing

```bash
sdk use java <identifier>
```

### automated dependencies installation

Install dependent libraries by calling the script

```bash
./build_dependencies.sh
```

### manual installation

Install dependent libraries by checking out the repositories in a different directory, following the build instructions of each library. Install each library by calling

```bash
mvn install
```

Repositories to clone:

```bash
git clone https://github.com/sf666/lemma.git
git clone https://github.com/sf666/seamless.git
git clone https://github.com/sf666/cling.git
git clone https://bitbucket.org/ijabz/jaudiotagger.git
git clone https://github.com/sf666/musicbrainz.git
```

## build instructions

Before calling the `./build.sh` script or doing manual build steps, switch to JDK 8 or higher.

Switching between Java versions can be done with SDKMAN : https://sdkman.io/install

list available JDK installations with

```bash
sdk list java
```

select one Java 8 or higher Java version by typing

```bash
sdk use java <identifier>
```

### automated build

call `build.sh`

Build artifacts are located in the `build` directory.

### manual building

Since the frontend is deployed into the backend, it has to be build first.

#### build frontend

```bash
cd frontend/nextcp-ui
./ng build
```

UI will be build into the backend folder : `backend/nextcp2-runtime/src/main/resources/static`

```diff
! Since this directory contains generated content, do not add it to the repository.
```

#### build backend

```bash
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

#### McIntosh device driver

This device driver controls (bi-directional) a McIntosh device connected to a RS232/TCP-IP transceiver like this on: __USR-TCP232-302__.

Current implemented features:

- power control
- volume control

After a successfull build, the device driver (tested with McIntosh MA9000 amplifier) is located here: `backend/nextcp2-device-driver/nextcp2-ma9000/target/`.

# running the application

To run the snapshot - in this example with 256m memory - call :

```bash
java -Xms256m -Xmx256m -jar [-DconfigFile=path_to_config_file] nextcp2.jar
```

or if build manually, replace `nextcp2.jar` with the your current version of the `nextcp2-assembly-spring-boot` file, i.e.

```bash
java -Xms256m -Xmx256m -jar [-DconfigFile=path_to_config_file] nextcp2-assembly-spring-boot-2.0.0-SNAPSHOT.jar
```

By default the application will start on the current interface on port `8085`.

Open your browser and connect to the application:

```
http://localhost:8085
```

If nextcp runs on a remote mashine, replace `localhost` by the IP address of your remote maschine.


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

Launch your favorite chromium browser from the Visual Studio Code debug perspective.

## code generation

Generatied classes are located in the package `codegen` within the maven module `nextcp2-runtime`.

### DTO

DTOs for Java and Typescript are generated to keep data exchange between the rest and SSE interface (Java) consistent with the consuming Typescript code.

#### Java DTOs

The class `DtoModelGen` generates Java-DTO classes configured by the file `dto.yaml` located in the resource folder `src/main/resources/yaml`.

This file has many elements of

```
[CLASS_NAME]:
    [PROPERTY]: [TYPE]
```

Call `DtoModelGen` each time you modify the yaml file. Generate the file into the maven project `nextcp2-modelgen` in the package `nextcp.dto` by pointing to this absolt path as first parameter.

```diff
! ATTENTION: Never modify the generated DTOs files since changes to them will be overwritten by the next call to the generator.
```

#### Typescript DTOs

After generating Java DTOs, Typescript DTO's are automatically generated by the maven build process.

To manually start the Typescript DTO generation, enter the maven project `nextcp2-modelgen` and call `mvn process-classes`.

Typescript DTO's will be generated in the file `nextcp-ui/src/app/service/dto.d.ts`.

```diff
! ATTENTION: Never modify the generated DTO file since changes will be overwritten by the next maven build.
```

### UPnP services

If activated in the configuration file, java code (service classes, input and output classes, event consumer) will be generated for all discovered UPnP services. The generated code uses [cling](https://github.com/4thline/cling) as UPnP stack.
