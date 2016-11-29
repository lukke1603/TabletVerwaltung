package leila.tabletverwaltung.DataTypes;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import leila.tabletverwaltung.DataConnection.DbConnection_alt;
import leila.tabletverwaltung.R;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class Lehrer extends Person {
    public Lehrer(Context context, int id, String name, String vorname) {
        super(context, id, name, vorname);
    }

    public Lehrer(Context context){
        super(context);
    }


    public static ArrayList<Lehrer> getAll(Context context){
        DbConnection_alt db = DbConnection_alt.CreateInstance(context);


        ArrayList<Lehrer> lehrer = new ArrayList<Lehrer>();
        String query = context.getResources().getString(R.string.query_Lehrer_getAll);
        ResultSet rs = db.Select(query);

        try {
            while(rs.next()){
                lehrer.add(Lehrer.createFromResult(context, rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lehrer;
    }


    public static Lehrer createFromResult(Context context, ResultSet rs){
        Lehrer lehrer = new Lehrer(context);
        try {
            lehrer.setmId(rs.getInt("leh_id"));
            lehrer.setmName(rs.getString("leh_name"));
            lehrer.setmVorname(rs.getString("leh_vorname"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lehrer;
    }




}
