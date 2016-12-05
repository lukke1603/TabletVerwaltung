package leila.tabletverwaltung.DataTypes;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;

import leila.tabletverwaltung.DataConnection.DbConnection;
import leila.tabletverwaltung.R;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class Schueler extends Person {

    private int mKursId;
    private int mId;
    private String name;
    private String vorname;

    public Schueler(Context context, int id, String name, String vorname, int kursId) {
        super(context, id, name, vorname);
        mKursId = kursId;
    }

    public  int getKursId(){
        return  mKursId;
    }

    public static Schueler get(Context baseContext, int id){
        DbConnection dbc = DbConnection.connect(baseContext);

        String query = baseContext.getResources().getString(R.string.query_Schueler_get).replace("%sch_id%", Integer.toString(id));
        ResultSet rs = dbc.Select(query);

        Schueler schueler = null;
        try {
            if(rs.first()){
                schueler = Schueler.createFromResult(baseContext, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        dbc.disconnect();
        return schueler;
    }

    private static Schueler createFromResult(Context baseContext, ResultSet rs) {
        Schueler schueler = null;
        try {
            schueler = new Schueler(
                    baseContext,
                    rs.getInt("sch_id"),
                    rs.getString("sch_name"),
                    rs.getString("sch_vorname"),
                    rs.getInt("sch_kla_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schueler;
    }
}
