package leila.tabletverwaltung.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import leila.tabletverwaltung.DataTypes.Historie;
import leila.tabletverwaltung.DataTypes.Kurs;
import leila.tabletverwaltung.DataTypes.Schueler;
import leila.tabletverwaltung.R;

/**
 * Created by Lukas Brinkmann on 02.12.2016.
 */
public class HistorieAdapter extends BaseAdapter {

    private ArrayList<Historie> eintraege;
    private Context context;

    public HistorieAdapter(Context context, ArrayList<Historie> eintraege){
        this.eintraege = eintraege;
        this.context = context;
    }


    @Override
    public int getCount() {
        return eintraege.size();
    }

    @Override
    public Historie getItem(int position) {
        return eintraege.get(position);
    }

    @Override
    public long getItemId(int position) {
        return eintraege.get(position).getmId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Historie historie = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_historie,parent, false);
        }

        TextView tvBis = (TextView)convertView.findViewById(R.id.tvBis);
        TextView tvVerliehenAn = (TextView)convertView.findViewById(R.id.tvVerliehenAn);
        TextView tvVerliehenDurch = (TextView)convertView.findViewById(R.id.tvVerliehenDurch);
        TextView tvDatumRueckgabe = (TextView)convertView.findViewById(R.id.tvDatumRueckgabe);
        TextView tvDatumVerleih = (TextView)convertView.findViewById(R.id.tvDatumVerleih);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date dVerleih = null;
        Date dRueckgabe = null;
        try {
            if(historie.getmDatumVerleih() != null) dVerleih = fmt.parse(historie.getmDatumVerleih().toString());
            if(historie.getmDatumRueckgabe() != null) dRueckgabe = fmt.parse(historie.getmDatumRueckgabe().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvVerliehenDurch.setText(context.getResources().getString(R.string.tvVerliehenDurch_prefix) + " " + historie.getmVerliehenDurch().getVorname() + " " +  historie.getmVerliehenDurch().getName());
        tvDatumVerleih.setText((dVerleih != null) ? format.format(dVerleih) + " " + context.getResources().getText(R.string.tvUhr) : "");
        tvDatumRueckgabe.setText((dRueckgabe != null) ? format.format(dRueckgabe) + " " + context.getResources().getText(R.string.tvUhr) : "");

        String verliehenAn = context.getResources().getText(R.string.geraete_verfuegbar).toString();
        if(historie.getmVerliehenAn() != null){
            verliehenAn = context.getResources().getText(R.string.tvVerliehen_prefix) + " " + ((Schueler) historie.getmVerliehenAn()).getVorname() + " " + ((Schueler) historie.getmVerliehenAn()).getName();
        }else if(historie.getmKurs() != null){
            verliehenAn = context.getResources().getText(R.string.tvVerliehen_prefix) + " " + ((Kurs) historie.getmKurs()).getKursName();
        }

        tvVerliehenAn.setText(verliehenAn);

        return convertView;
    }
}
