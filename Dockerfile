FROM openjdk:11-jdk-alpine

ENV APP_HOME /home

ENV JAVA_OPTS="-Dspring.profiles.active=h2"

WORKDIR $APP_HOME

COPY wallet.jar $APP_HOME/app.jar

CMD ["sh", "-c","java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar $APP_HOME/app.jar"]