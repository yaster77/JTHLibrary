/**
 * 
 */
package se.hj.doelibs.mobile.listadapter;

import java.util.HashMap;
import java.util.List;

import android.graphics.Typeface;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.mobile.R.id;
import se.hj.doelibs.mobile.R.layout;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Christoph
 *
 */
public class NavigationListAdapter extends ArrayAdapter<String> {

	private final Activity context;
	private String[] web;
	private Integer[] imageId;
	private Typeface novaLight;
	
	public NavigationListAdapter(Activity pContext) {
		super(pContext, R.layout.navigation_drawer_list_view_item, pContext.getResources().getStringArray(R.array.nav_drawer_items));
		this.context = pContext;
		this.novaLight = Typeface.createFromAsset(getContext().getAssets(), "fonts/Proxima Nova Alt Condensed Light.otf");

		this.web = pContext.getResources().getStringArray(R.array.nav_drawer_items);
		this.imageId = new Integer[] {
			R.drawable.ic_launcher,
			R.drawable.ic_titles,
			R.drawable.ic_settings
		};
	}
	
	

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.navigation_drawer_list_view_item, null, true);
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.navigation_text);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.navigation_img);

		txtTitle.setTypeface(novaLight);
		
		txtTitle.setText(web[position]);
		imageView.setImageResource(imageId[position]);
		
		return rowView;
	}

}
