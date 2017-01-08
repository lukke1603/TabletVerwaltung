package leila.tabletverwaltung;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import leila.tabletverwaltung.Adapter.HistorieAdapter;
import leila.tabletverwaltung.DataTypes.Historie;

public class HistorieActivity extends AppCompatActivity {

    private ListView lvHistorie;

    private int geraeteId;
    private ArrayList<Historie> eintraege;
    private RelativeLayout progress_overlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historie);

        progress_overlay = (RelativeLayout)findViewById(R.id.progress_overlay);
        lvHistorie = (ListView) findViewById(R.id.lvHistorie);
        geraeteId = getIntent().getIntExtra("geraeteId", 0);
    }


    @Override
    protected void onResume() {
        super.onResume();

        progress_overlay.setVisibility(View.VISIBLE);

        /**
         * Startet einen neuen Thread
         *
         * holt alle Eintrage der Historie für ein Gerät und listet sie in der ListView
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                eintraege = Historie.getAll(getBaseContext(), geraeteId, true);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HistorieAdapter hAdapter = new HistorieAdapter(getApplicationContext(), eintraege);
                        lvHistorie.setAdapter(hAdapter);

                        progress_overlay.setVisibility(View.GONE);
                    }
                });


            }
        }).start();
    }
}
