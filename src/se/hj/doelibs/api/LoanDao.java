package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Loan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph
 */
public class LoanDao extends BaseDao<Loan> {

    public LoanDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Loan getById(int id) throws HttpException {
        Loan loan = null;
        try {
            HttpResponse response = get("/Loan/" + id);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject result = new JSONObject(responseString);

            loan = LoanDao.parseFromJson(result);
        } catch (IOException e) {
            Log.e("LoanDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("LoanDao", "could not parse JSON result", e);
        }

        return loan;
    }

    /**
     * returns the logged in users current loans
     *
     * @return list of loans
     */
    public List<Loan> getCurrentUsersLoans() {
        List<Loan> loans = new ArrayList<Loan>();
        try {
            HttpResponse response = get("/Loan/");

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONArray result = new JSONArray(responseString);
            for(int i = 0; i<result.length();i++) {
                loans.add(LoanDao.parseFromJson(result.getJSONObject(i)));
            }
        } catch (IOException e) {
            Log.e("LoanDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("LoanDao", "could not parse JSON result", e);
        } catch (HttpException e) {
            Log.d("LoanDao", "could not load users loans", e);
        }

        return loans;
    }

    /**
     * checks a loan in
     *
     * @param loanId
     * @return
     */
    public boolean checkIn(int loanId) {
        boolean success = false;
        try {
            HttpResponse response = delete("/Loan/" + loanId);

            //check statuscode of request
            checkResponse(response);

            //if no HTTP-exception was thrown everything is ok
            success = true;
        } catch (IOException e) {
            Log.e("LoanDao", "error on DELETE request", e);
        } catch (HttpException e) {
            Log.d("LoanDao", "could not checkIn", e);
        }

        return success;
    }

    public static Loan parseFromJson(JSONObject jsonObject) throws JSONException {
        Loan loan = new Loan();

        loan.setLoanId(jsonObject.getInt("LoanId"));
        loan.setBorrower(UserDao.parseFromJson(jsonObject.getJSONObject("Borrower")));
        loan.setLoanable(LoanableDao.parseFromJson(jsonObject.getJSONObject("Loanable")));
        loan.setBorrowDate(convertDotNetDateTime(jsonObject.getString("BorrowDate")));
        loan.setRecallExpiredDate(convertDotNetDateTime(jsonObject.getString("RecallExpiredDate")));
        loan.setToBeReturnedDate(convertDotNetDateTime(jsonObject.getString("ToBeReturnedDate")));

        if(!jsonObject.isNull("ReasonForRecall")) {
            loan.setReasonForRecall(Loan.RecallReason.getType(jsonObject.getInt("ReasonForRecall")));
        } else {
            loan.setReasonForRecall(null);
        }


        return loan;
    }
}
