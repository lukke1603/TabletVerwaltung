package leila.tabletverwaltung;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.ResultSet;

import leila.tabletverwaltung.DataConnection.DbConnection;

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


        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String url = sp.getString(SettingsActivity.SP_URL, null);
                String benutzer = sp.getString(SettingsActivity.SP_BENUTZER, null);
                String passwort = sp.getString(SettingsActivity.SP_PASSWORT, null);

                Log.i("URL", url);
                Log.i("BENUTZER", benutzer);
                Log.i("PASSWORT", passwort);

                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                if(url != null && benutzer != null && passwort != null) {
                    try {
                        DbConnection con = DbConnection.CreateInstance("jdbc:mysql://"+url+"/tabletverwaltung", benutzer, passwort);
                        ResultSet rs = con.Select("SELECT 1 as `valid`");
                        rs.first();
                        Log.i("RESULT", Integer.toString(rs.getInt("valid")));
                        if(rs.getInt("valid") == 1){
                            i = new Intent(getApplicationContext(), MainActivity.class);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                startActivity(i);
                return null;
            }

        }.execute();










//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//
//        String url = sp.getString(SettingsActivity.SP_URL, null);
//        String benutzer = sp.getString(SettingsActivity.SP_BENUTZER, null);
//        String passwort = sp.getString(SettingsActivity.SP_PASSWORT, null);
//
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//
//
//        if(url != null && benutzer != null && passwort != null){
//
//            try{
//                DbConnection con = DbConnection.CreateInstance("jdbc:mysql://"+url+"/tabletverwaltung", benutzer, passwort);
//                con.Select("SELECT 1");
//            }catch (Exception e){
//                e.printStackTrace();
//                intent = new Intent(getApplicationContext(), SettingsActivity.class);
//            }





//            try {
//                if(!con.getmConnection().isValid(1)){
//                    Toast.makeText(getApplicationContext(), R.string.toast_settings_datenbankverbindung_falsch, Toast.LENGTH_LONG);
//                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), R.string.toast_settings_datenbankverbindung_falsch, Toast.LENGTH_LONG);
//                intent = new Intent(getApplicationContext(), SettingsActivity.class);
//            }
//        }else{
//            intent = new Intent(getApplicationContext(), SettingsActivity.class);
//        }



//        startActivity(intent);

    }
}
