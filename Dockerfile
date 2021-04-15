FROM adoptopenjdk:11-jre-hotspot

WORKDIR /usr

RUN mkdir /volume

COPY ./build/libs/*.jar ./app/app.jar

ENV ACTIVE_PROFILE=aws
ENV PERSISTENCE_VOLUME=/usr/volume
ENV AWS_REGION=us-east-1
ENV AWS_ACCESS_KEY=AKIAZVBKEQUPY6LPQZX3
ENV AWS_SECRET_KEY=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
ENV AWS_BUCKET_NAME=bucket-for-image-storage

EXPOSE 8081

CMD ["java","-jar","./app/app.jar"]