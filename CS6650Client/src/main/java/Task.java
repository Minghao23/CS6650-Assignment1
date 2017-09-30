
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import java.util.concurrent.Callable;

import static java.net.HttpURLConnection.HTTP_OK;

public class Task implements Callable<Metric> {
    private static final String GET_ENDPOINT = "/CS6650Project_war_exploded/hello";
    private static final String POST_ENDPOINT = "/CS6650Project_war_exploded/hello";
    private static final String PROTOCOL = "http://";

    private int iterationNum;
    private Metric metric;
    private WebTarget getWebTarget;
    private WebTarget postWebTarget;
    private String endpointGet;
    private String endpointPost;


    public Task(String ip, int port, int iterationNum) {
        this.iterationNum = iterationNum;
        endpointGet = String.format("%s%s:%d%s", PROTOCOL, ip, port, GET_ENDPOINT);
        endpointPost = String.format("%s%s:%d%s", PROTOCOL, ip, port, POST_ENDPOINT);
        metric = new Metric();
    }

    private void doGet() {
        long startTime = System.currentTimeMillis();
        boolean isSent;
        Response response = null;
        try {
            response = getWebTarget.request(MediaType.TEXT_PLAIN).get();
            isSent = true;
            response.close();
        } catch (ProcessingException e) {
            isSent = false;
        }
        long curLatency = System.currentTimeMillis() - startTime;
        boolean curIsSuccess = isSent && response.getStatus() == HTTP_OK;
        metric.addRequest(curLatency, isSent, curIsSuccess);
    }

    private void doPost() {
        boolean isSent;
        Response response = null;
        long startTime = System.currentTimeMillis();
        MultivaluedHashMap<String, String> data = new MultivaluedHashMap<String, String>();
        data.add("content", "Hello World!");
        try {
            response = postWebTarget.request().post(Entity.form(data));
            isSent = true;
            response.close();
        } catch (ProcessingException e) {
            isSent = false;
        }
        long curLatency = System.currentTimeMillis() - startTime;
        boolean curIsSuccess = isSent && response.getStatus() == HTTP_OK;
        metric.addRequest(curLatency, isSent, curIsSuccess);
    }

    public Metric call() throws Exception {
        Client client = ClientBuilder.newClient();
        getWebTarget = client.target(endpointGet);
        postWebTarget = client.target(endpointPost);
        metric = new Metric();

        for (int i = 0; i < iterationNum; ++i) {
            doGet();
            doPost();
        }
        client.close();
        return metric;
    }
}
