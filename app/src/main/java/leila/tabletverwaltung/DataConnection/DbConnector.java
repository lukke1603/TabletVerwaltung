package leila.tabletverwaltung.DataConnection;

import android.os.StrictMode;
import android.util.Log;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Lukas on 28.11.2016.
 */
public class DbConnector {
    private Connection mConnection;
    private Statement mStatement;
    private ResultSet mResult;

    private String url;
    private String benutzer;
    private String passwort;


    public DbConnector(String url, String benutzer, String passwort){
        this.url = "jdbc:mysql://"+url+"/tabletverwaltung";
        this.benutzer = benutzer;
        this.passwort = passwort;

        initConnection();
    }


    public static DbConnector connect(String url, String benutzer, String passwort){
        DbConnector dbc = new DbConnector(url, benutzer, passwort);
        return dbc;
    }


    public ResultSet Select(String query){
        if(mConnection == null) initConnection();

        try{
            mStatement = (Statement) mConnection.createStatement();
            mResult = mStatement.executeQuery(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  mResult;
    }


    public void disconnect(){
        try {
            if(mResult != null) this.mResult.close();
            if(mStatement != null) this.mStatement.close();
            if(mConnection != null) this.mConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void initConnection(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try{
            DriverManager.setLoginTimeout(5);
            mConnection = (Connection) DriverManager.getConnection
                    (url,benutzer,passwort);
        }catch (SQLException e){
            Log.e("SQL",e.getMessage());
            Log.i("DRIVERMANAGER", "here");
        }
    }


}
