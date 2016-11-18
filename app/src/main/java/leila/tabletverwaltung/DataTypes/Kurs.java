package leila.tabletverwaltung.DataTypes;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class Kurs {
    private int mKursId;
    private int mLehrerId;
    private int mFachId;
    private  String mKursName;

    public Kurs(int kursId, int lehrerId, int fachId, String kursName){
        mKursId = kursId;
        mLehrerId = lehrerId;
        mFachId = fachId;
        mKursName = kursName;
    }

    public  int getKursId(){
        return  mKursId;
    }

    public String getKursName(){
        return mKursName;
    }


}
