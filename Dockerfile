FROM node:13-alpine AS node
WORKDIR /node
COPY captain-adhoc-frontend/package.json ./
RUN npm install
COPY captain-adhoc-frontend/ ./
RUN apk update && apk upgrade && \
    echo @edge http://nl.alpinelinux.org/alpine/edge/community >> /etc/apk/repositories && \
    echo @edge http://nl.alpinelinux.org/alpine/edge/main >> /etc/apk/repositories && \
    apk add bash chromium@edge nss@edge
ENV CHROME_BIN chromium-browser
RUN npm run unit
RUN npm run build

FROM maven:3-jdk-8-alpine AS maven
WORKDIR /maven
COPY captain-adhoc-backend/pom.xml ./
RUN mvn dependency:go-offline -B
COPY captain-adhoc-backend/src/ ./src
COPY --from=node /node/dist/ ./src/main/resources/static
RUN mvn package


FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=maven /maven/target/*.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Xmx500m", "-jar", "./app.jar"]
