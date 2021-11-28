# soq21320913
Answer Repo for [Stackoverflow Question: Convert REST Spring Boot Example to SOAP](https://stackoverflow.com/q/21320913/592355).

Shows the migration from a [simple REST service](https://spring.io/guides/gs/rest-service/) ([repo](https://github.com/spring-guides/gs-rest-service)) to its SOAP variant ([tutorial](https://spring.io/guides/gs/producing-web-service/)|[repo](https://github.com/spring-guides/gs-producing-web-service/)).

## Prerequisites
    
- JDK 1.8

- bash like command line shell/interface

- `curl`/SOAP browser
    
## Build

### Clean

    ./gradlew clean

### (Re-)Generate jaxb classes

    ./gradlew genJaxb

### Run Tests

    ./gradlew test


### Run Webservice

    ./gradlew bootRun
   
## Usage

### Get WSDL

    curl http://localhost:8080/ws/greetings.wsdl

### Input XML

Let request.xml:

```
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:gs="http://stackoverflow.com/greeting-soap/">
  <soapenv:Header/>
  <soapenv:Body>
    <gs:getGreetingRequest>
      <gs:name>Joe</gs:name>
    </gs:getGreetingRequest>
  </soapenv:Body>
</soapenv:Envelope>
```

### Invocation

With above request.xml

    curl --header "content-type: text/xml" -d @request.xml http://localhost:8080/ws > output.xml
