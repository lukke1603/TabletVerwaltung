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
 * Created by Lukas on 05.12.2016.
 */
public class DbConnection {
    private static Connection mConnection = null;
    private static boolean valid = false;

    private String domain;
    private String user;
    private String password;


    public DbConnection(String domain, String user, String password, boolean reConnect){
        this.domain = "jdbc:mysql://"+domain+":3306/tabletverwaltung";
        this.user = user;
        this.password = password;

        if(reConnect || mConnection == null) {
            initConnection(reConnect);
        }
    }


    public static DbConnection connect(String domain, String user, String password){
        DbConnection dbc = new DbConnection(domain, user, password, false);
        return dbc;
    }

    public static DbConnection connect(String domain, String user, String password, boolean reConnect){
        DbConnection dbc = new DbConnection(domain, user, password, reConnect);
        return dbc;
    }

    public static DbConnection connect(Context baseContext){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(baseContext);

        String domain = sp.getString(SettingsActivity.SP_URL, null);
        String user = sp.getString(SettingsActivity.SP_BENUTZER, null);
        String password = sp.getString(SettingsActivity.SP_PASSWORT, null);

        DbConnection dbc = new DbConnection(domain, user, password, false);
        return dbc;
    }

    public static DbConnection connect(Context baseContext, boolean reConnect){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(baseContext);

        String domain = sp.getString(SettingsActivity.SP_URL, null);
        String user = sp.getString(SettingsActivity.SP_BENUTZER, null);
        String password = sp.getString(SettingsActivity.SP_PASSWORT, null);

        DbConnection dbc = new DbConnection(domain, user, password, reConnect);
        return dbc;
    }

    public ResultSet Select(String query){
        ResultSet mResult = null;
        try{
            Statement mStatement = (Statement) mConnection.createStatement();
            mResult = mStatement.executeQuery(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return  mResult;
    }


    private boolean ExecuteStatement (String sql) throws SQLException {
        Statement mStatement = (Statement)mConnection.createStatement();
        return mStatement.execute(sql);
    }

    public boolean Update(String sql){
        boolean result = false;
        try {
            result = ExecuteStatement(sql);
        }catch (SQLException exception){
            exception.printStackTrace();
        }finally {
            return result;
        }
    }

    public boolean Insert(String sql){
        boolean result = false;
        try {
            result = ExecuteStatement(sql);
        }catch (SQLException exception){
            exception.printStackTrace();
        }finally {
            return result;
        }
    }


    public boolean isValid(){
        boolean result = false;
        try{
            ResultSet rs = Select("SELECT 1 as `valid`");
            rs.first();
            Log.i("RESULT", rs.getString("valid"));
            if(rs.getInt("valid") == 1 && valid){
                result = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }


    private void initConnection(boolean reConnect){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        boolean valid = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try{
            DriverManager.setLoginTimeout(5);
            if(mConnection == null || reConnect){
                Log.i("RECONNECT", "processing");
                mConnection = (Connection) DriverManager.getConnection
                        (domain,user,password);
                valid = true;
            }
        }catch (SQLException e){
            Log.e("SQL",e.getMessage());
        }

        this.valid = valid;
    }


    private static void close(){
        try {
            if(mConnection != null) mConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}



















//package leila.tabletverwaltung.DataConnection;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.StrictMode;
//import android.preference.PreferenceManager;
//import android.util.Log;
//
//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;
//
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import leila.tabletverwaltung.SettingsActivity;
//
///**
// * Created by Lukas on 28.11.2016.
// */
//public class DbConnection {
//    private Connection mConnection;
//    private Statement mStatement;
//    private ResultSet mResult;
//
//    private String url;
//    private String benutzer;
//    private String passwort;
//
//
//    public DbConnection(String url, String benutzer, String passwort){
//        this.url = "jdbc:mysql://"+url+"/tabletverwaltung";
//        this.benutzer = benutzer;
//        this.passwort = passwort;
//
//        initConnection();
//    }
//
//
//    public static DbConnection connect(String url, String benutzer, String passwort){
//        DbConnection dbc = new DbConnection(url, benutzer, passwort);
//        return dbc;
//    }
//
//
//    public static DbConnection connect(Context context){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//
//        String url = sp.getString(SettingsActivity.SP_URL, null);
//        String benutzer = sp.getString(SettingsActivity.SP_BENUTZER, null);
//        String passwort = sp.getString(SettingsActivity.SP_PASSWORT, null);
//
//        DbConnection dbc = new DbConnection(url, benutzer, passwort);
//        return dbc;
//    }
//
//
//    public ResultSet Select(String query){
//        if(mConnection == null) initConnection();
//
//        try{
//            mStatement = (Statement) mConnection.createStatement();
//            mResult = mStatement.executeQuery(query);
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        return  mResult;
//    }
//
//
//    public void Update(String sql){
//        try {
//            ExecuteStatement(sql);
//        }
//        catch (SQLException exception){
//            Log(exception);
//        }
//    }
//
//    public void Insert(String sql){
//        try {
//            ExecuteStatement(sql);
//        }
//        catch (SQLException exception){
//            Log(exception);
//        }
//
//    }
//
//    private boolean ExecuteStatement (String sql) throws SQLException {
//        mStatement = (Statement)mConnection.createStatement();
//        return mStatement.execute(sql);
//    }
//
//
//
//    public void disconnect(){
//        try {
//            if(mResult != null) this.mResult.close();
//            if(mStatement != null) this.mStatement.close();
//            if(mConnection != null) this.mConnection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void initConnection(){
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        try{
//            DriverManager.setLoginTimeout(5);
//            if(mConnection == null){
//                mConnection = (Connection) DriverManager.getConnection
//                        (url,benutzer,passwort);
//            }
//        }catch (SQLException e){
//            Log.e("SQL",e.getMessage());
//        }
//    }
//
//
//    public boolean isValid(){
//        try{
//            ResultSet rs = Select("SELECT 1 as `valid`");
//            rs.first();
//            if(rs.getInt("valid") != 1){
//                return false;
//            }else{
//                return true;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return false;
//    }
//
//    private void Log(Exception e){
//        Log.e("SQLException", e.getMessage());
//    }
//
//    public ResultSet getmResult() {
//        return mResult;
//    }
//}
