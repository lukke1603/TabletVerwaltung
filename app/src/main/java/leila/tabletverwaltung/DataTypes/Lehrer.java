package leila.tabletverwaltung.DataTypes;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import leila.tabletverwaltung.DataConnection.DbConnection;
import leila.tabletverwaltung.R;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class Lehrer extends Person {

    public static ArrayList<Lehrer> lehrerListe;
    public static Long zuletztGeaendertLehrerListe;

    public Lehrer(Context context, int id, String name, String vorname) {
        super(context, id, name, vorname);
    }

    public Lehrer(Context context){
        super(context);
    }


    public static ArrayList<Lehrer> getAll(Context baseContext){
        Long currentTs = System.currentTimeMillis() / (1000 * 1000);
        int dayInSeconds = 60 * 60 * 24;
        if(Lehrer.lehrerListe == null || (Lehrer.zuletztGeaendertLehrerListe != null && (Lehrer.zuletztGeaendertLehrerListe - currentTs) > dayInSeconds)){
            DbConnection dbc = DbConnection.connect(baseContext);

            String query = baseContext.getResources().getString(R.string.query_Lehrer_getAll);
            ResultSet rs = dbc.Select(query);

            try {
                Lehrer.lehrerListe = new ArrayList<>();
                while(rs.next()){
                    Lehrer.lehrerListe.add(Lehrer.createFromResult(baseContext, rs));
                }
                Lehrer.zuletztGeaendertLehrerListe = System.currentTimeMillis() / (1000 * 1000);
            } catch (SQLException e) {
                e.printStackTrace();
            }

//            dbc.disconnect();
            return Lehrer.lehrerListe;
        }else{
            return Lehrer.lehrerListe;
        }
    }



    public static Lehrer get(Context baseContext, int id){
        DbConnection dbc = DbConnection.connect(baseContext);

        String query = baseContext.getResources().getString(R.string.query_Lehrer_get).replace("%leh_id%", Integer.toString(id));
        ResultSet rs = dbc.Select(query);

        Lehrer lehrer = null;
        try {
            if(rs.first()){
                lehrer = Lehrer.createFromResult(baseContext, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        dbc.disconnect();
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
