package nomad.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nomad.JSONWriter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author dderose
 *
 */
@RestController
public class QueryController  extends GetController {

    private static final Logger logger = Logger.getLogger(QueryController.class);

    @ResponseBody
    @RequestMapping("/getCompanyInfo")
    public String doTestQuery(HttpSession session) {
        return this.doQuery(session, "Invoice", "");
    }

    /** Main method to preform and execute Queries */
    public String doQuery(HttpSession session, String tableName, String condition) {

        String realmId = getRealmID(session);

        // Prepare the URL for the get request
        String queryEndpoint = getEndpoint(realmId, tableName, condition);

        // Get the result of the query request
        JSONObject result = doGetRequest(session, queryEndpoint);

        // We've hijacked the above request to get the shipments data. Now we write it out to a file for later work
        String filename = "Shipments";
        JSONWriter writer = new JSONWriter();
        writer.datedWrite(filename, result);

        return result.toString();
    }

    private String getEndpoint(String realmId, String tableName, String condition){
        // All query endpoints end with this version specification
        String queryEnd = "&minorversion=4";

        // Encode the condition into a valid url segment
        String urlCondition;
        try {
            urlCondition = URLEncoder.encode(condition, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This should never happen, since the encoding is hardcoded in
            System.out.println("UnsupportedEncodingException in QueryController. Executing query w/o condition");
            urlCondition = "";
        }

        // Assemble the query section of the endpoint
        String query = "query?query=select%20%2a%20from%20" + tableName + urlCondition + queryEnd;

        // Put together and return the endpoint from the above data
        return String.format("%s/v3/company/%s/%s", oAuth2Configuration.getAccountingAPIHost(), realmId, query);
    }

}
