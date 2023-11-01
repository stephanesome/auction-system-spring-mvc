# build application
FROM gradle:7.6.3-jdk17 AS build
WORKDIR /workspace
COPY . .
RUN  ./gradlew bootJar --no-daemon
# extract jar
FROM openjdk:17-alpine AS extraction
WORKDIR workspace
ARG JAR_FILE=workspace/build/libs/*.jar
COPY --from=build ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract
# setup image
FROM openjdk:17-alpine AS image
RUN addgroup -S appgroup && adduser -S spring -G appgroup
USER spring
WORKDIR workspace
COPY --from=extraction workspace/dependencies/ ./
COPY --from=extraction workspace/spring-boot-loader/ ./
COPY --from=extraction workspace/snapshot-dependencies/ ./
COPY --from=extraction workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
