package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Author;

import java.io.IOException;

/**
 * Class for CRUD operations to the DoeLibS API as far as it supports it
 *
 * @author Christoph
 */
public class AuthorDao extends BaseDao<Author>{

    public AuthorDao() {
        super();
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

            author = new Author();
            author.setAuthorId(authorObject.getInt("Aid"));
            author.setName(authorObject.getString("Name"));

        } catch (IOException e) {
            Log.e("AuthorDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("AuthorDao", "could not parse JSON result", e);
        }

        return author;
    }

}
