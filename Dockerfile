FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

RUN useradd -m springuser && chown springuser:springuser /app
USER springuser

ENTRYPOINT ["java", "-jar", "app.jar"]