package leila.tabletverwaltung.DataTypes;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private Timestamp mDatumVerleih;
    private Timestamp mDatumRueckgabe;
    private Kurs mKurs;


    private static ArrayList<Historie> eintraege;
    private static long zuletztGeaendertGeraeteListe;


    public Historie(Context context, int id, Hardware hardware, Lehrer verliehenDurch, Schueler verliehenAn, Timestamp datumVerleih, Timestamp datumRueckgabe, Kurs kurs){
        super(context);
        this.mDatumRueckgabe = datumRueckgabe;
        this.mDatumVerleih = datumVerleih;
        this.mHardware = hardware;
        this.mId = id;
        this.mKurs = kurs;
        this.mVerliehenAn = verliehenAn;
        this.mVerliehenDurch = verliehenDurch;
    }


    public static ArrayList<Historie> getAll(Context baseContext, int hardwareId, boolean update){
        Long currentTs = System.currentTimeMillis() / (1000 * 1000);
        int dayInSeconds = 60 * 60 * 24;
        if(update || Historie.eintraege == null || (Historie.zuletztGeaendertGeraeteListe != 0 && (Historie.zuletztGeaendertGeraeteListe - currentTs) > dayInSeconds)){
            DbConnection dbc = DbConnection.connect(baseContext);

            String query = baseContext.getResources().getString(R.string.query_Historie_getAllWithRef);
            query = query.replace("%his_hardware%", Integer.toString(hardwareId));
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

//            dbc.disconnect();
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

//            dbc.disconnect();
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


            Log.i("TS", rs.getString("his_datum_verleih"));


            Timestamp ts_verleih = rs.getTimestamp("his_datum_verleih");
            Timestamp ts_rueckgabe = rs.getTimestamp("his_datum_rueckgabe");

            eintrag = new Historie(
                    baseContext,
                    rs.getInt("his_id"),
                    hardware,
                    lehrer,
                    schueler,
                    ts_verleih,
                    ts_rueckgabe,
                    kurs);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return eintrag;
    }


    public static void geraetZurueckgeben(Context baseContext, int hardware){
        DbConnection dbc = DbConnection.connect(baseContext);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date datumRueckgabe = new Date();
        String queryUpdate = baseContext.getResources().getString(R.string.query_Historie_rueckgabe);
        queryUpdate = queryUpdate.replace("%his_datum_rueckgabe%", format.format(datumRueckgabe))
                .replace("%his_hardware%", Integer.toString(hardware));
        Log.i("QUERY", queryUpdate);
        dbc.Update(queryUpdate);
    }


    public static void setVerliehenAnSchueler(Context baseContext, int schueler, int lehrer, int hardware) {
        geraetZurueckgeben(baseContext, hardware);

        DbConnection dbc = DbConnection.connect(baseContext);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date datumVerleih = new Date();
        String queryInsert = baseContext.getResources().getString(R.string.query_Historie_setVerliehenAnSchueler);
        queryInsert = queryInsert.replace("%his_hardware%", Integer.toString(hardware))
                .replace("%his_verliehen_an%", Integer.toString(schueler))
                .replace("%his_verliehen_durch%", Integer.toString(lehrer))
                .replace("%his_datum_verleih%", format.format(datumVerleih));

        dbc.Insert(queryInsert);
    }


    public static void setVerliehenAnKurs(Context baseContext, int kurs, int lehrer, int hardware){
        geraetZurueckgeben(baseContext, hardware);

        DbConnection dbc = DbConnection.connect(baseContext);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date datumVerleih = new Date();
        String queryInsert = baseContext.getResources().getString(R.string.query_Historie_setVerliehenAnKurs);
        queryInsert = queryInsert.replace("%his_hardware%", Integer.toString(hardware))
                .replace("%his_kurs%", Integer.toString(kurs))
                .replace("%his_verliehen_durch%", Integer.toString(lehrer))
                .replace("%his_datum_verleih%", format.format(datumVerleih));

        dbc.Insert(queryInsert);
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

    public Timestamp getmDatumVerleih() {
        return mDatumVerleih;
    }

    public void setmDatumVerleih(Timestamp mDatumVerleih) {
        this.mDatumVerleih = mDatumVerleih;
    }

    public Timestamp getmDatumRueckgabe() {
        return mDatumRueckgabe;
    }

    public void setmDatumRueckgabe(Timestamp mDatumRueckgabe) {
        this.mDatumRueckgabe = mDatumRueckgabe;
    }

    public Kurs getmKurs() {
        return mKurs;
    }

    public void setmKurs(Kurs mKurs) {
        this.mKurs = mKurs;
    }


}
