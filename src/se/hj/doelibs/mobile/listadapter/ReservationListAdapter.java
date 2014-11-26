package se.hj.doelibs.mobile.listadapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import se.hj.doelibs.mobile.R;
import se.hj.doelibs.model.Reservation;

import java.util.List;

/**
 * Created by Alexander on 2014-11-25.
 */
public class ReservationListAdapter extends BaseAdapter {

    private Activity activity;
    private List<Reservation> reservations;

    public ReservationListAdapter( Activity activity, List<Reservation> reservations)
    {
        this.activity = activity;
        this.reservations = reservations;

    };

    @Override
    public int getCount()
    {
        return reservations.size();
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
        Button button = (Button)rowView.findViewById(R.id.btn_reservations_check_out);

        Reservation reservation = reservations.get(position);

        header.setText(reservation.getTitle().getBookTitle() + " (" + reservation.getTitle().getEditionYear() + ")");
        subcontent1.setText("Placeholder");

        //Todo Handle CheckOut Checkout button and implement a function to check if you're first in reservation queue


        return rowView;


    }




}
