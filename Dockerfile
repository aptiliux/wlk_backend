FROM openjdk:14
COPY . /usr/src/backend
WORKDIR /usr/src/backend
RUN ./mvnw clean package
CMD ["/usr/src/backend/mvnw", "exec:java"]
