package leila.tabletverwaltung;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.ResultSet;

import leila.tabletverwaltung.DataConnection.DbConnection;
import leila.tabletverwaltung.DataConnection.DbConnector;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.i("ACTIVITY", "StartActivity");

        new Start(this, getApplicationContext(), getBaseContext()).execute();
    }
}

class Start extends AsyncTask{
    private Intent i;
    private Context baseContext;
    private Context context;
    private Activity activity;

    public Start(Activity activity, Context context, Context baseContext){
        this.context = context;
        this.baseContext = baseContext;
        this.activity = activity;
    }

    @Override
    protected Object doInBackground(Object... params) {

        Log.i("TEST", "here");


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(baseContext);
        String url = sp.getString(SettingsActivity.SP_URL, null);
        String benutzer = sp.getString(SettingsActivity.SP_BENUTZER, null);
        String passwort = sp.getString(SettingsActivity.SP_PASSWORT, null);
//
        Log.i("URL", url);
        Log.i("BENUTZER", benutzer);
        Log.i("PASSWORT", passwort);
//
        i = new Intent(context, SettingsActivity.class);
        if(url != null && benutzer != null && passwort != null) {
            DbConnector con = null;
            try {
                con = DbConnector.connect(url, benutzer, passwort);
                Log.i("CONNECTION", con.toString());
                ResultSet rs = con.Select("SELECT 1 as `valid`");
                rs.first();
                Log.i("RESULT", Integer.toString(rs.getInt("valid")));
                if(rs.getInt("valid") == 1){
                    i = new Intent(context, MainActivity.class);
                }

            }catch (Exception e){
                e.printStackTrace();
                Log.i("EXCEPTION", "here");
            }finally {
                Log.i("STATE", "FINAL");
                if(con != null){
                    Log.i("STATE", "CLOSE");
                    con.disconnect();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        activity.startActivity(i);

    }
}
