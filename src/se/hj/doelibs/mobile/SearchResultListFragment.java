package se.hj.doelibs.mobile;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christoph
 */
public class SearchResultListFragment extends Fragment {
	private OnTitleItemSelectedListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_result_list, container, false);

		final ListView _list = (ListView)view.findViewById(R.id.searchResultList);
		final List<SearchResultItem> _data = new ArrayList<SearchResultItem>();

		SearchResultItem item1 = new SearchResultItem();
		SearchResultItem item2 = new SearchResultItem();

		item1.title = "title1";
		item1.titleId = 39;
		item2.title = "title2";
		item2.titleId = 40;

		_data.add(item1);
		_data.add(item2);
		_list.setAdapter(new ArrayAdapter<SearchResultItem>(view.getContext(), android.R.layout.simple_list_item_1, _data));
		_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SearchResultItem clicked = (SearchResultItem)_list.getItemAtPosition(position);
				listener.onTitleItemSelected(clicked.titleId);
			}
		});

		return view;
	}

	public interface OnTitleItemSelectedListener {
		public void onTitleItemSelected(int titleId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnTitleItemSelectedListener) {
			listener = (OnTitleItemSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implemenet MyListFragment.OnItemSelectedListener");
		}
	}
}