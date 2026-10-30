FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw -q dependency:go-offline
COPY src src
RUN ./mvnw -q clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
RUN useradd -r -u 1001 app
COPY --from=build /app/target/*.jar app.jar
USER app
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=70 -XX:+UseSerialGC"
ENTRYPOINT ["java", "-jar", "app.jar"]