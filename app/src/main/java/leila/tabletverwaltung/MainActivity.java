package leila.tabletverwaltung;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.view.MenuInflater;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner sLehrer;
    private RelativeLayout rlGeraete;
    private RelativeLayout rlEinlesen;
    private ArrayList<String> lehrer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sLehrer = (Spinner) findViewById(R.id.sLehrer);
        rlGeraete = (RelativeLayout) findViewById(R.id.rlGeraete);
        rlEinlesen = (RelativeLayout) findViewById(R.id.rlEinlesen);

        rlEinlesen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ReaderActivity.class);
                startActivity(i);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.einstellungen) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void initSpinnerLehrer(){
        lehrer.add("Ruhl Holger");
        lehrer.add("Schlüter Bernd");

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, lehrer);
        sLehrer.setAdapter(adapter);
    }
}
