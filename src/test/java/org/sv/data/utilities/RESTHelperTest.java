package org.sv.data.utilities;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.io.IOException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RESTHelperTest {

    private WireMockServer wireMockServer;

    @BeforeEach
    public void startWireMockServer() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    public void stopWireMockServer() {
        wireMockServer.stop();
    }

    @Test
    void testExecuteGetRequest() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:" + wireMockServer.port() + "/test-url");
        testExecuteRequest(request, "Mock server response");
    }

    private void testExecuteRequest(HttpUriRequest request, String knownResponse) {
        // Stub a request to the URL with the known response
        WireMock.stubFor(
                request(request.getMethod(), urlEqualTo(request.getURI().getPath()))
                        .willReturn(aResponse().withStatus(200).withBody(knownResponse)));

        try {
            // Call RESTHelper.executeRequest with the request
            String response = RESTHelper.executeRequest(request);

            // Assert that the returned response is equal to the known response
            assertEquals(knownResponse, response);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Test failed due to an exception: " + e.getMessage());
        }
    }

    @Test
    void testExecutePostRequest() throws IOException {
        HttpPost request = new HttpPost("http://localhost:" + wireMockServer.port() + "/test-url");
        request.setEntity(new StringEntity("request body"));
        testExecuteRequest(request, "Mock server response");
    }
}
