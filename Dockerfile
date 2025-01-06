FROM openjdk:17

# ssl 인증서 복사
COPY koreaexim.pem /ssl/koreaexim.pem

# JRE에 ssl 인증서 추가
RUN keytool -import -trustcacerts \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit \
    -noprompt \
    -alias custom-cert \
    -file /ssl/koreaexim.pem

COPY build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
