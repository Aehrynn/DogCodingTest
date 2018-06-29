# DogCodingTest


Java Spring Boot App based on the spring boot starter application

To Build :
mvn clean install

To run :
java -jar dogCodingTest-0.1.0.jar

Runs on port 8080 with an embedded H2 database running in memory

Deployed to AWS:
Web user interface access is provided by Swagger : http://18.130.154.67:8080/swagger-ui.html#/
This lists every REST operation with descriptions and expected return data

The included POSTMAN collection includes the API endpoints and expected data formats

The spring boot app is deployed manually to the RHEL server (ec2) but is set up as a service, this works due to maven properties are set up for the application to be executable and hooks into the RHEL service settings (start/stop/restart)
ex: service dogCodingTest start


