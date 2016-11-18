package leila.tabletverwaltung.DataTypes;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class Schueler extends Person {

    private int mKursId;

    public Schueler(int id, String name, String vorname, int kursId) {
        super(id, name, vorname);
        mKursId = kursId;
    }

    public  int getKursId(){
        return  mKursId;
    }
}
