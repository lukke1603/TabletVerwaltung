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
public class Kurs extends DataType {
    private int mKursId;
    private  String mKursName;

    public static ArrayList<Kurs> kursListe = null;
    public static Long zuletztGeaendertKursListe;

    public Kurs(Context context, int kursId, String kursName){
        super(context);
        mKursId = kursId;
        mKursName = kursName;
    }



    public static ArrayList<Kurs> getAll(Context baseContext){
        Long currentTs = System.currentTimeMillis() / (1000 * 1000);
        int dayInSeconds = 60 * 60 * 24;
        if(Kurs.kursListe == null || (Kurs.zuletztGeaendertKursListe != null && (Kurs.zuletztGeaendertKursListe - currentTs) > dayInSeconds)){
            DbConnection dbc = DbConnection.connect(baseContext);

            String query = baseContext.getResources().getString(R.string.query_Kurs_getAll);
            ResultSet rs = dbc.Select(query);

            try {
                Kurs.kursListe = new ArrayList<Kurs>();
                while(rs.next()){
                    Kurs.kursListe.add(Kurs.createFromResult(baseContext, rs));
                }
                Kurs.zuletztGeaendertKursListe = System.currentTimeMillis() / (1000 * 1000);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            dbc.disconnect();
            return Kurs.kursListe;
        }else{
            return Kurs.kursListe;
        }
    }


    public static Kurs get(Context baseContext, int id){
        DbConnection dbc = DbConnection.connect(baseContext);

        String query = baseContext.getResources().getString(R.string.query_Kurs_get).replace("%kur_id%", Integer.toString(id));
        ResultSet rs = dbc.Select(query);

        Kurs kurs = null;
        try {
            if(rs.first()){
                kurs = Kurs.createFromResult(baseContext, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbc.disconnect();
        return kurs;
    }

    private static Kurs createFromResult(Context baseContext, ResultSet rs) {
        Kurs kurs = null;
        try {
            kurs = new Kurs(
                    baseContext,
                    rs.getInt("kur_id"),
                    rs.getString("kur_name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kurs;
    }


    public  int getKursId(){
        return  mKursId;
    }

    public String getKursName(){
        return mKursName;
    }


}
