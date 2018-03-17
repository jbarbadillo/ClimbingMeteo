package com.javirock.meteoclimb;


import com.javirock.meteoclimb.models.ApiNetwork;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;

public class MockServerTest {
    public final MockWebServer server = new MockWebServer();

    /**
     * Dispatcher for mocking responses
     */
    final Dispatcher dispatcher = new Dispatcher(){
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException{
            if (request.getPath().equals("https://www.remote.com/api/vo/isAlive")){
                return new MockResponse().setResponseCode(200)
                        .addHeader("Content-type","application/json")
                        .setBody("{}");
            } else if (request.getPath().equals("/user")){
                return new MockResponse().setResponseCode(200).setBody("version=9");
            } else if (request.getPath().equals("/checkImage")) {
                return new MockResponse().setResponseCode(200).setBody("{\\\"info\\\":{\\\"name\":\"Lucas Albuquerque\",\"age\":\"21\",\"gender\":\"male\"}}");
            }
            return new MockResponse().setResponseCode(404);
        }

    };
    @Before
    public void setupTest(){
        try{
            //server.setDispatcher(dispatcher);
            server.start();
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
    @Test
    public void is_alive_request_returns_200() throws Exception {
        MockResponse mockedResponse = new MockResponse();
        mockedResponse.setResponseCode(200);
        mockedResponse.setBody("{}");

        server.enqueue(mockedResponse);

        ApiNetwork.isAlive();

        RecordedRequest request1 = server.takeRequest();
        assertEquals("https://www.remote.com/api/vo/isAlive", request1.getPath());
    }

}
