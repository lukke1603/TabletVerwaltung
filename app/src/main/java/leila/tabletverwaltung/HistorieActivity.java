package leila.tabletverwaltung;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import leila.tabletverwaltung.Adapter.HistorieAdapter;
import leila.tabletverwaltung.DataTypes.Historie;

public class HistorieActivity extends AppCompatActivity {

    private ListView lvHistorie;

    private int geraeteId;
    private ArrayList<Historie> eintraege;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historie);

        lvHistorie = (ListView) findViewById(R.id.lvHistorie);
        geraeteId = getIntent().getIntExtra("geraeteId", 0);
    }


    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {

                eintraege = Historie.getAll(getBaseContext(), geraeteId, true);
                HistorieAdapter hAdapter = new HistorieAdapter(getApplicationContext(), eintraege);
                lvHistorie.setAdapter(hAdapter);

            }
        }).start();
    }
}
