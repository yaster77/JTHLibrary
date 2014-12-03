package se.hj.doelibs.mobile.listadapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.model.Title;

import java.util.List;

/**
 * Listadapter to show search results
 *
 * @author Christoph
 */
public class SearchResultListAdapter extends ArrayAdapter<Title> {

	private List<Title> titles;
	private int resource;
	private Activity activity;

	public SearchResultListAdapter(Activity activity, int resource, List<Title> titles) {
		super(activity, resource, titles);

		this.activity = activity;
		this.resource = resource;
		this.titles = titles;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = activity.getLayoutInflater().inflate(R.layout.search_result_list_item, null);
		}
		Title title = getItem(position);

		TextView text = (TextView) view.findViewById(R.id.search_result_list_item_text);
		text.setText(title.getBookTitle());

		if (position % 2 == 1) {
			view.setBackgroundResource(R.drawable.list_item1);
		} else {
			view.setBackgroundResource(R.drawable.list_item2);
		}

		return view;
	}

}
