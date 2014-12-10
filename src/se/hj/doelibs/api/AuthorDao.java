package se.hj.doelibs.api;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;

import se.hj.doelibs.model.Author;
import android.util.Log;

/**
 * Class for CRUD operations to the DoeLibS API as far as it supports it
 *
 * @author Christoph
 */
public class AuthorDao extends BaseDao<Author>{

    public AuthorDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Author getById(int authorId) throws HttpException {
        Author author = null;
        try {
            HttpResponse response = get("/Author/" + authorId);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);
            
            

            //create object out of JSON result
            JSONObject result = new JSONObject(responseString);
            JSONObject authorObject = result.getJSONObject("Author");

            author = AuthorDao.parseFromJson(authorObject);
        } catch (IOException e) {
            Log.e("AuthorDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("AuthorDao", "could not parse JSON result", e);
        }

        return author;
    }

    public static Author parseFromJson(JSONObject jsonObject) throws JSONException {
        Author author = new Author();

        author.setAuthorId(jsonObject.getInt("Aid"));
        author.setName(jsonObject.getString("Name"));

        return author;
    }

}
