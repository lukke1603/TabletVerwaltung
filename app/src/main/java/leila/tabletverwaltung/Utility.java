package leila.tabletverwaltung;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import leila.tabletverwaltung.DataConnection.DbConnection;

/**
 * Created by Lukas Brinkmann on 30.11.2016.
 */
public class Utility {

    public static boolean dbParametersValid(Context baseContext){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(baseContext);
        String url = sp.getString(SettingsActivity.SP_URL, null);
        String benutzer = sp.getString(SettingsActivity.SP_BENUTZER, null);
        String passwort = sp.getString(SettingsActivity.SP_PASSWORT, null);

        if(url != null && benutzer != null && passwort != null){
            return true;
        }else{
            return false;
        }
    }



    public static void hideKeyboard(InputMethodManager imm, Activity activity ){
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideKeyboard(Activity activity){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        hideKeyboard(imm, activity);
    }



    public static void checkDbConnection(final Context baseContext, final Activity activity, final Runnable ifExists, final Runnable ifNotExists){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isValid = false;
                if(Utility.dbParametersValid(baseContext)) {
                    DbConnection con = null;
                    try {
                        con = DbConnection.connect(baseContext, true);
                        Log.i("BOOLEAN", ""+con.isValid());
                        if(con.isValid()){
                            isValid = true;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {

                    }
                }

                Runnable callback;
                if(isValid){    //  Falls eine Verbindung hergestellt werden konnte
                    callback = ifExists;
                }else{          //  Falls die Verbindung nicht hergestellt werden konnte
                    callback = ifNotExists;
                }

                activity.runOnUiThread(callback);
            }
        }).start();
    }


    public static void initApplicationData(Context baseContext){
    }





}
