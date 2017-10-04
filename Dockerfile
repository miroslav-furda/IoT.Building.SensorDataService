# Maven 3.3.9
# Java openjdk8
# for deploy purpose

FROM gliderlabs/alpine:3.4
MAINTAINER Peter Szatmary <peter.szatmary@gmail.com>

ENV REFRESHED_AT 2017-10-04

ENV MAVEN_VERSION 3.3.9
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8

ENV JAVA_HOME /usr/lib/jvm/default-jvm
ENV PATH $PATH:$JAVA_HOME

ENV M2_HOME /home/codexa/etc/apache-maven-$MAVEN_VERSION
ENV M2 $M2_HOME/bin
ENV PATH $PATH:$M2

RUN apk-install bash ca-certificates openjdk8 wget

RUN adduser codexa -h /home/codexa -s /bin/bash -D
USER codexa
WORKDIR /home/codexa/
RUN mkdir etc
WORKDIR /home/codexa/etc

RUN wget http://tux.rainside.sk/apache/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz
RUN tar -xvf apache-maven-$MAVEN_VERSION-bin.tar.gz
RUN rm -r apache-maven-$MAVEN_VERSION-bin.tar.gz

WORKDIR /home/codexa/darwin-sensor-data-rest

# Bundle app source
COPY . /home/codexa/darwin-sensor-data-rest

EXPOSE 7777

RUN mvn package

ENTRYPOINT ["java", "-jar", "./target/darwin-sensor-data-rest.jar"]