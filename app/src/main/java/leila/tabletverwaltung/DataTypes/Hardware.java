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
public class Hardware extends DataType {

    public static ArrayList<Hardware> geraeteListe;
    public static long zuletztGeaendertGeraeteListe;

    private int mId;
    private long mSeriennummer;
    private String mBeschreibung;
    private String mBemerkung;

    public Hardware(Context context) {
        super(context);
    }
    //TODO: implement hardware


    public static ArrayList<Hardware> getAll(Context baseContext){
        Long currentTs = System.currentTimeMillis() / (1000 * 1000);
        int dayInSeconds = 60 * 60 * 24;
        if(Hardware.geraeteListe == null || (Hardware.zuletztGeaendertGeraeteListe != 0 && (Hardware.zuletztGeaendertGeraeteListe - currentTs) > dayInSeconds)){
            DbConnection dbc = DbConnection.connect(baseContext);

            String query = baseContext.getResources().getString(R.string.query_Hardware_getAll);
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


    public static Hardware createFromResult(Context context, ResultSet rs){
        Hardware geraet = new Hardware(context);
        try {
            geraet.setmId(rs.getInt("har_id"));
            geraet.setmSeriennummer(rs.getLong("har_seriennummer"));
            geraet.setmBeschreibung(rs.getString("har_beschreibung"));
            geraet.setmBemerkung(rs.getString("har_bemerkung"));
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
}
