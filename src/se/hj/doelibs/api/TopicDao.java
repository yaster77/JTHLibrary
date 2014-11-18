package se.hj.doelibs.api;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Topic;

/**
 * @author Christoph
 */
public class TopicDao extends BaseDao<Topic> {

    @Override
    public Topic getById(int id) throws HttpException {
        return null;
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
