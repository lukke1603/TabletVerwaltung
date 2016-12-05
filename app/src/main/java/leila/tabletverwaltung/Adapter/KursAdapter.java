package leila.tabletverwaltung.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import leila.tabletverwaltung.DataTypes.Kurs;
import leila.tabletverwaltung.R;

/**
 * Created by lukas.brinkmann on 05.12.2016.
 */
public class KursAdapter extends BaseAdapter {

    private ArrayList<Kurs> kurse;
    private Context context;

    public KursAdapter(Context context, ArrayList<Kurs> kurse){
        this.context = context;
        this.kurse = kurse;
    }


    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Kurs kurs = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_kurs_list, parent, false);
        }

        TextView tvKurs = (TextView)convertView.findViewById(R.id.tvKurs);
        tvKurs.setText(kurs.getKursName());
        convertView.setTag(kurs.getKursId());

        return convertView;
    }



    @Override
    public int getCount() {
        return kurse.size();
    }

    @Override
    public Kurs getItem(int position) {
        return kurse.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Kurs kurs = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_kurs, parent, false);
        }

        TextView tvKurs = (TextView)convertView.findViewById(R.id.tvKurs);
        tvKurs.setText(kurs.getKursName());
        convertView.setTag(kurs.getKursId());

        return convertView;
    }
}
