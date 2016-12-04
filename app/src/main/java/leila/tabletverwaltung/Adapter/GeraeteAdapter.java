package leila.tabletverwaltung.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

import leila.tabletverwaltung.DataTypes.Hardware;
import leila.tabletverwaltung.DataTypes.Kurs;
import leila.tabletverwaltung.DataTypes.Schueler;
import leila.tabletverwaltung.R;

/**
 * Created by Lukas on 01.12.2016.
 */
public class GeraeteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Hardware> geraete;

    public GeraeteAdapter(Context context, ArrayList<Hardware> geraete){
        this.context = context;
        this.geraete = geraete;
    }

    @Override
    public int getCount() {
        return geraete.size();
    }

    @Override
    public Hardware getItem(int position) {
        return geraete.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Hardware geraet = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_geraete,parent,false);
        }

        TextView tvVerliehen = (TextView)convertView.findViewById(R.id.tvVerliehen);
        TextView tvBeschreibung = (TextView)convertView.findViewById(R.id.tvBeschreibung);
        TextView tvSeriennummer = (TextView)convertView.findViewById(R.id.tvSeriennummer);

        tvSeriennummer.setText(context.getResources().getText(R.string.tvSeriennummer_prefix) + " " + geraet.getmSeriennummer());
        tvBeschreibung.setText(geraet.getmBeschreibung());

        String verliehenAn = context.getResources().getText(R.string.geraete_verfuegbar).toString();
        if(geraet.getmVerliehenAn() instanceof Schueler){
            verliehenAn = context.getResources().getText(R.string.tvVerliehen_prefix) + " " + ((Schueler) geraet.getmVerliehenAn()).getVorname() + " " + ((Schueler) geraet.getmVerliehenAn()).getName();
        }else if(geraet.getmVerliehenAn() instanceof Kurs){
            verliehenAn = context.getResources().getText(R.string.tvVerliehen_prefix) + " " + ((Kurs) geraet.getmVerliehenAn()).getKursName();
        }

        tvVerliehen.setText(verliehenAn);
        convertView.setTag(geraet.getmId());

        return convertView;
    }
}
