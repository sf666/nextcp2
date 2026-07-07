# Runtime image for nextCP/2.
#
# This is a thin runtime image: it expects the build artifacts to already exist
# (produced by CI or by ./build.sh) and copies them into a public JRE base
# image. It intentionally does NOT bake in any configuration - point a volume at
# /etc/nextcp2 (or let the app generate a default config on first start).
#
# Expected artifacts (relative to the build context = repo root):
#   backend/nextcp2-assembly/target/nextcp2.jar
#   backend/nextcp2-device-driver/nextcp2-ma9000/target/device_driver_ma9000.jar
FROM eclipse-temurin:25-jre

# Runtime directories used by the application.
RUN mkdir -p /nextcp2/lib /nextcp2/logs /nextcp2/data /nextcp2/upnp_code /etc/nextcp2

COPY backend/nextcp2-assembly/target/nextcp2.jar /nextcp2/nextcp2.jar
COPY backend/nextcp2-device-driver/nextcp2-ma9000/target/device_driver_ma9000.jar /nextcp2/lib/

# Sensible default heap; override at runtime via JAVA_TOOL_OPTIONS (e.g. in
# docker-compose) to enable a different GC or change memory limits.
ENV JAVA_TOOL_OPTIONS="-Xms1024m -Xmx2048m"
ENV TZ=Europe/Berlin

# The embedded server listens here. UPnP/SSDP discovery needs host networking,
# so run the container with --network host (see docker-compose.yml).
EXPOSE 8085

WORKDIR /nextcp2
ENTRYPOINT ["java", "-jar", "nextcp2.jar"]
