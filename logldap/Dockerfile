FROM openjdk:17-slim

COPY ./build/libs/scan-log4shell-logldap-fat-*-all.jar /srv/logldap.jar

ENV JAVA_OPTS="-Xmx256m -Djava.security.egd=file:/dev/./urandom"

ENV LDAP_PORT="389"

EXPOSE 389

CMD ["sh", "-c", "java $JAVA_OPTS -jar /srv/logldap.jar -p $LDAP_PORT"]
