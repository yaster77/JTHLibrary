package se.hj.doelibs.mobile.listadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.mobile.listener.LoanableCheckInOnClickListener;
import se.hj.doelibs.model.Loan;

import java.util.List;

/**
 * Created by Alexander on 2014-11-25.
 */
public class LoanListAdapter extends BaseAdapter{


    private Activity activity;
    private List<Loan> loans;
    private TaskCallback<Boolean> checkingCallback;//

    public LoanListAdapter( Activity activity, List<Loan> loans,TaskCallback<Boolean> callback)
    {
        this.activity = activity;
        this.loans = loans;
        this.checkingCallback = callback;//

    }

    @Override
    public int getCount() {
        return loans.size();
    }

    @Override
    public Object getItem(int position)
    {
        return loans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return loans.get(position).getLoanId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.loans_list_view_item, null,true);

        TextView header = (TextView) rowView.findViewById(R.id.tv_loans_header);
        TextView subcontent1 = (TextView) rowView.findViewById(R.id.tv_loans_subcontent1);
        TextView subcontent2 = (TextView) rowView.findViewById(R.id.tv_loans_subcontent2);
        Button button = (Button)rowView.findViewById(R.id.btn_loans_checkIn);

        Loan loan = loans.get(position);

        String timeGMT = loan.getToBeReturnedDate().toGMTString().substring(0, loan.getToBeReturnedDate().toGMTString().length() -12 );
        String LocationAndCategory = loan.getLoanable().getLocation()+" ("+loan.getLoanable().getCategory().getName()+") ";

        header.setText(loan.getLoanable().getTitle().getBookTitle() +" ("+ loan.getLoanable().getTitle().getEditionYear()+") ");
        subcontent1.setText(": " + LocationAndCategory);
        subcontent2.setText(": "+ timeGMT);

        //Todo Handle button
        button.setOnClickListener(new LoanableCheckInOnClickListener(loan.getLoanable().getTitle().getTitleId(), loan.getLoanable().getLoanableId(), activity, checkingCallback));

        return rowView;

    }

}
