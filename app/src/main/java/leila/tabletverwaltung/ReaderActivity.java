package leila.tabletverwaltung;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import leila.tabletverwaltung.Adapter.SchuelerAdapter;
import leila.tabletverwaltung.DataTypes.Hardware;
import leila.tabletverwaltung.DataTypes.Historie;
import leila.tabletverwaltung.DataTypes.Kurs;
import leila.tabletverwaltung.DataTypes.Schueler;


public class ReaderActivity extends AppCompatActivity {
    private ImageView ivBorder;
    private TextView tvBarcodeResult;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private RelativeLayout flLoading;


    private AlertDialog dialog;
    private AlertDialog innerDialog;

    private int kursId;
    private int lehrerId;
    private boolean klassenWeise;

    private ArrayList<Schueler> schueler = new ArrayList<>();


    private static boolean barcodeDetected = false;
    private static boolean surfaceCreated = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        flLoading = (RelativeLayout) findViewById(R.id.progress_overlay);
        flLoading.setVisibility(View.VISIBLE);

        Intent parseIntent = getIntent();
        kursId = parseIntent.getIntExtra("kurs", 0);
        lehrerId = parseIntent.getIntExtra("lehrer", 0);
        klassenWeise = parseIntent.getBooleanExtra("klassenweiseAusgeben", false);


        Log.e("READER", "CREATE");


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        tvBarcodeResult = (TextView) findViewById(R.id.tvBarcodeResult);
        ivBorder = (ImageView) findViewById(R.id.ivBorder);
        cameraView = (SurfaceView) findViewById(R.id.camera_view);

        barcodeDetected = false;

        tvBarcodeResult.setText("");
        ivBorder.setImageResource(R.drawable.barcode_border_box_white);

        initCamera();

    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.e("READER", "START");

        new Thread(new Runnable() {
            @Override
            public void run() {
                schueler = Schueler.getAllFromKurs(getBaseContext(), kursId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        flLoading.setVisibility(View.GONE);
                        if(surfaceCreated) initSurfaceView();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Reader Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://leila.tabletverwaltung/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        Log.e("READER", "PAUSE");
        client.disconnect();
        cameraSource.stop();

        if (dialog != null) dialog.cancel();
        if (innerDialog != null) innerDialog.cancel();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

//        barcodeDetected = false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("READER", "RESUME");

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        barcodeDetected = false;

        tvBarcodeResult.setText("");
        ivBorder.setImageResource(R.drawable.barcode_border_box_white);


    }


    private static boolean cameraFocus(CameraSource cameraSource, String focusMode) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        Camera.Parameters params = camera.getParameters();
                        params.setFocusMode(focusMode);
                        camera.setParameters(params);
                        return true;
                    }

                    return false;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                break;
            }
        }

        return false;
    }


    private void initCamera() {
        Log.i("STATE", "initCamera");
        cameraView.getHolder().addCallback(getHolderCallback());

    }

    private void validateBarcode(final String barcode){
        Log.i("BARCODE", barcode);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Hardware geraet = Hardware.getFromBarcode(getBaseContext(), barcode);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(geraet != null){
                            Log.i("GERAET", "here");
                            Log.i("VERLIEHEN", (geraet.isVerliehen()) ? "true" : "false");
                            if(geraet.isVerliehen()){       //  Gerät ist bereits verliehen
                                Log.i("GERAET VERLIEHEN", "here");
                                dialog = new AlertDialog.Builder(ReaderActivity.this, R.style.AlertDialog)
                                        .setTitle(R.string.alertReaderBereitsVerliehenTitle)
                                        .setMessage(R.string.alertReaderBereitsVerliehenMessage)
                                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                            @Override
                                            public void onCancel(DialogInterface dialog) {
                                                onResume();
                                            }
                                        })
                                        .setPositiveButton(R.string.alertReaderBereitsVerliehenPositiveButton, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if(klassenWeise){
                                                    Log.i("KURS", Integer.toString(kursId));
                                                    verleiheAnKlasse(geraet, kursId, lehrerId);
                                                }else{



                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_spinner_schueler, null, false);
                                                                    final Spinner sp = (Spinner) v.findViewById(R.id.spSchueler);

                                                                    SchuelerAdapter sAdapter = new SchuelerAdapter(getBaseContext(), schueler);
                                                                    sp.setAdapter(sAdapter);
                                                                    sp.getBackground().setColorFilter(getResources().getColor(R.color.white200), PorterDuff.Mode.SRC_ATOP);

                                                                    innerDialog = new AlertDialog.Builder(ReaderActivity.this, R.style.AlertDialog)
                                                                            .setTitle(R.string.alertReaderAnSchuelerAusgebenTitle)
                                                                            .setView(v)
                                                                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                                                @Override
                                                                                public void onCancel(DialogInterface dialog) {
                                                                                    onResume();
                                                                                }
                                                                            })
                                                                            .setPositiveButton(R.string.alertReaderAusleihen, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    verleiheAnSchueler(geraet, (Schueler)sp.getSelectedItem(), lehrerId);
                                                                                }
                                                                            })
                                                                            .setNegativeButton(R.string.alertReaderAbbrechen, new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    onResume();
                                                                                }
                                                                            })
                                                                            .show();
                                                                }
                                                            });

                                                        }
                                                    }).start();


                                                }
                                            }
                                        })
                                        .setNegativeButton(R.string.alertReaderBereitsVerliehenNegativeButton, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                geraetZurueckgeben(geraet);
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }else{
                                Log.i("GERAET VERFÜGBAR", "here");

                                if(klassenWeise){       //  Gerät wird an Klasse ausgegeben

                                    Log.i("KURS", Integer.toString(kursId));

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            final Kurs kurs = Kurs.get(getBaseContext(), kursId);

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(kurs != null){
                                                        String title = getResources().getString(R.string.alertReaderAnKlasseAusgebenTitle).replace("%kur_name%", kurs.getKursName());
                                                        String message = getResources().getString(R.string.alertReaderAnKlasseAusgebenMessage).replace("%kur_name%", kurs.getKursName());

                                                        dialog = new AlertDialog.Builder(ReaderActivity.this, R.style.AlertDialog)
                                                                .setTitle(title)
                                                                .setMessage(message)
                                                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                                    @Override
                                                                    public void onCancel(DialogInterface dialog) {
                                                                        onResume();
                                                                    }
                                                                })
                                                                .setPositiveButton(R.string.alertReaderAnKlasseAusgebenPositiveButton, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        verleiheAnKlasse(geraet, kurs.getKursId(), lehrerId);
                                                                        onResume();
                                                                    }
                                                                })
                                                                .setNegativeButton(R.string.alertReaderAnKlasseAusgebenNegativeButton, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        onResume();
                                                                    }
                                                                })
                                                                .show();
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                }else{

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.dialog_spinner_schueler, null, false);
                                                    final Spinner sp = (Spinner) v.findViewById(R.id.spSchueler);

                                                    SchuelerAdapter sAdapter = new SchuelerAdapter(getBaseContext(), schueler);
                                                    sp.setAdapter(sAdapter);
                                                    sp.getBackground().setColorFilter(getResources().getColor(R.color.white200), PorterDuff.Mode.SRC_ATOP);

                                                    dialog = new AlertDialog.Builder(ReaderActivity.this, R.style.AlertDialog)
                                                            .setTitle(R.string.alertReaderAnSchuelerAusgebenTitle)
                                                            .setView(v)
                                                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                                @Override
                                                                public void onCancel(DialogInterface dialog) {
                                                                    onResume();
                                                                }
                                                            })
                                                            .setPositiveButton(R.string.alertReaderAusleihen, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    verleiheAnSchueler(geraet, (Schueler)sp.getSelectedItem(), lehrerId);
                                                                }
                                                            })
                                                            .setNegativeButton(R.string.alertReaderAbbrechen, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    onResume();
                                                                }
                                                            })
                                                            .show();
                                                }
                                            });
                                        }
                                    }).start();
                                }
                            }
                        }else{
                            onResume();
                        }
                    }
                });

            }
        }).start();

    }



    private void geraetZurueckgeben(final Hardware geraet) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Historie.geraetZurueckgeben(getBaseContext(), geraet.getmId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.toast_geraet_zuruecknehmen, Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                });
            }
        }).start();
    }


    private void verleiheAnSchueler(final Hardware geraet, final Schueler schueler, final int lehrerId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Historie.setVerliehenAnSchueler(getBaseContext(),schueler.getId(),lehrerId,geraet.getmId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.toast_geraet_verliehen, Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                });
            }
        }).start();
    }


    private void verleiheAnKlasse(final Hardware geraet, final int kursId, final int lehrerId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Historie.setVerliehenAnKurs(getBaseContext(),kursId,lehrerId,geraet.getmId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), R.string.toast_geraet_verliehen, Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                });
            }
        }).start();
    }


    public SurfaceHolder.Callback getHolderCallback() {
        return new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i("STATE", "SurfaceCreated");
                initSurfaceView();
                surfaceCreated = true;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i("CHANGED SURFACE", "here");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
                surfaceCreated = false;
            }
        };
    }

    private void initSurfaceView() {
        try {
            BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(ReaderActivity.this).setBarcodeFormats(
                    Barcode.QR_CODE | Barcode.DATA_MATRIX | Barcode.EAN_8 | Barcode.EAN_13 | Barcode.UPC_A | Barcode.UPC_E | Barcode.CODE_128 | Barcode.ITF | Barcode.CODE_39
            ).build();

            Log.i("HEIGHT", Integer.toString(cameraView.getHeight()));
            cameraSource = new CameraSource.Builder(ReaderActivity.this, barcodeDetector).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(cameraView.getWidth(), (cameraView.getHeight() / 2)).build();

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    if (barcodeDetected) return;
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                    //  Barcode wurde erkannt
                    if (barcodes.size() != 0) {
                        barcodeDetected = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String seriennummer = barcodes.valueAt(0).displayValue;

                                tvBarcodeResult.setText(seriennummer);
                                ivBorder.setImageResource(R.drawable.barcode_border_box_green);
                                Vibrator v = (Vibrator) ReaderActivity.this.getSystemService(VIBRATOR_SERVICE);
                                v.vibrate(500);

                                validateBarcode(seriennummer);
                            }
                        });
                    }
                }
            });

            if (ActivityCompat.checkSelfPermission(ReaderActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            cameraSource.start(cameraView.getHolder());
            cameraFocus(cameraSource, Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
