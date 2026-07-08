# Runtime image for nextCP/2.
#
# This is a thin runtime image: it expects the build artifacts to already exist
# (produced by CI or by ./build.sh) and copies them into a public JRE base image.
#
# All mutable state lives under the data directory NEXTCP_DATA (=/nextcp2_data).
# On first start the app generates its config, database, logback config and the
# logs/ and upnp_code/ sub-directories there. Mount a volume at /nextcp2_data to
# persist everything across container restarts and image updates (see the
# docker-compose example in the docs).
#
# Expected artifacts (relative to the build context = repo root):
#   backend/nextcp2-assembly/target/nextcp2.jar
#   backend/nextcp2-device-driver/nextcp2-ma9000/target/device_driver_ma9000.jar
FROM eclipse-temurin:25-jre

COPY backend/nextcp2-assembly/target/nextcp2.jar /nextcp2/nextcp2.jar
COPY backend/nextcp2-device-driver/nextcp2-ma9000/target/device_driver_ma9000.jar /nextcp2/lib/

# Data directory for config, database and logs. The app reads this env var and
# creates the directory (and its logs/ + upnp_code/ sub-dirs) on first start.
ENV NEXTCP_DATA=/nextcp2_data
VOLUME ["/nextcp2_data"]

# Device-driver library directory: the generated default config points libraryPath
# here so the bundled MA9000/MA12000 driver (copied to /nextcp2/lib above) is found.
ENV NEXTCP_LIB=/nextcp2/lib

# Sensible default heap; override at runtime via JAVA_TOOL_OPTIONS (e.g. in
# docker-compose) to enable a different GC or change memory limits.
ENV JAVA_TOOL_OPTIONS="-Xms1024m -Xmx2048m"
ENV TZ=Europe/Berlin

# The embedded server listens here. UPnP/SSDP discovery needs host networking,
# so run the container with --network host (see docker-compose.yml).
EXPOSE 8085

WORKDIR /nextcp2
ENTRYPOINT ["java", "-jar", "nextcp2.jar"]
