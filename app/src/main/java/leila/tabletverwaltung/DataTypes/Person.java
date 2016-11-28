package leila.tabletverwaltung.DataTypes;

import android.content.Context;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public abstract class Person extends DataType {

    private int mId;
    private String mName;
    private String mVorname;

    public  Person(Context context, int id, String name, String vorname){
        super(context);
        mId = id;
        mName = name;
        mVorname = vorname;
    }


    public Person(Context context){
        super(context);
    }

    public String getName(){
        return mName;
    }

    public  String getVorname(){
        return  mVorname;
    }

    public  int getId(){
        return mId;
    }


    public void setmId(int mId) {
        this.mId = mId;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmVorname(String mVorname) {
        this.mVorname = mVorname;
    }
}
