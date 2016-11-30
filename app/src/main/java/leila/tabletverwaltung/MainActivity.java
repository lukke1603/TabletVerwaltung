package leila.tabletverwaltung;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.util.ArrayList;

import leila.tabletverwaltung.Adapter.LehrerAdapter;
import leila.tabletverwaltung.DataConnection.DbConnection;
import leila.tabletverwaltung.DataTypes.Lehrer;

public class MainActivity extends AppCompatActivity {
    private DbConnection dbc;

    private String url;
    private String benutzer;
    private String passwort;
    private Intent nextIntent;

    private Spinner sLehrer;
    private RelativeLayout rlGeraete;
    private RelativeLayout rlEinlesen;
    private static ArrayList<Lehrer> lehrer = new ArrayList<Lehrer>();

    private boolean isCheckingConnection = false;

    private RelativeLayout flLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("ACTIVITY", "Mainactivity");

        flLoading = (RelativeLayout) findViewById(R.id.progress_overlay);
        flLoading.setVisibility(View.VISIBLE);

        boolean connectionIsValid = getIntent().getBooleanExtra("connectionIsValid", false);
        if(connectionIsValid) {
            createMainActivity();
        }else{
            isCheckingConnection = true;
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            url = sp.getString(SettingsActivity.SP_URL, null);
            benutzer = sp.getString(SettingsActivity.SP_BENUTZER, null);
            passwort = sp.getString(SettingsActivity.SP_PASSWORT, null);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    if(url != null && benutzer != null && passwort != null) {
                        DbConnection con = null;
                        try {
                            con = DbConnection.connect(getBaseContext());
                            ResultSet rs = con.Select("SELECT 1 as `valid`");
                            rs.first();
                            if(rs.getInt("valid") != 1) {
                                nextIntent = new Intent(MainActivity.this, SettingsActivity.class);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            nextIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        }finally {
                            if(con != null){
                                con.disconnect();
                            }
                        }
                    }else{
                        nextIntent = new Intent(MainActivity.this, SettingsActivity.class);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(nextIntent != null){
                                Log.i("TOAST", "Mainacti");
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_settings_verbindung_fehlgeschlagen), Toast.LENGTH_LONG).show();
                                startActivity(nextIntent);
                            }else{
                                isCheckingConnection = false;
                                createMainActivity();
                            }

                        }
                    });

                }
            }).start();
        }






    }


    private void createMainActivity(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        sLehrer = (Spinner) findViewById(R.id.sLehrer);
        rlGeraete = (RelativeLayout) findViewById(R.id.rlGeraete);
        rlEinlesen = (RelativeLayout) findViewById(R.id.rlEinlesen);

        rlEinlesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ReaderActivity.class);
                startActivity(i);
            }
        });

        Log.i("STATE", "here");

        initSpinnerLehrer();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.einstellungen) {
            Intent i = new Intent(this.getApplicationContext(), SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initSpinnerLehrer(){
        //  Färbt das Dreieck des Spinners weis
        sLehrer.getBackground().setColorFilter(getResources().getColor(R.color.white200), PorterDuff.Mode.SRC_ATOP);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.lehrer = Lehrer.getAll(getBaseContext());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LehrerAdapter adapter = new LehrerAdapter(getApplicationContext(), MainActivity.lehrer);
                        Log.i("TEST", "here");
                        sLehrer.setAdapter(adapter);
                        flLoading.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

        //  Asynctask um die DB auszulesen ohne die MainThread zu blockieren

    }




}



//class LadeLehrer extends AsyncTask<String, String, ArrayList<Lehrer>> {
//    private ArrayList<Lehrer> lehrer;
//
//    @Override
//    protected ArrayList<Lehrer> doInBackground(String... params) {
//        lehrer = Lehrer.getAll(getBaseContext());
//        return lehrer;
//    }
//
//    @Override
//    protected void onPostExecute(ArrayList<Lehrer> lehrer) {
//        super.onPostExecute(lehrer);
//        //  Die Lehrer dem Spinner zuweisen.
//        Log.i("TEST", "here");
//        LehrerAdapter adapter = new LehrerAdapter(getApplicationContext(), lehrer);
//        sLehrer.setAdapter(adapter);
//    }
//}


