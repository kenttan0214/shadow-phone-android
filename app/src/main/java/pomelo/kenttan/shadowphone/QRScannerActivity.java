package pomelo.kenttan.shadowphone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import pomelo.kenttan.shadowphone.util.Config;

public class QRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = QRScannerActivity.class.getSimpleName();
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.v(TAG, rawResult.getText()); // Prints scan results
        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);

        Intent intent = new Intent(QRScannerActivity.this, DevicesListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Config.QR_TAG, rawResult.getText());
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
