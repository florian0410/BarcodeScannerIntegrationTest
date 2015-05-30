package zlisproduction.barcodescannerintegrationtest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeReader;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    Button mScannerAccess = null;
    Context context = null;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Layout = inflater.inflate(R.layout.fragment_main, container, false);

        mScannerAccess = (Button) Layout.findViewById(R.id.scanner_button);

        mScannerAccess.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                IntentIntegrator integrator = IntentIntegrator.forFragment(MainActivityFragment.this);
                integrator.setCaptureActivity(CaptureActivityOrientation.class);
                integrator.setOrientationLocked(true);  // verrouillage de l'orientation
                integrator.initiateScan();
            }
        });
        return Layout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,data);
        if (scanningResult != null) {
            // Récupérer le contenu
            String ScanContent = scanningResult.getContents();
            // Récupérer le format du barcode lu
            String scanFormat = scanningResult.getFormatName();
        }
        else{
            Toast toast = Toast.makeText(context.getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

}
