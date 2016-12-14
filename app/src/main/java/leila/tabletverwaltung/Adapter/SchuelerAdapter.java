package leila.tabletverwaltung.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import leila.tabletverwaltung.DataTypes.Lehrer;
import leila.tabletverwaltung.DataTypes.Schueler;
import leila.tabletverwaltung.R;

/**
 * Created by Lukas Brinkmann on 14.12.2016.
 */
public class SchuelerAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Schueler> schueler;


    public SchuelerAdapter(Context context, ArrayList<Schueler> schueler){
        this.context = context;
        this.schueler = schueler;
    }


    @Override
    public int getCount() {
        return schueler.size();
    }

    @Override
    public Schueler getItem(int position) {
        return schueler.get(position);
    }

    @Override
    public long getItemId(int position) {
        return schueler.get(position).getId();
    }



    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Schueler s = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_schueler_list, parent, false);
        }

        TextView tvSchueler = (TextView)convertView.findViewById(R.id.tvSchueler);
        tvSchueler.setText(s.getName()+" "+s.getVorname());
        convertView.setTag(s.getId());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Schueler s = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_schueler, parent, false);
        }

        TextView tvSchueler = (TextView)convertView.findViewById(R.id.tvSchueler);
        tvSchueler.setText(s.getName()+" "+s.getVorname());
        convertView.setTag(s.getId());

        return convertView;
    }
}
