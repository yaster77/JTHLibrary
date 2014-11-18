package se.hj.doelibs.api;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Baseclass for all Dao classes which communicates with the API. Provides basic functionality like to perform GET requests
 * @author Christoph
 */
public abstract class BaseDao<T> {

    protected final String API_BASE_URL;
    protected AbstractHttpClient httpClient;


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
    public BaseDao() {
        API_BASE_URL = "http://doelibs-001-site1.myasp.net/api";

        httpClient = new DefaultHttpClient();

        Credentials creds = new UsernamePasswordCredentials("user", "passsword");
        AuthScope authScope = new AuthScope(API_BASE_URL, 80);
        httpClient.getCredentialsProvider().setCredentials(authScope, creds);
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
        HttpResponse response = httpClient.execute(httpGet);

        return response;
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
}
