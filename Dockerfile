# build application
FROM gradle:8.5-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN chmod +x gradle
RUN gradle bootJar --no-daemon
# extract jar
FROM eclipse-temurin:21 AS extraction
WORKDIR workspace
ARG JAR_FILE=workspace/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract
# setup image
FROM eclipse-temurin:21 AS image
#RUN addgroup -S appgroup && adduser -S spring -G appgroup
RUN adduser --system --group spring
USER spring
WORKDIR workspace
COPY --from=extraction workspace/dependencies/ ./
COPY --from=extraction workspace/spring-boot-loader/ ./
COPY --from=extraction workspace/snapshot-dependencies/ ./
COPY --from=extraction workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
