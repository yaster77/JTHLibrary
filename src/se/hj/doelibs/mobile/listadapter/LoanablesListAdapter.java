package se.hj.doelibs.mobile.listadapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.mobile.asynctask.TaskCallback;
import se.hj.doelibs.mobile.listener.LoanableCheckInOnClickListener;
import se.hj.doelibs.mobile.listener.LoanableCheckOutOnClickListener;
import se.hj.doelibs.mobile.utils.CurrentUserUtils;
import se.hj.doelibs.model.Loan;
import se.hj.doelibs.model.Loanable;

import java.util.List;

/**
 * @author Christoph
 */
public class LoanablesListAdapter extends BaseAdapter {

	private Activity activity;
	private List<Loanable> loanables;
	private TaskCallback<Loan> checkOutCallback;
	private TaskCallback<Boolean> checkInCallback;

	public LoanablesListAdapter(Activity activity, List<Loanable> loanables, TaskCallback<Loan> checkOutCallback, TaskCallback<Boolean> checkInCallback) {
		this.activity = activity;
		this.loanables = loanables;
		this.checkOutCallback = checkOutCallback;
		this.checkInCallback = checkInCallback;
	}

	@Override
	public int getCount() {
		return loanables.size();
	}

	@Override
	public Object getItem(int position) {
		return loanables.get(position);
	}

	@Override
	public long getItemId(int position) {
		return loanables.get(position).getLoanableId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.title_detail_loanable_listview_item, null, true);

		TextView header = (TextView) rowView.findViewById(R.id.tv_titledetails_loanable_header);
		TextView subcontent1 = (TextView) rowView.findViewById(R.id.tv_titledetails_loanable_subcontent1);
		TextView subcontent2 = (TextView) rowView.findViewById(R.id.tv_titledetails_loanable_subcontent2);
		Button button = (Button)rowView.findViewById(R.id.btn_titledetails_loanable_checkInOrOut);


		Typeface novaLight = Typeface.createFromAsset(rowView.getResources().getAssets(), "fonts/Proxima Nova Alt Condensed Light.otf");
		header.setTypeface(novaLight);
		subcontent1.setTypeface(novaLight);
		subcontent2.setTypeface(novaLight);
		button.setTypeface(novaLight);

		Loanable loanable = loanables.get(position);

		header.setText(loanable.getLocation() + " (" + loanable.getCategory().getName() + ")");
		subcontent1.setText(loanable.getOwner().toString());
		subcontent2.setText(activity.getResources().getText(R.string.prefix_status) + ": " + loanable.getStatus().getText(activity));

		//show button only logged in users
		if(CurrentUserUtils.getCredentials(activity) != null) {
			if(loanable.getStatus() == Loanable.Status.AVAILABLE || loanable.getStatus() == Loanable.Status.RESERVED) {
				button.setText(R.string.btn_check_out);
				button.setOnClickListener(new LoanableCheckOutOnClickListener(loanable.getTitle().getTitleId(), loanable.getLoanableId(), activity, checkOutCallback));
			} else {
				//these loanables are already filtered --> only AVAILABLE, RESERVED, BORROWED, RECALLED loanables
				button.setText(R.string.btn_check_in);
				button.setOnClickListener(new LoanableCheckInOnClickListener(loanable.getTitle().getTitleId(), loanable.getLoanableId(), activity, checkInCallback));
			}
		} else {
			button.setVisibility(View.INVISIBLE);
		}

		return rowView;
	}
}
