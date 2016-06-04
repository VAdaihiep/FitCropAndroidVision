package vn.vadaihiep.fitcropandroidvision;

import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import vn.vadaihiep.fitcropandroidvision.camera.BarcodeTrackerFactory;
import vn.vadaihiep.fitcropandroidvision.camera.CameraSourcePreview;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "fitcropandroidvision";

    private TextView txtResult;

    private CameraSourcePreview cameraPreview;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = (TextView) findViewById(R.id.txtResult);
        cameraPreview = (CameraSourcePreview) findViewById(R.id.preview);

        createCameraSource(false);

    }

    private void createCameraSource(boolean frontCamera) {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(new MyBarcodeTracker());
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());

        if (!barcodeDetector.isOperational()) {
            Log.w(TAG, "Detector dependencies are not yet available.");

            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this, "Low storage error!!!", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Low storage error!!!");
            }
        }

        // Creates and starts the camera
        CameraSource.Builder builder = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 640)
                .setRequestedFps(15.0f);

        if (frontCamera) {
            builder.setFacing(CameraSource.CAMERA_FACING_FRONT);
        } else {
            builder.setFacing(CameraSource.CAMERA_FACING_BACK);
        }

        cameraSource = builder.build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraPreview != null) {
            cameraPreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraPreview != null) {
            cameraPreview.release();
        }
    }

    protected void startCamera() throws SecurityException {
        if (Camera.getNumberOfCameras() == 0) {
            return;
        }

        if (cameraSource != null) {
            try {
                cameraPreview.start(cameraSource);
            } catch (IOException e) {
                e.printStackTrace();
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    /**
     * Switch camera front or back
     *
     * @param isFront
     */
    private void switchCamera(boolean isFront) {
        // Re-open camera
        cameraPreview.stop();
        cameraSource.release();
        cameraSource = null;

        createCameraSource(isFront);
        startCamera();
    }

    /**
     *
     */
    class MyBarcodeTracker extends Tracker<Barcode> {
        @Override
        public void onUpdate(Detector.Detections<Barcode> detections, final Barcode item) {
            Log.d(TAG, "Read: " + item.displayValue);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtResult.setText("Result:\n\n" + item.displayValue);
                }
            });
        }
    }
}
