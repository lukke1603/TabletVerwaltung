package leila.tabletverwaltung;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;


import java.security.Permission;
import java.util.ArrayList;
import java.util.jar.*;

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
    public static final int PERMISSION_REQUEST_CODE_CAMERA = 1;

    private RelativeLayout flLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utility.initApplicationData(getApplicationContext());

        Log.i("ACTIVITY", "Mainactivity");

        flLoading = (RelativeLayout) findViewById(R.id.progress_overlay);
    }


    @Override
    protected void onResume(){
        super.onResume();

        flLoading.setVisibility(View.VISIBLE);

        boolean connectionIsValid = getIntent().getBooleanExtra("connectionIsValid", false);
        if(connectionIsValid) {
            createMainActivity();
        }else{
            isCheckingConnection = true;

            Utility.checkDbConnection(getBaseContext(), this, new Runnable() {
                @Override
                public void run() {
                    isCheckingConnection = false;

                    createMainActivity();
                }
            }, new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_settings_verbindung_fehlgeschlagen), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(i);
                }
            });
        }
    }


    private void createMainActivity(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        sLehrer = (Spinner) findViewById(R.id.sLehrer);
        rlGeraete = (RelativeLayout) findViewById(R.id.rlGeraete);
        rlEinlesen = (RelativeLayout) findViewById(R.id.rlEinlesen);

        final Activity currentActivity = this;
        rlEinlesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int perm = currentActivity.checkSelfPermission(Manifest.permission.CAMERA);
                    if (perm != PackageManager.PERMISSION_GRANTED) {
                        // Permission not granted (need to ask for it).
                        currentActivity.requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE_CAMERA);
                    }
                    else {
                        // Permission granted (user already accepted).
                        Intent i = new Intent(MainActivity.this, ReaderActivity.class);
                        startActivity(i);
                    }
                } else {
                    // Permission granted (because no runtime permission).
                    Intent i = new Intent(MainActivity.this, ReaderActivity.class);
                    startActivity(i);
                }
            }
        });

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
        if (isCheckingConnection) return false;

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
                        sLehrer.setAdapter(adapter);
                        flLoading.setVisibility(View.GONE);
                    }
                });
            }
        }).start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    rlEinlesen.performClick();
                }
                break;
        }

    }
}




