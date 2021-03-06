package se.hj.doelibs.mobile.listadapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.model.Loanable;
import se.hj.doelibs.model.Reservation;

import java.util.List;

/**
 * Created by Alexander on 2014-11-25.
 */
public class ReservationListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Reservation> reservations;
    private Typeface novaLight;
    private Typeface novaRegItalic;

    public ReservationListAdapter( Activity activity, List<Reservation> reservations)
    {
        this.activity = activity;
        this.reservations = reservations;
        this.novaLight = Typeface.createFromAsset(activity.getAssets(), "fonts/Proxima Nova Alt Condensed Light.otf");
        this.novaRegItalic = Typeface.createFromAsset(activity.getAssets(), "fonts/Proxima Nova Alt Condensed Regular Italic.otf");

    };

    @Override
    public int getCount()
    {
        return (reservations != null) ? reservations.size() : 0;
    }

    @Override
    public Object getItem(int position)
    {
        return reservations.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return reservations.get(position).getReservationId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.reservation_list_view_item,null,true);

        TextView header = (TextView)rowView.findViewById(R.id.tv_reservations_header);
        TextView subcontent1 = (TextView)rowView.findViewById(R.id.tv_reservations_info);

        header.setTypeface(novaLight);
        subcontent1.setTypeface(novaRegItalic);

        Reservation reservation = reservations.get(position);

        header.setText(reservation.getTitle().getBookTitle() + " (" + reservation.getTitle().getEditionYear() + ")");
        if(reservation.isLoanRecalled() && reservation.getAvailableDate() != null) {
            subcontent1.setText(activity.getText(R.string.prefix_status) + ": " + Loanable.Status.AVAILABLE.getText(activity));
        } else {
            subcontent1.setText(activity.getText(R.string.prefix_status) + ": " + activity.getText(R.string.reservation_waiting_for_loanable));
        }

        return rowView;
    }
}
