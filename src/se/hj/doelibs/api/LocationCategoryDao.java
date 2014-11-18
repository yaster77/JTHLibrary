package se.hj.doelibs.api;

import org.apache.http.HttpException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.LocationCategory;

/**
 * @author Christoph
 */
public class LocationCategoryDao extends BaseDao<LocationCategory> {

    public LocationCategoryDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public LocationCategory getById(int id) throws HttpException {
        return null;
    }

    public static LocationCategory parseFromJson(JSONObject jsonObject) throws JSONException {
        LocationCategory locCat = new LocationCategory();

        locCat.setCategoryId(jsonObject.getInt("CategoryId"));
        locCat.setName(jsonObject.getString("Name"));
        locCat.setOwner(UserDao.parseFromJson(jsonObject.getJSONObject("Owner")));

        return locCat;
    }
}
