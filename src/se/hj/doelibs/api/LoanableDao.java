package se.hj.doelibs.api;

import org.apache.http.HttpException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.json.JSONException;
import org.json.JSONObject;
import se.hj.doelibs.model.Loanable;

/**
 * @author Christoph
 */
public class LoanableDao extends BaseDao<Loanable> {

    public LoanableDao(UsernamePasswordCredentials credentials) {
        super(credentials);
    }

    @Override
    public Loanable getById(int id) throws HttpException {
        return null;
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
