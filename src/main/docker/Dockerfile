FROM java:8
VOLUME /tmp
#ADD spring-boot-docker-0.0.1-SNAPSHOT.jar /chess.jar
ADD demo-0.0.1-SNAPSHOT.jar /chess.jar
#EXPOSE 12006
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/chess.jar"]
