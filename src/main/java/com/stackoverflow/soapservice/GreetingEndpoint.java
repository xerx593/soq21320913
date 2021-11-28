package com.stackoverflow.soapservice;

import com.stackoverflow.greeting_soap.GetGreetingRequest;
import com.stackoverflow.greeting_soap.GetGreetingResponse;
import com.stackoverflow.greeting_soap.Greeting;
import static com.stackoverflow.soapservice.SOAPServiceApplication.NAMESPACE_URI;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class GreetingEndpoint {

    private static final String HELLO_TPL = "Hello, %s!";
    private static final AtomicLong COUNTER = new AtomicLong();

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getGreetingRequest")
    @ResponsePayload
    public GetGreetingResponse sayHello(@RequestPayload GetGreetingRequest request) {
        GetGreetingResponse response = new GetGreetingResponse();

        Greeting greeting = new Greeting();
        greeting.setContent(
                String.format(
                        HELLO_TPL, request.getName() == null ? "World" : request.getName()
                )
        );
        greeting.setId(COUNTER.incrementAndGet());

        response.setGreeting(greeting);
        return response;
    }
}
