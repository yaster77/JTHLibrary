package se.hj.doelibs.api;

import org.apache.http.HttpException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Publisher;

/**
 * @author Christoph
 */
public class PublisherDao extends BaseDao<Publisher> {

    public PublisherDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Publisher getById(int id) throws HttpException {
        return null;
    }

    public static Publisher parseFromJson(JSONObject jsonObject) throws JSONException {
        Publisher publisher = new Publisher();

        publisher.setPublisherId(jsonObject.getInt("PublisherId"));
        publisher.setName(jsonObject.getString("Name"));

        return publisher;
    }
}
