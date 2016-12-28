package leila.tabletverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import leila.tabletverwaltung.Adapter.GeraeteAdapter;
import leila.tabletverwaltung.DataTypes.Hardware;

public class GeraeteActivity extends AppCompatActivity {
    private ListView lvGeraete;
    private RelativeLayout flLoading;

    private ArrayList<Hardware> geraete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geraete);

        lvGeraete = (ListView)findViewById(R.id.lvGeraete);

        flLoading = (RelativeLayout)findViewById(R.id.progress_overlay);
        flLoading.setVisibility(View.VISIBLE);

        lvGeraete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int geraeteId = (int)view.getTag();
                Intent i = new Intent(GeraeteActivity.this, HistorieActivity.class);
                i.putExtra("geraeteId", geraeteId);
                startActivity(i);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        flLoading.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                geraete = Hardware.getAll(getBaseContext(), true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GeraeteAdapter gAdapter = new GeraeteAdapter(getApplicationContext(), geraete);
                        lvGeraete.setAdapter(gAdapter);
                        flLoading.setVisibility(View.GONE);
                    }
                });

            }
        }).start();

    }
}
