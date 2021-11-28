/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stackoverflow.soapservice;

import https.stackoverflow_com.greeting_soap.GetGreetingRequest;
import https.stackoverflow_com.greeting_soap.GetGreetingResponse;
import java.util.HashMap;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GreetingEndpointTest {

    private static final QName NS_NAME_QNAME = QName.valueOf(
            String.format("{%s}%s", SOAPServiceApplication.NAMESPACE_URI, "name")
    );

    @TestConfiguration
    static class TestConfig {

        @Bean
        public Jaxb2Marshaller marshaller() {
            final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setPackagesToScan("https.stackoverflow_com.greeting_soap");
            marshaller.setMarshallerProperties(new HashMap<String, Object>() {
                {
                    put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
                }
            });
            return marshaller;
        }

        @Bean
        public WebServiceTemplate wsTemplate(Jaxb2Marshaller marshaller) {
            return new WebServiceTemplate(marshaller);
        }
    }

    @LocalServerPort
    private int port = 0;

    @Autowired
    private WebServiceTemplate ws;

    @Test
    public void testSendAndReceive() {
        // Given:
        final String NAME = "Test";
        GetGreetingRequest request = new GetGreetingRequest();
        JAXBElement<String> elem = new JAXBElement<>(NS_NAME_QNAME, String.class, NAME);
        request.setName(elem);

        // When:
        GetGreetingResponse result = (GetGreetingResponse) ws.marshalSendAndReceive("http://localhost:"
                + port + "/ws", request);

        // Then:
        assertThat(result != null);
        assertThat(result.getGreeting() != null);
        assertThat(result.getGreeting().getContent() != null);
        assertThat(result.getGreeting().getContent().startsWith("Hello")
                && result.getGreeting().getContent().contains(NAME));
    }

    @Test
    public void testSendAndReceiveNullNameElem() {
        // Given:
        GetGreetingRequest request = new GetGreetingRequest();

        // When:
        GetGreetingResponse result = (GetGreetingResponse) ws.marshalSendAndReceive("http://localhost:"
                + port + "/ws", request);

        // Then:
        assertThat(result != null);
        assertThat(result.getGreeting() != null);
        assertThat(result.getGreeting().getContent() != null);
        assertThat(result.getGreeting().getContent().startsWith("Hello")
                && result.getGreeting().getContent().contains("World"));
    }

    @Test
    public void testSendAndReceiveNullName() {
        // Given:
        GetGreetingRequest request = new GetGreetingRequest();
        JAXBElement<String> elem = new JAXBElement<>(NS_NAME_QNAME, String.class, GetGreetingRequest.class, null);
        request.setName(elem);

        // When:
        GetGreetingResponse result = (GetGreetingResponse) ws.marshalSendAndReceive("http://localhost:"
                + port + "/ws", request);

        // Then:
        assertThat(result != null);
        assertThat(result.getGreeting() != null);
        assertThat(result.getGreeting().getContent() != null);
        assertThat(result.getGreeting().getContent().startsWith("Hello")
                && result.getGreeting().getContent().contains("from XSD"));
    }
}
