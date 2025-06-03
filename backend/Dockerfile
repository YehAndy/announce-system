# 使用 Maven + JDK 21 作為 Builder
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# 複製 pom.xml 和程式碼
COPY pom.xml .
COPY src ./src

# 進行 Maven package（跳過測試）
RUN mvn clean package -DskipTests

# 第二階段：只拿 jar 跑的輕量映像
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/target/*.jar ./backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "backend.jar"]
