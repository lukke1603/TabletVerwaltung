package leila.tabletverwaltung.DataTypes;

import android.content.Context;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import leila.tabletverwaltung.DataConnection.DbConnection;
import leila.tabletverwaltung.R;

/**
 * Created by Lukas Brinkmann on 02.12.2016.
 */
public class Historie extends DataType {
    private int mId;
    private Hardware mHardware;
    private Lehrer mVerliehenDurch;
    private Schueler mVerliehenAn;
    private long mDatumVerleih;
    private long mDatumRueckgabe;
    private Kurs mKurs;


    private static ArrayList<Historie> eintraege;
    private static long zuletztGeaendertGeraeteListe;


    public Historie(Context context, int id, Hardware hardware, Lehrer verliehenDurch, Schueler verliehenAn, long datumVerleih, long datumRueckgabe, Kurs kurs){
        super(context);
        this.mDatumRueckgabe = datumRueckgabe;
        this.mDatumVerleih = datumVerleih;
        this.mHardware = hardware;
        this.mId = id;
        this.mKurs = kurs;
        this.mVerliehenAn = verliehenAn;
        this.mVerliehenDurch = verliehenDurch;
    }


    public static ArrayList<Historie> getAll(Context baseContext, int id, boolean update){
        Long currentTs = System.currentTimeMillis() / (1000 * 1000);
        int dayInSeconds = 60 * 60 * 24;
        if(update || Historie.eintraege == null || (Historie.zuletztGeaendertGeraeteListe != 0 && (Historie.zuletztGeaendertGeraeteListe - currentTs) > dayInSeconds)){
            DbConnection dbc = DbConnection.connect(baseContext);

            String query = baseContext.getResources().getString(R.string.query_Historie_getAllWithRef);
            query.replace("%his_hardware%", Integer.toString(id));
            ResultSet rs = dbc.Select(query);

            try {
                Historie.eintraege = new ArrayList<Historie>();
                while(rs.next()){
                    Historie.eintraege.add(Historie.createFromResult(baseContext, rs));
                }
                Historie.zuletztGeaendertGeraeteListe = System.currentTimeMillis() / (1000 * 1000);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            dbc.disconnect();
            return Historie.eintraege;
        }else{
            return Historie.eintraege;
        }
    }



    public static ArrayList<Historie> getAll(Context baseContext, boolean update){
        Long currentTs = System.currentTimeMillis() / (1000 * 1000);
        int dayInSeconds = 60 * 60 * 24;
        if(update || Historie.eintraege == null || (Historie.zuletztGeaendertGeraeteListe != 0 && (Historie.zuletztGeaendertGeraeteListe - currentTs) > dayInSeconds)){
            DbConnection dbc = DbConnection.connect(baseContext);

            String query = baseContext.getResources().getString(R.string.query_Historie_getAll);
            ResultSet rs = dbc.Select(query);

            try {
                Historie.eintraege = new ArrayList<Historie>();
                while(rs.next()){
                    Historie.eintraege.add(Historie.createFromResult(baseContext, rs));
                }
                Historie.zuletztGeaendertGeraeteListe = System.currentTimeMillis() / (1000 * 1000);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            dbc.disconnect();
            return Historie.eintraege;
        }else{
            return Historie.eintraege;
        }
    }


    public static Historie createFromResult(Context baseContext, ResultSet rs){
        Historie eintrag = null;
        try {
            Hardware hardware = Hardware.get(baseContext, rs.getInt("his_hardware"));
            Lehrer lehrer = Lehrer.get(baseContext, rs.getInt("his_verliehen_durch"));
            Schueler schueler = Schueler.get(baseContext, rs.getInt("his_verliehen_an"));
            Kurs kurs = Kurs.get(baseContext, rs.getInt("his_kurs"));

            eintrag = new Historie(
                    baseContext,
                    rs.getInt("his_id"),
                    hardware,
                    lehrer,
                    schueler,
                    rs.getLong("his_datum_verleih"),
                    rs.getLong("his_datum_rueckgabe"),
                    kurs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eintrag;
    }


    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public Hardware getmHardware() {
        return mHardware;
    }

    public void setmHardware(Hardware mHardware) {
        this.mHardware = mHardware;
    }

    public Lehrer getmVerliehenDurch() {
        return mVerliehenDurch;
    }

    public void setmVerliehenDurch(Lehrer mVerliehenDurch) {
        this.mVerliehenDurch = mVerliehenDurch;
    }

    public Schueler getmVerliehenAn() {
        return mVerliehenAn;
    }

    public void setmVerliehenAn(Schueler mVerliehenAn) {
        this.mVerliehenAn = mVerliehenAn;
    }

    public long getmDatumVerleih() {
        return mDatumVerleih;
    }

    public void setmDatumVerleih(long mDatumVerleih) {
        this.mDatumVerleih = mDatumVerleih;
    }

    public long getmDatumRueckgabe() {
        return mDatumRueckgabe;
    }

    public void setmDatumRueckgabe(long mDatumRueckgabe) {
        this.mDatumRueckgabe = mDatumRueckgabe;
    }

    public Kurs getmKurs() {
        return mKurs;
    }

    public void setmKurs(Kurs mKurs) {
        this.mKurs = mKurs;
    }
}
