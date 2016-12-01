package leila.tabletverwaltung;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

import leila.tabletverwaltung.Adapter.GeraeteAdapter;
import leila.tabletverwaltung.DataTypes.Hardware;

public class GeraeteActivity extends AppCompatActivity {
    private ListView lvGeraete;

    private ArrayList<Hardware> geraete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geraete);

        lvGeraete = (ListView)findViewById(R.id.lvGeraete);

    }


    @Override
    protected void onResume() {
        super.onResume();

        geraete = Hardware.getAll(getBaseContext(), true);

        Log.i("HERE", "here");
        GeraeteAdapter gAdapter = new GeraeteAdapter(getApplicationContext(), geraete);
        lvGeraete.setAdapter(gAdapter);
    }
}
