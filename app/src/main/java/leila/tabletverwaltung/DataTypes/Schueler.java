package leila.tabletverwaltung.DataTypes;

import android.content.Context;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class Schueler extends Person {

    private int mKursId;

    public Schueler(Context context, int id, String name, String vorname, int kursId) {
        super(context, id, name, vorname);
        mKursId = kursId;
    }

    public  int getKursId(){
        return  mKursId;
    }
}
