FROM openjdk:8-jre-alpine

MAINTAINER Marcel Kowynia

ENV env=dev
ENV app=buildit-webcrawler
ENV main=com.buildit.BuilditWebcrawler


COPY env/* /etc/buildit-webcrawler/$env
ADD target/buildit-webcrawler-1.0-jar-with-dependencies.jar /opt/buildit-webcrawler/buildit-webcrawler.jar
COPY docker-entrypoint.sh /opt/buildit-webcrawler/
RUN ["chmod", "+x", "/opt/buildit-webcrawler/docker-entrypoint.sh"]
RUN ln -s /opt/buildit-webcrawler/docker-entrypoint.sh docker-entrypoint.sh
VOLUME ["/tmp/buildit", "/opt/buildit-webcrawler"]
RUN mkdir -p /var/log/buildit-webcrawler/
RUN echo $env
ENTRYPOINT ["sh","docker-entrypoint.sh"]

