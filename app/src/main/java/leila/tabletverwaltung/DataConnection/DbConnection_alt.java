package leila.tabletverwaltung.DataConnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import leila.tabletverwaltung.SettingsActivity;


/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class DbConnection_alt {
    private Connection mConnection;
    private ResultSet mResult;
    private Statement mStatement;

    private static DbConnection_alt mInstance;

    private static String mUrl;
    private static String mUserName;
    private static String mPassWord;

    private DbConnection_alt(String url, String userName, String passWord){
        mUrl = "jdbc:mysql://"+url+"/tabletverwaltung";
        mUserName = userName;
        mPassWord = passWord;

        setConnection();
    }

    public static DbConnection_alt CreateInstance(String url, String userName, String passWord){
        if(mInstance == null) {
            mInstance = new DbConnection_alt(url, userName, passWord);
        }else {
            mInstance.setConnection();
        }
        return mInstance;
    }


    public static DbConnection_alt CreateInstance(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        if(mInstance == null){
            mInstance = new DbConnection_alt(sp.getString(SettingsActivity.SP_URL, null), sp.getString(SettingsActivity.SP_BENUTZER, null), sp.getString(SettingsActivity.SP_PASSWORT, null));
        }else{
            mInstance.setConnection();
        }

        return mInstance;
    }


    public static DbConnection_alt GetInstance(){
        if(mInstance == null){
            mInstance = new DbConnection_alt(mUrl, mUserName,mPassWord);
        }else{
            mInstance.setConnection();
        }

        return  mInstance;
    }

    public ResultSet Select(String query){
        try{
            mStatement = (Statement) mConnection.createStatement();
            mResult = mStatement.executeQuery(query);
        }catch (SQLException e){
            Log(e);
        }
        return  mResult;
    }

    public void Update(String sql){
        try {
            ExecuteStatement(sql);
        }
        catch (SQLException exception){
            Log(exception);
        }
    }

    public void Insert(String sql){
        try {
            ExecuteStatement(sql);
        }
        catch (SQLException exception){
            Log(exception);
        }

    }

    private boolean ExecuteStatement (String sql) throws SQLException {
        mStatement = (Statement)mConnection.createStatement();
        return mStatement.execute(sql);
    }


    public void close(){
        try {
            if(this.mConnection != null){
                if(mResult != null) this.mResult.close();
                if(mStatement != null) this.mStatement.close();
                if(mConnection != null) this.mConnection.close();
                this.mInstance = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setConnection(){
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
                    (mUrl,mUserName,mPassWord);
        }catch (SQLException e){
        }
    }


    private void Log(Exception e){
    }

    public Connection getmConnection() {
        return mConnection;
    }
}









