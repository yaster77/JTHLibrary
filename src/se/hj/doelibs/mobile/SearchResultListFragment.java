package se.hj.doelibs.mobile;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import se.hj.doelibs.mobile.listener.OnTitleItemSelectedListener;
import se.hj.doelibs.model.Title;

import java.lang.reflect.Type;

/**
 * Fragment to show the search results in a list
 *
 * @author Elias
 * @author Christoph
 */
public class SearchResultListFragment extends Fragment {
	private OnTitleItemSelectedListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_search_result_list, container, false);

		final ListView _list = (ListView)view.findViewById(R.id.searchResultList);

		_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Title clicked = (Title)_list.getItemAtPosition(position);
				listener.onTitleItemSelected(clicked.getTitleId());
			}
		});

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnTitleItemSelectedListener) {
			listener = (OnTitleItemSelectedListener) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement OnTitleItemSelectedListener");
		}
	}
}