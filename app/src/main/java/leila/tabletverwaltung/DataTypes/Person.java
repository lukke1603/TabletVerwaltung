package leila.tabletverwaltung.DataTypes;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public abstract class Person {

    private int mId;
    private String mName;
    private String mVorname;

    public  Person(int id, String name, String vorname){
        mId = id;
        mName = name;
        mVorname = vorname;
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
}
