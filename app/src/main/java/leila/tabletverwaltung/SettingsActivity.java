package leila.tabletverwaltung;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

import leila.tabletverwaltung.DataConnection.DbConnection;

public class SettingsActivity extends AppCompatActivity {

    private EditText etUrl;
    private EditText etBenutzer;
    private EditText etPasswort;

    private static String DEFAULT_URL = "www.ihreDomain.de/tablets";

    private static String SP_PREFIX = "tabletverwaltung";
    public static String SP_URL = SP_PREFIX+".url";
    public static String SP_BENUTZER = SP_PREFIX+".benutzer";
    public static String SP_PASSWORT = SP_PREFIX+".passwort";

    private boolean isCheckingConnection = false;

    private RelativeLayout flLoading;

    private Map spOld;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.i("ACTIVITY", "SettingsActivity");

        flLoading = (RelativeLayout) findViewById(R.id.progress_overlay);

        this.sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        initViews();
        saveSettings();

        this.spOld = this.sp.getAll();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(isCheckingConnection) return true;

        Utility.hideKeyboard(this);

        if(id == android.R.id.home){
            validateChanges();
            return true;
        }else if (id == R.id.einstellungenSpeichern) {
            saveSettings();
            checkDbConnection();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateChanges() {
        final Intent upIntent = NavUtils.getParentActivityIntent(this);

        if(!this.etBenutzer.getText().toString().equals(this.spOld.get(SP_BENUTZER).toString()) ||
                !this.etPasswort.getText().toString().equals(this.spOld.get(SP_PASSWORT).toString()) ||
                !this.etUrl.getText().toString().equals(this.spOld.get(SP_URL).toString())){

            Utility.hideKeyboard(this);

            new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialog)
                    .setTitle(R.string.alertEinstellungenSpeichernTitle)
                    .setMessage(R.string.alertEinstellungenSpeichernMessage)
                    .setPositiveButton(R.string.alertEinstellungenSpeichernPositiveButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveSettings();
                            checkDbConnection();
                        }
                    })
                    .setNegativeButton(R.string.alertEinstellungenSpeichernNegativeButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkDbConnection();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            checkDbConnection();
        }
    }


    private void saveSettings() {
        SharedPreferences.Editor spEditor = this.sp.edit();
        spEditor.putString(SP_URL, this.etUrl.getText().toString());
        spEditor.putString(SP_PASSWORT, this.etPasswort.getText().toString());
        spEditor.putString(SP_BENUTZER, this.etBenutzer.getText().toString());
        spEditor.commit();
    }

    private void initViews() {
        this.etUrl = (EditText)findViewById(R.id.etUrl);
        this.etBenutzer = (EditText)findViewById(R.id.etBenutzer);
        this.etPasswort = (EditText)findViewById(R.id.etPasswort);

        this.etUrl.setText(this.sp.getString(SP_URL, DEFAULT_URL));
        this.etBenutzer.setText(this.sp.getString(SP_BENUTZER, null));
        this.etPasswort.setText(this.sp.getString(SP_PASSWORT, null));
    }


    private void checkDbConnection(){
        flLoading.setVisibility(View.VISIBLE);
        isCheckingConnection = true;

        Utility.checkDbConnection(getBaseContext(), this, new Runnable() {
            @Override
            public void run() {
//                Intent nextIntent = new Intent(SettingsActivity.this, MainActivity.class);

                Intent nextIntent = new Intent();
                nextIntent.putExtra("connectionIsValid", true);
                setResult(Activity.RESULT_OK, nextIntent);
//                startActivity(nextIntent);
                finish();
            }
        }, new Runnable() {
            @Override
            public void run() {
                Intent nextIntent = new Intent(SettingsActivity.this, SettingsActivity.class);
                Toast.makeText(SettingsActivity.this, getResources().getString(R.string.toast_settings_verbindung_fehlgeschlagen), Toast.LENGTH_LONG).show();
                startActivity(nextIntent);
                isCheckingConnection = false;
                finish();
            }
        });

//
    }


}
