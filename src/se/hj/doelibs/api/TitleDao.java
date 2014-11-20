package se.hj.doelibs.api;

import android.util.Log;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Author;
import se.hj.doelibs.model.Loanable;
import se.hj.doelibs.model.Title;
import se.hj.doelibs.model.Topic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph
 */
public class TitleDao extends BaseDao<Title> {

    public TitleDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Title getById(int titleId) throws HttpException {
        Title title = null;

        try {
            HttpResponse response = get("/Title/" + titleId);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject titleModel = new JSONObject(responseString);

            //get basic titleinformation
            title = TitleDao.parseFromJson(titleModel.getJSONObject("Title"));

            //get loanables
            JSONArray loanableArray = titleModel.getJSONArray("Loanables");
            List<Loanable> loanables = new ArrayList<Loanable>();
            for(int i = 0;i<loanableArray.length();i++) {
                Loanable loanable = LoanableDao.parseFromJson(loanableArray.getJSONObject(i));
                loanables.add(loanable);
            }

            //get authors
            JSONArray authorsArray = titleModel.getJSONArray("Authors");
            List<Author> authors = new ArrayList<Author>();
            for(int i = 0;i<authorsArray.length();i++) {
                Author author = AuthorDao.parseFromJson(authorsArray.getJSONObject(i));
                authors.add(author);
            }

            //get Editors
            JSONArray editorsArray = titleModel.getJSONArray("Editors");
            List<Author> editors = new ArrayList<Author>();
            for(int i = 0;i<editorsArray.length();i++) {
                Author editor  = AuthorDao.parseFromJson(editorsArray.getJSONObject(i));
                editors.add(editor);
            }

            //get Topics
            JSONArray topicArray = titleModel.getJSONArray("Editors");
            List<Topic> topics = new ArrayList<Topic>();
            for(int i = 0;i<topicArray.length();i++) {
                Topic topic = TopicDao.parseFromJson(topicArray.getJSONObject(i));
                topics.add(topic);
            }

            //add everything
            title.setLoanables(loanables);
            title.setAuthors(authors);
            title.setEditors(editors);
            title.setTopics(topics);

        } catch (IOException e) {
            Log.e("AuthorDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("AuthorDao", "could not parse JSON result", e);
        }

        return title;
    }

    /**
     * returns the title with the given ISBN number if it exists in DoeLibS
     *
     * @param isbn
     * @param isbnFormat the ISBN format (isbn10 or isbn13)
     * @return Title
     */
    public Title getByIsbn(String isbn, IsbnFormat isbnFormat) throws HttpException {
        Title title = null;

        try {
            String context = String.format("/Title/%s/%s", isbnFormat.getValue(), isbn);
            HttpResponse response = get(context);

            //check statuscode of request
            checkResponse(response);

            //get the result
            String responseString = getResponseAsString(response);

            //create object out of JSON result
            JSONObject titleModel = new JSONObject(responseString);

            //get basic titleinformation
            title = TitleDao.parseFromJson(titleModel.getJSONObject("Title"));
        } catch (IOException e) {
            Log.e("AuthorDao", "Exception on GET request", e);
        } catch (JSONException e) {
            Log.e("AuthorDao", "could not parse JSON result", e);
        }

        return title;
    }

    public static Title parseFromJson(JSONObject jsonObject) throws JSONException {
        Title title = new Title();

        title.setTitleId(jsonObject.getInt("TitleId"));
        title.setBookTitle(jsonObject.getString("BookTitle"));
        title.setIsbn10(jsonObject.getString("ISBN10"));
        title.setIsbn13(jsonObject.getString("ISBN13"));
        title.setEditionNumber(jsonObject.getInt("EditionNumber"));
        title.setEditionNumber(jsonObject.getInt("EditionYear"));
        if(jsonObject.has("FirstEditionYear") && !jsonObject.isNull("FirstEditionYear")) {
            title.setFirstEditionYear(jsonObject.getInt("FirstEditionYear"));
        }

        //set publisher
        title.setPublisher(PublisherDao.parseFromJson(jsonObject.getJSONObject("Publisher")));

        return title;
    }

    /**
     * specifies the format of the isbn number
     */
    public enum IsbnFormat {
        ISBN10("isbn10"),
        ISBN13("isbn13");

        private String value;
        private IsbnFormat(String value) {
            this.value = value;
        }

        /**
         * returns the isbn string representation for this ISBN version
         * @return String
         */
        public String getValue() {
            return value;
        }
    }
}
