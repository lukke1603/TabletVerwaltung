package leila.tabletverwaltung.DataTypes;

import android.content.Context;
import android.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import leila.tabletverwaltung.DataConnection.DbConnection;
import leila.tabletverwaltung.R;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class Hardware extends DataType {

    public static ArrayList<Hardware> geraeteListe;
    public static long zuletztGeaendertGeraeteListe;

    private int mId;
    private long mSeriennummer;
    private String mBeschreibung;
    private String mBemerkung;
    private Object mVerliehenAn;


    public Hardware(Context context) {
        super(context);
    }
    //TODO: implement hardware


    public static ArrayList<Hardware> getAll(Context baseContext, boolean update){
        Long currentTs = System.currentTimeMillis() / (1000 * 1000);
        int dayInSeconds = 60 * 60 * 24;
        if(update || Hardware.geraeteListe == null || (Hardware.zuletztGeaendertGeraeteListe != 0 && (Hardware.zuletztGeaendertGeraeteListe - currentTs) > dayInSeconds)){
            DbConnection dbc = DbConnection.connect(baseContext);

            String query = baseContext.getResources().getString(R.string.query_Hardware_getAllWithRef);
            ResultSet rs = dbc.Select(query);

            try {
                Hardware.geraeteListe = new ArrayList<Hardware>();
                while(rs.next()){
                    Hardware.geraeteListe.add(Hardware.createFromResult(baseContext, rs));
                }
                Hardware.zuletztGeaendertGeraeteListe = System.currentTimeMillis() / (1000 * 1000);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            dbc.disconnect();
            return Hardware.geraeteListe;
        }else{
            return Hardware.geraeteListe;
        }
    }



    public static Hardware get(Context baseContext, int id){
        DbConnection dbc = DbConnection.connect(baseContext);

        String query = baseContext.getResources().getString(R.string.query_Hardware_get).replace("%har_id%", Integer.toString(id));
        ResultSet rs = dbc.Select(query);

        Hardware geraet = null;
        try {
            if(rs.first()){
                geraet = Hardware.createFromResult(baseContext, rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbc.disconnect();
        return geraet;
    }



    public static Hardware createFromResult(Context baseContext, ResultSet rs){
        Hardware geraet = new Hardware(baseContext);
        try {
            geraet.setmId(rs.getInt("har_id"));
            geraet.setmSeriennummer(rs.getLong("har_seriennummer"));
            geraet.setmBeschreibung(rs.getString("har_beschreibung"));
            geraet.setmBemerkung(rs.getString("har_bemerkung"));
            Object verliehenAn = null;
            if(rs.getInt("sch_id") != 0){       //  Ausgeliehen an einen Schüler
                verliehenAn = Schueler.get(baseContext, rs.getInt("sch_id"));
            }else if(rs.getInt("kur_id") != 0){ //  Ausgeliehen an eine Klasse
                verliehenAn = null;
            }

            geraet.setmVerliehenAn(verliehenAn);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return geraet;
    }


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public long getmSeriennummer() {
        return mSeriennummer;
    }

    public void setmSeriennummer(long mSeriennummer) {
        this.mSeriennummer = mSeriennummer;
    }

    public String getmBeschreibung() {
        return mBeschreibung;
    }

    public void setmBeschreibung(String mBeschreibung) {
        this.mBeschreibung = mBeschreibung;
    }

    public String getmBemerkung() {
        return mBemerkung;
    }

    public void setmBemerkung(String mBemerkung) {
        this.mBemerkung = mBemerkung;
    }

    public Object getmVerliehenAn() {
        return mVerliehenAn;
    }

    public void setmVerliehenAn(Object mVerliehenAn) {
        this.mVerliehenAn = mVerliehenAn;
    }
}
