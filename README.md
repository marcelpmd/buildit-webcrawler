# Buildit Webcrawler
Web Crawler for http://wiprodigital.com

## Fresh Installation

### Download Docker for Mac
https://download.docker.com/mac/stable/Docker.dmg


### Run Webcrawler
```
docker run marcelpmd/buildit-webcrawler:latest http://wiprodigital.com > output.txt
```

### Install Maven
```
brew install maven
```

### Run Tests
```
mvn clean test
```

### Reasoning

This solution demonstrates a basic understanding and implementation of necessary software components used in practice
such as logging, external configuration and automatic build process. Although the instructions are specific to MacOS
this solution is environment agnostic as it is fully containarized. With the limited amount of time
I did not demonstrate other aspects such as dependency injection, better output formatting etc.
