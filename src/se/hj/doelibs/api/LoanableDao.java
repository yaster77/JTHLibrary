package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Loan;
import se.hj.doelibs.model.Loanable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * checks a loanable out
     *
     * @param loanableId
     * @return
     */
    public Loan checkOut(int loanableId) {
        Loan loan = null;
        try {
            HttpResponse response = post("/Loanable/" + loanableId, null);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject result = new JSONObject(responseString);

            loan = LoanDao.parseFromJson(result);
        } catch (IOException e) {
            Log.e("LoanableDao", "Exception on PUT request", e);
        } catch (HttpException e) {
            Log.e("LoanableDao", "Exception on PUT request", e);
        } catch (JSONException e) {
            Log.e("LoanableDao", "Could not parse JSON", e);
        }

        return loan;
    }

    /**
     * checks the given loanable back in.
     * This method takes the loanableId instead of the loanId because in some cases we have only the loanableId.
     * If you have the loanId, you should use the LoanDao.checkIn(loanId) because it will be faster
     *
     * @param loanableId
     * @return
     */
    public boolean checkInByLoanableId(int loanableId) {
        //get the loanId for this loanable from the current users loans:
        LoanDao loanDao = new LoanDao(credentials);

        for (Loan loan : loanDao.getCurrentUsersLoans()) {
            if(loan.getLoanable().getLoanableId() == loanableId) {
                return loanDao.checkIn(loan.getLoanId());
            }
        }
        return false;
    }

    /**
     * Adds a new loanable to DoeLibS
     *
     * @param titleId
     * @param doeLibSId
     * @param locationCategory
     * @param room
     * @return
     */
    public boolean addLoanableBasic(int titleId, String doeLibSId, String locationCategory, String room) throws HttpException {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Barcode", doeLibSId));
        nameValuePairs.add(new BasicNameValuePair("Location", room));
        nameValuePairs.add(new BasicNameValuePair("Category", locationCategory));

        boolean success = false;
        try {
            HttpResponse response = post("/Title/"+titleId, nameValuePairs);
            checkResponse(response);

            success = true;
        } catch (IOException e) {
            Log.d("Add loanable", "IOException on POST request", e);
        }

        return success;
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
