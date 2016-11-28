package leila.tabletverwaltung.DataTypes;

import android.content.Context;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class Kurs extends DataType {
    private int mKursId;
    private int mLehrerId;
    private int mFachId;
    private  String mKursName;

    public Kurs(Context context, int kursId, int lehrerId, int fachId, String kursName){
        super(context);
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
