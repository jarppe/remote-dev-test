#
# Minimal JRE:
#

FROM europe-north1-docker.pkg.dev/arcone-390411/docker/alpine-java:latest AS runtime

# Note - Can't use the "build" context above, as that is run on host platform 
#        only. This must be done separately on each target platform.

ARG SERVICE_MODULES=java.base,jdk.httpserver
ARG FIREBASE_MODULES=jdk.crypto.ec,java.management,java.logging

ARG MODULES=${SERVICE_MODULES},${FIREBASE_MODULES}

RUN \
  jlink --add-modules ${MODULES}                                                   \
        --strip-debug                                                              \
        --strip-java-debug-attributes                                              \
        --no-man-pages                                                             \
        --no-header-files                                                          \
        --compress=2                                                               \
        --output /java

#
# Dist image:
#

FROM europe-north1-docker.pkg.dev/arcone-390411/docker/alpine:latest AS dist

COPY --from=runtime  /java  /java

COPY ./target/logo-server-deps.jar  .
COPY ./target/logo-server.jar       .

ENV PATH=/java/bin:$PATH
ENV CLASSPATH="logo-server.jar:logo-server-deps.jar"
ENV JDK_JAVA_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75 -XX:+UseParallelGC"
ENV MODE=prod

USER service

ARG BUILD_DATE
ARG BUILD_ID

ENV BUILD_DATE=${BUILD_DATE}
ENV BUILD_ID=${BUILD_ID}

CMD ["java", "-server", "logo.server.main"]

LABEL project="brand-generator"
LABEL build-date=${BUILD_DATE}
LABEL build-number=${BUILD_ID}
LABEL org.label-schema.build-date=${BUILD_DATE}
LABEL org.label-schema.name="brand-generator-service"
LABEL org.label-schema.description="Brand Generator backend service"
LABEL org.label-schema.schema-version="1.0"
