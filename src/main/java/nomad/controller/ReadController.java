package nomad.controller;

import org.json.JSONObject;

import javax.servlet.http.HttpSession;

public class ReadController extends GetController {

    /** Get a single object with the given id from the given table */
    public JSONObject doRead(HttpSession session, String tableName, int id) {


        String realmId = getRealmID(session);
        String urlBegin = oAuth2Configuration.getAccountingAPIHost();
        String endpoint =  String.format("%s/v3/company/%s/%s/%s?minorversion=4", urlBegin, realmId, tableName, id);

        // Leaving this out of return statement for readability and convenience
        JSONObject result = doGetRequest(session, endpoint);

        return result;
    }
}