package leila.tabletverwaltung.DataConnection;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by a.moszczynski on 15.11.2016.
 */
public class DbConnection {
    private Connection mConnection;
    private  static DbConnection mInstance;

    private static String mDataBaseName;
    private static String mUserName;
    private static String mPassWord;

    private DbConnection(String dbName, String userName, String passWord){
        mDataBaseName = dbName;
        mUserName = userName;
        mPassWord = passWord;

        try
        {
            mConnection = DriverManager.getConnection
                    (mDataBaseName,mUserName,mPassWord);
        }catch (SQLException e){
            Log.e("SQL",e.getMessage());
        }
    }

    public static DbConnection CreateInstance(String dbName, String userName, String passWord){

        if(mInstance == null)
            mInstance = new DbConnection(dbName,userName,passWord);
        return mInstance;
    }

    public static DbConnection GetInstance(){
        if(mInstance == null)
            mInstance = new DbConnection(mDataBaseName, mUserName,mPassWord);
        return  mInstance;
    }

    public ResultSet Select(String query){
        ResultSet resultSet = null;
        try{
            Statement statement = mConnection.createStatement();
            resultSet = statement.executeQuery(query);
        }catch (SQLException e){
            Log(e);
        }
        return  resultSet;
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
        Statement statement = mConnection.createStatement();
        return statement.execute(sql);
    }

    private void Log(Exception e){
        Log.e("SQLException", e.getMessage());
    }
}







