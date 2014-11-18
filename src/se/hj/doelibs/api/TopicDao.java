package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Topic;

import java.io.IOException;

/**
 * @author Christoph
 */
public class TopicDao extends BaseDao<Topic> {

    public TopicDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Topic getById(int id) throws HttpException {
        Topic topic = null;
        try {
            HttpResponse response = get("/Loanable/" + id);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject result = new JSONObject(responseString);
            JSONObject topicJson = result.getJSONObject("Topic");

            topic = TopicDao.parseFromJson(topicJson);
        } catch (IOException e) {
            Log.e("TopicDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("TopicDao", "could not parse JSON result", e);
        }

        return topic;
    }

    /**
     * creates a Topic from a JSONObject
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static Topic parseFromJson(JSONObject jsonObject) throws JSONException {
        Topic topic = new Topic();

        topic.setTopicId(jsonObject.getInt("TopicId"));
        topic.setName(jsonObject.getString("TopicName"));

        return topic;
    }
}
