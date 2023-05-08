FROM adoptopenjdk/maven-openjdk8
ADD k8replace-interface-service/target/k8replace-interface.jar k8replace-interface.jar
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /k8replace-interface.jar
