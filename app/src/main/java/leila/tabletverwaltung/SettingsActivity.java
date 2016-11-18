package leila.tabletverwaltung;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    private EditText etUrl;
    private EditText etBenutzer;
    private EditText etPasswort;

    private static String DEFAULT_URL = "www.ihreDomain.de/tablets";

    private static String SP_PREFIX = "tabletverwaltung";
    private static String SP_URL = SP_PREFIX+".url";
    private static String SP_BENUTZER = SP_PREFIX+".benutzer";
    private static String SP_PASSWORT = SP_PREFIX+".passwort";

    private InputMethodManager imm;




    private Map spOld;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        this.sp = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);

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

        Log.i("ID", Integer.toString(id));


        if(id == android.R.id.home){
            validateChanges();
            return true;
        }else if (id == R.id.einstellungenSpeichern) {
            saveSettings();
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void validateChanges() {

        if(!this.etBenutzer.getText().toString().equals(this.spOld.get(SP_BENUTZER).toString()) ||
                !this.etPasswort.getText().toString().equals(this.spOld.get(SP_PASSWORT).toString()) ||
                !this.etUrl.getText().toString().equals(this.spOld.get(SP_URL).toString())){

            this.imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            new AlertDialog.Builder(SettingsActivity.this, R.style.AlertDialog)
                    .setTitle(R.string.alertEinstellungenSpeichernTitle)
                    .setMessage(R.string.alertEinstellungenSpeichernMessage)
                    .setPositiveButton(R.string.alertEinstellungenSpeichernPositiveButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveSettings();
                            NavUtils.navigateUpFromSameTask(SettingsActivity.this);
                        }
                    })
                    .setNegativeButton(R.string.alertEinstellungenSpeichernNegativeButton, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NavUtils.navigateUpFromSameTask(SettingsActivity.this);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            NavUtils.navigateUpFromSameTask(SettingsActivity.this);
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


}
