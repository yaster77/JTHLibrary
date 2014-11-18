package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Loanable;

import java.io.IOException;

/**
 * @author Christoph
 */
public class LoanableDao extends BaseDao<Loanable> {

    public LoanableDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Loanable getById(int id) throws HttpException {
        Loanable loanable = null;
        try {
            HttpResponse response = get("/Loanable/" + id);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject result = new JSONObject(responseString);

            loanable = LoanableDao.parseFromJson(result);
        } catch (IOException e) {
            Log.e("LoanableDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("LoanableDao", "could not parse JSON result", e);
        }

        return loanable;
    }

    public static Loanable parseFromJson(JSONObject jsonObject) throws JSONException {
        Loanable loanable = new Loanable();

        loanable.setLoanableId(jsonObject.getInt("LoanableId"));
        loanable.setBarcode(jsonObject.getString("Barcode"));
        loanable.setDeleted(jsonObject.getBoolean("Deleted"));
        loanable.setLocation(jsonObject.getString("Location"));
        loanable.setStatus(Loanable.Status.getType(jsonObject.getInt("Status")));
        loanable.setUnavailableFromOwner(jsonObject.getBoolean("UnavailableFromOwner"));

        loanable.setCategory(LocationCategoryDao.parseFromJson((jsonObject.getJSONObject("Category"))));
        loanable.setOwner(UserDao.parseFromJson((jsonObject.getJSONObject("Owner"))));
        loanable.setTitle(TitleDao.parseFromJson((jsonObject.getJSONObject("Title"))));

        return loanable;
    }
}
