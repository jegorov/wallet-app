FROM adoptopenjdk/openjdk11

ENV APP_HOME /home

ENV JAVA_OPTS="-Dspring.profiles.active=h2"

WORKDIR $APP_HOME

COPY wallet-1.0.0.jar $APP_HOME/app.jar

CMD ["sh", "-c","java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar $APP_HOME/app.jar"]