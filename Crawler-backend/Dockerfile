FROM openjdk:17
LABEL authors="kangdiwang"

ENV PARAMS=""

#add jar file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Time zone
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8080
EXPOSE 9998

ENTRYPOINT ["java","-jar","/app.jar $PARAMS"]