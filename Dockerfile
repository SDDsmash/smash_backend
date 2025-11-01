############################
# 1) JAR BUILD (Gradle)
############################
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /workspace

# a) Gradle 캐시 최적화
COPY gradlew ./gradlew
COPY gradle/wrapper/ ./gradle/wrapper/
COPY settings.gradle* build.gradle* gradle.properties* ./
RUN chmod +x ./gradlew

RUN --mount=type=cache,target=/root/.gradle ./gradlew --no-daemon -q help

# b) 애플리케이션 소스 복사
COPY src ./src

# c) 빌드(테스트 제외) + 산출물 이름 고정(*-plain.jar 제외)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon clean bootJar -x test && \
    JAR_FILE=$(ls build/libs | grep -E '\.jar$' | grep -v 'plain' | head -n 1) && \
    mv "build/libs/$JAR_FILE" app.jar



############################
# 2) RUNTIME
############################
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=40 -XX:+UseStringDeduplication -XX:+ExitOnOutOfMemoryError -Duser.timezone=Asia/Seoul -Dfile.encoding=UTF-8"

COPY --from=builder /workspace/app.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]