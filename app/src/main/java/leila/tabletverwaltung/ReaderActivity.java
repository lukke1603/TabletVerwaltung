package leila.tabletverwaltung;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.lang.reflect.Field;

import leila.tabletverwaltung.DataTypes.Hardware;


public class ReaderActivity extends AppCompatActivity {
    private ImageView ivBorder;
    private TextView tvBarcodeResult;
    private SurfaceView cameraView;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    private AlertDialog dialog;

    private int kursId;
    private boolean klassenWeise;

    private static boolean barcodeDetected = false;
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

        Intent parseIntent = getIntent();
        kursId = parseIntent.getIntExtra("kursId", 0);
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

        dialog.cancel();
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

//        initCamera();
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
                            if(geraet.isVerliehen()){
                                Log.i("GERAET VERLIEHEN", "here");
                                dialog = new AlertDialog.Builder(ReaderActivity.this, R.style.AlertDialog)
                                        .setTitle(R.string.alertReaderBereitsVerliehenTitle)
                                        .setMessage(R.string.alertReaderBereitsVerliehenMessage)
                                        .setPositiveButton(R.string.alertReaderBereitsVerliehenPositiveButton, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setNegativeButton(R.string.alertReaderBereitsVerliehenNegativeButton, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                onResume();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                    }
                });

            }
        }).start();

    }


    public SurfaceHolder.Callback getHolderCallback() {
        return new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    barcodeDetector = new BarcodeDetector.Builder(ReaderActivity.this).setBarcodeFormats(
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

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        };
    }
}
