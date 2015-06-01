package zlisproduction.barcodescannerintegrationtest;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Button mScannerAccess = null;
    private Context context = null;
    public static TextView mTextResult;
    private String url = "http://fr.openfoodfacts.org/api/v0/produit/";
    JSONArray name = null;
    private static String title = null;

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
        mTextResult = (TextView) Layout.findViewById(R.id.ScanResult);

        mScannerAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Toast toast = null;
        if (scanningResult != null) {
            // Récupérer le contenu
            String scanContent = scanningResult.getContents();
            // Récupérer le format du barcode lu
            String scanFormat = scanningResult.getFormatName();
            url = url + scanContent;
        } else {
            toast = Toast.makeText(context.getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();

        }
        if (dbsCommunication.checkNetworkState(context)) {
            toast = Toast.makeText(context.getApplicationContext(),
                    "Connexion internet OK", Toast.LENGTH_SHORT);
            new JSONParse().execute();
        } else {
            toast = Toast.makeText(context.getApplicationContext(),
                    "Connexion internet indisponible", Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    /*private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                // Getting JSON Array
                // name = json.getJSONArray("contacts");   // Quand démarre par [
                JSONObject c = json.getJSONObject("product");   // quand démarre par { ou quand une seule case

                // Storing  JSON item in a Variable
                title = c.getString("product_name");

                //Set JSON Data in TextView
                mTextResult.setText("Nom du produit: " + title);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}


