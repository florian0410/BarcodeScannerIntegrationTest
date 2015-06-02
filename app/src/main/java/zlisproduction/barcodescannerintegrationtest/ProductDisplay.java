package zlisproduction.barcodescannerintegrationtest;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian.S on 02/06/2015.
 */
public class ProductDisplay  extends Fragment {
    private Context context = null;
    private TextView mTitle = null;
    private TextView mCategories = null;
    private TextView mImage = null;
    private Button mNewScanButton = null;
    private String mScanContent = null;
    private String mScanFormat = null;
    private String SourceURL = "http://fr.openfoodfacts.org/api/v0/produit/";   // source avec code barre manquant
    private String DynamicURL = null;   // String permettant d'avoir une URL changeante


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Layout = inflater.inflate(R.layout.product_info, container, false);


        mTitle = (TextView) Layout.findViewById(R.id.title);
        mCategories = (TextView) Layout.findViewById(R.id.categories);
        mImage = (TextView) Layout.findViewById(R.id.product_picture);
        mNewScanButton = (Button) Layout.findViewById(R.id.start_scan_button);
        mNewScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = IntentIntegrator.forFragment(ProductDisplay.this);
                integrator.setCaptureActivity(CaptureActivityOrientation.class);
                integrator.setOrientationLocked(true);  // verrouillage de l'orientation
                integrator.initiateScan();
            }
        });
        return Layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = new Bundle();
        if(bundle != null) {
            bundle = this.getArguments();
            mScanContent = bundle.getString("Content");
            mScanFormat = bundle.getString("Format");
        }
        DynamicURL  = SourceURL +mScanContent;
        new JSONParse().execute();
    }

    /*
    * Quand on reçevra un résultat, on instanciera un nouveau fragment affichant celui-ci.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String scanContent = null;
        String scanFormat = null;
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(dbsCommunication.checkScanAnswer(scanningResult, context)) {
            if (scanningResult != null) {
                // Récupérer le contenu
                scanContent = scanningResult.getContents();
                // Récupérer le format du barcode lu
                scanFormat = scanningResult.getFormatName();
            }

            // Changement de fragment
            Fragment fragment = new ProductDisplay();

            // Ajout des information supplémentaires scannées
            Bundle bundle = new Bundle();
            bundle.putString("Content", scanContent);
            bundle.putString("Format", scanFormat);
            fragment.setArguments(bundle);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(DynamicURL);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                // Getting JSON Array
                // name = json.getJSONArray("contacts");   // Quand démarre par [
                int status = json.getInt("status");

                // Check detection produit
                if(status == 0){
                    mTitle.setText("Produit non reconnu");
                    mCategories.setText("Code barre: " + mScanContent);
                }
                else {
                    JSONObject c = json.getJSONObject("product");   // quand démarre par { ou quand une seule case

                    // Check si le produit est incomplet au niveau infos
                    String creator = c.getString("creator");
                    if (creator == "null") {
                        mTitle.setText("Produit non Reconnu");
                        mCategories.setText("Code barre :" + mScanContent);
                    }
                    else {
                        // Storing  JSON item in a Variable
                        String title = c.getString("product_name");
                        String image = c.getString("image_url");
                        String categories = c.getString("categories");
                        String brands = c.getString("brands");
                        String quantity = c.getString("quantity");

                        String customTitle = title + " - " + brands + " - " + quantity;
                        //Set JSON Data in TextView
                        mTitle.setText(customTitle);
                        mCategories.setText(categories);
                        mImage.setText(image);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
