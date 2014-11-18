package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.User;
import se.hj.doelibs.model.UserCategory;

import java.io.IOException;

/**
 * @author Christoph
 */
public class UserDao extends BaseDao<User> {

    public UserDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public User getById(int userId) throws HttpException {
        User user = null;

        try {
            HttpResponse response = get("/User/" + userId);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject userJson = new JSONObject(responseString);

            //get basic userinformation
            user = UserDao.parseFromJson(userJson);

        } catch (IOException e) {
            Log.e("AuthorDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("AuthorDao", "could not parse JSON result", e);
        }

        return user;
    }

    /**
     * returns the object of the current logged in user
     *
     * @return
     * @throws HttpException
     */
    public User getCurrentLoggedin() throws HttpException {
        User user = null;

        try {
            HttpResponse response = get("/User/");

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject userJson = new JSONObject(responseString);

            //get basic userinformation
            user = UserDao.parseFromJson(userJson);

        } catch (IOException e) {
            Log.e("AuthorDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("AuthorDao", "could not parse JSON result", e);
        }

        return user;
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
