package se.hj.doelibs.api;

import android.util.Base64;
import android.util.Log;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Baseclass for all Dao classes which communicate with the API. Provides basic functionality like to perform HTTP GET, POST, PUT and DELETE requests
 * @author Christoph
 */
public abstract class BaseDao<T> {

    protected final String API_BASE_URL;
    protected AbstractHttpClient httpClient;
    protected Header authorizationHeader;
    private static final SimpleDateFormat dotNetDateTimeFormat;

    static {
        dotNetDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss.SS");//2014-12-07T15:28:54.63
    }

    /**
     * returns the object with the given ID
     *
     * @param id
     * @return
     */
    public abstract T getById(int id) throws HttpException;

    /**
     * Sets up the API_BASE_URL string and the httpClient with authentification
     */
    public BaseDao(UsernamePasswordCredentials credentials) {
        API_BASE_URL = "http://doelibs-001-site1.myasp.net/api";

        httpClient = new DefaultHttpClient();

        if(credentials != null) {
            String authorizationString = "Basic " + Base64.encodeToString((credentials.getUserName() + ":" + credentials.getPassword()).getBytes(), Base64.NO_WRAP);
            authorizationHeader = new BasicHeader("Authorization", authorizationString);
        } else {
            authorizationHeader = new BasicHeader("Authorization", "");
        }
    }

    /**
     * executes a get request on the given context at the API
     *
     * @param context the context under the API where the resource can be found (ie: /author/1)
     * @return the HttpResponse object
     * @throws IOException
     */
    protected HttpResponse get(String context) throws IOException {
        if (context.charAt(0) != '/') {
            context = '/' + context;
        }

        HttpGet httpGet = new HttpGet(API_BASE_URL + context);
        httpGet.addHeader(authorizationHeader);

        return httpClient.execute(httpGet);
    }

    /**
     * executes a post request on the given context at the API
     *
     * @param context
     * @param parameters
     * @return
     * @throws IOException
     */
    protected HttpResponse post(String context, List<NameValuePair> parameters) throws IOException {
        if (context.charAt(0) != '/') {
            context = '/' + context;
        }

        HttpPost httpPost = new HttpPost(API_BASE_URL + context);
        httpPost.addHeader(authorizationHeader);
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));

        return httpClient.execute(httpPost);
    }

    /**
     * executes a put request on the given context at the API
     *
     * @param context
     * @param parameters
     * @return
     * @throws IOException
     */
    protected HttpResponse put(String context, List<NameValuePair> parameters) throws IOException {
        if (context.charAt(0) != '/') {
            context = '/' + context;
        }

        HttpPut httpPut = new HttpPut(API_BASE_URL + context);
        httpPut.addHeader(authorizationHeader);
        httpPut.setEntity(new UrlEncodedFormEntity(parameters));

        return httpClient.execute(httpPut);
    }

    /**
     * executes a delete request on the given context at the API
     *
     * @param context
     * @param parameters
     * @return
     * @throws IOException
     */
    protected HttpResponse delete(String context, List<NameValuePair> parameters) throws IOException {
        if (context.charAt(0) != '/') {
            context = '/' + context;
        }

        HttpDelete httpDelete = new HttpDelete(API_BASE_URL + context);
        httpDelete.addHeader(authorizationHeader);

        return httpClient.execute(httpDelete);
    }
    /**
     * converts the content of a HttpResponse to a string
     *
     * @param response
     * @return
     * @throws IOException
     */
    protected String getResponseAsString(HttpResponse response) throws IOException {
        InputStream inputStream = null;
        StringBuilder result = new StringBuilder();

        try {
            inputStream = response.getEntity().getContent();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);

            String line = null;
            while ((line = bReader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return result.toString();
    }

    /**
     * checks if the statuscode of the responeobject is ok. Otherwise it throws an HttpException with more details
     *
     * @param response
     * @throws HttpException
     */
    protected void checkResponse(HttpResponse response) throws HttpException{
        int status = response.getStatusLine().getStatusCode();

        if ((status >= 100 && status <= 102) || (status >= 200 && status <= 226)) {
            //OK - do nothing
        } else if (status >= 300 && status <= 308) {
            //redirect
            throw new HttpException("Service has been moved (statuscode: " + status + ")");
        } else if (status == 400) {
            throw new HttpException("Error: 400 Bad Request");
        } else if (status == 401) {
            throw new HttpException("Error: HTTP 401 Unauthorized");
        } else if (status == 403) {
            throw new HttpException("Error: HTTP 403 Forbidden");
        } else if (status == 404) {
            throw new HttpException("Error: HTTP 404 Not Found");
        } else if (status == 402 || (status >= 400 && status <= 431)) {
            //redirect
            throw new HttpException("Error: HTTP Client error (statuscode: " + status + ")");
        } else if (status == 500) {
            throw new HttpException("Error: HTTP 500 Internal Server Error");
        } else if (status == 501) {
            throw new HttpException("Error: HTTP 501 Not Implemented");
        } else if (status >= 502 && status <= 599) {
            //redirect
            throw new HttpException("Server Error (statuscode: " + status + ")");
        } else {
            throw new HttpException("Error Unknown. Statuscode: " + status + ")");
        }
    }

    /**
     * convert a .NET DateTime string (ie: 2014-12-07T15:28:54.63) into a java Date object
     *
     * @param dateTime
     * @return Date object, null if could not parses
     */
    protected static Date convertDotNetDateTime(String dateTime) {
        //ignore null values and DateTime.MinValue
        if(dateTime.equals("null") || dateTime.equals("0001-01-01T00:00:00")) {
            return null;
        }

        Date result = null;
        try {
            result = dotNetDateTimeFormat.parse(dateTime);
        } catch (ParseException e) {
            Log.d("BaseDao", "could not parse given date ("+dateTime+")", e);
        }

        return result;
    }
}
