aaFROM openjdk:8-jre
RUN mkdir app
ARG JAR_FILE
ADD /target/${JAR_FILE} /app/aplicacao-ionic.jar
WORKDIR /app
ENTRYPOINT java -jar aplicacao-ionic.jar