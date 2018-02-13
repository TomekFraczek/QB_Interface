package nomad.controller;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

public class PostController  extends RequestController {

    private static final Logger logger = Logger.getLogger(PostController.class);

    /** Performs a GET request to execute a query, and returns the result as a JSONObject */
    protected JSONObject doPostRequest(HttpSession session, String postEndpoint, JSONObject postData) {

        // Turn the endpoint into a valid HTTP GET request
        HttpPost postRequest = new HttpPost(postEndpoint);

        // Append the data to the request
        try {
            postRequest.setEntity(new StringEntity(postData.toString()));
        } catch (UnsupportedEncodingException e) {
            System.out.print("Unsupported Encoding exception!" + e);
        }

        // Build the headers defining accepted and provided content types
        postRequest.setHeader("Accept", "application/json");
        postRequest.setHeader("Content-type", "application/json");

        // Build headers to provide authorization
        String accessToken = (String)session.getAttribute("access_token");
        postRequest.setHeader("Authorization","Bearer " + accessToken);

        return doRequest(session, postRequest);
    }
}
