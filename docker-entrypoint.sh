#!/bin/bash
java \
-Dext.properties.dir=/etc/buildit-webcrawler \
-Dspring.profiles.active=$env \
-Dlogs.dir=/var/log/buildit-webcrawler \
-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector \
-jar /opt/buildit-webcrawler/buildit-webcrawler.jar $1