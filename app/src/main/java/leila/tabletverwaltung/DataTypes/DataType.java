package leila.tabletverwaltung.DataTypes;

import android.content.Context;

import leila.tabletverwaltung.DataConnection.DbConnection;

/**
 * Created by Lukas on 27.11.2016.
 */
public abstract class DataType {
    protected Context context;
    protected DbConnection dbc;

    public DataType(Context context){
        this.context = context;
        this.dbc = DbConnection.connect(context);
    }
}
