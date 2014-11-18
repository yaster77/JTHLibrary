package se.hj.doelibs.api;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.User;
import se.hj.doelibs.model.UserCategory;

/**
 * @author Christoph
 */
public class UserDao extends BaseDao<User> {

    @Override
    public User getById(int id) throws HttpException {
        return null;
    }

    /**
     * creates a userobject out of a JSONObject
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public static User parseFromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        UserCategory userCat = new UserCategory();

        //get all basic fields
        user.setUserId(jsonObject.getInt("UserId"));
        user.setEmail(jsonObject.getString("EMail"));
        user.setFirstName(jsonObject.getString("FirstName"));
        user.setLastName(jsonObject.getString("LastName"));

        //room + phone is only at staff member available
        if(jsonObject.has("Room") && !jsonObject.isNull("Room")) {
            user.setRoom(jsonObject.getString("Room"));
        } else {
            user.setRoom(null);
        }

        if(jsonObject.has("UniPhone") && !jsonObject.isNull("UniPhone")) {
            user.setUniPhone(jsonObject.getString("UniPhone"));
        } else {
            user.setUniPhone(null);
        }

        //get usercategory
        JSONObject categoryJson = jsonObject.getJSONObject("Category");
        userCat.setCategoryId(categoryJson.getInt("CategoryId"));
        userCat.setName(categoryJson.getString("Name"));
        userCat.setLoanPeriod(categoryJson.getInt("LoanPeriod"));

        user.setCategory(userCat);

        return user;
    }
}
