package leila.tabletverwaltung.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import leila.tabletverwaltung.DataTypes.Lehrer;
import leila.tabletverwaltung.R;

/**
 * Created by Lukas on 27.11.2016.
 */
public class LehrerAdapter extends BaseAdapter {
    private ArrayList<Lehrer> lehrer;
    private Context context;

    public LehrerAdapter(Context context, ArrayList<Lehrer> lehrer) {
        this.context = context;
        this.lehrer = lehrer;
    }


    @Override
    public int getCount() {
        return this.lehrer.size();
    }

    @Override
    public Lehrer getItem(int position) {
        return this.lehrer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lehrer l = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_lehrer, parent, false);
        }

        TextView tvLehrer = (TextView)convertView.findViewById(R.id.tvLehrer);
        tvLehrer.setText(l.getName()+" "+l.getVorname());
        convertView.setTag(l.getId());

        return convertView;
    }
}
