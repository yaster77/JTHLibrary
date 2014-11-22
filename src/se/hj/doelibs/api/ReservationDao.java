package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Reservation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph
 */
public class ReservationDao extends BaseDao<Reservation> {

    public ReservationDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Reservation getById(int id) throws HttpException {

        Reservation reservation = null;
        try {
            HttpResponse response = get("/Reservation/" + id);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject result = new JSONObject(responseString);

            reservation = ReservationDao.parseFromJson(result);
        } catch (IOException e) {
            Log.e("LoanDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("LoanDao", "could not parse JSON result", e);
        }

        return reservation;
    }

    public List<Reservation> getCurrentUsersReservations() {
        List<Reservation> reservations = null;
        try {
            HttpResponse response = get("/Reservation/");

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONArray result = new JSONArray(responseString);
            reservations = new ArrayList<Reservation>();
            for(int i = 0; i<result.length();i++) {
                reservations.add(ReservationDao.parseFromJson(result.getJSONObject(i)));
            }
        } catch (IOException e) {
            Log.e("LoanDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("LoanDao", "could not parse JSON result", e);
        } catch (HttpException e) {
            Log.e("LoanDao", "could not load users loans", e);
        }

        return reservations;
    }

    /**
     * returns if the user has a available reservation for the title
     *
     * @param titleId
     * @return
     */
    public boolean userHasAvailableReservationForTitle(int titleId) {
        boolean hasReservation = false;
        try {
            HttpResponse response = get("/Reservation?availableReservation&titleId=" + titleId);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject result = new JSONObject(responseString);
            if (result.length() > 0) {
                Reservation reservation = ReservationDao.parseFromJson(result);

                if(reservation != null) {
                    hasReservation = true;
                }
            } else {
                //it was empty --> no reservation
            }
        } catch (IOException e) {
            Log.d("ReservationDao", "check if user has available reservation", e);
        } catch (HttpException e) {
            Log.d("ReservationDao", "check if user has available reservation", e);
        } catch (JSONException e) {
            Log.d("ReservationDao", "check if user has available reservation", e);
        }

        return hasReservation;
    }

    /**
     * reserves a title
     *
     * @param titleId
     * @return
     */
    public boolean reserve(int titleId) {
        boolean reservationSuccessfull = false;

        try {
            HttpResponse response = post("/Reservation?titleId=" + titleId, null);

            //check statuscode of request
            checkResponse(response);

            reservationSuccessfull = true;
        } catch (IOException e) {
            Log.d("ReservationDao", "error on POST request", e);
        } catch (HttpException e) {
            Log.d("ReservationDao", "http exception", e);
        }

        return reservationSuccessfull;
    }

    public static Reservation  parseFromJson(JSONObject jsonObject) throws JSONException {
        Reservation reservation = new Reservation();

        reservation.setReservationId(jsonObject.getInt("ReservationId"));
        reservation.setAvailableDate(convertDotNetDateTime(jsonObject.getString("AvailableDate")));
        reservation.setReserveDate(convertDotNetDateTime(jsonObject.getString("ReserveDate")));
        reservation.setLoanRecalled(jsonObject.getBoolean("LoanRecalled"));
        reservation.setTitle(TitleDao.parseFromJson(jsonObject.getJSONObject("Title")));
        reservation.setUser(UserDao.parseFromJson(jsonObject.getJSONObject("User")));

        return reservation;
    }
}
