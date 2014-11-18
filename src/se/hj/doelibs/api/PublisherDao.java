package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Publisher;

import java.io.IOException;

/**
 * @author Christoph
 */
public class PublisherDao extends BaseDao<Publisher> {

    public PublisherDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Publisher getById(int id) throws HttpException {
        Publisher publisher = null;
        try {
            HttpResponse response = get("/Loanable/" + id);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject result = new JSONObject(responseString);

            publisher = PublisherDao.parseFromJson(result);
        } catch (IOException e) {
            Log.e("PublisherDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("PublisherDao", "could not parse JSON result", e);
        }

        return publisher;
    }

    public static Publisher parseFromJson(JSONObject jsonObject) throws JSONException {
        Publisher publisher = new Publisher();

        publisher.setPublisherId(jsonObject.getInt("PublisherId"));
        publisher.setName(jsonObject.getString("Name"));

        return publisher;
    }
}
