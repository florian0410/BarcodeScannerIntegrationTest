package zlisproduction.barcodescannerintegrationtest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian.S on 02/06/2015.
 */
public class ProductDisplay  extends Fragment {
    private Context context = null;
    private static TextView mTitle = null;
    private static TextView mCategories = null;
    private static TextView mImage = null;
    private String mScanContent = null;
    private String mScanFormat = null;
    private String url = "http://fr.openfoodfacts.org/api/v0/produit/";


    private String Title = null;
    private String Image = null;
    private String Categories = null;
    private String customTitle = null;
    private String Brands = null;
    private String Quantity = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity.getApplicationContext();
    }

    public static void setmTitleContent(String pTitle) {
        mTitle.setText(pTitle);
    }

    public static void setmCategoriesContent(String pCategories) {
        mCategories.setText(pCategories);
    }

    public static void setmImageContent(String pImage) {
        mImage.setText(pImage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Layout = inflater.inflate(R.layout.product_info, container, false);


        mTitle = (TextView) Layout.findViewById(R.id.title);
        mCategories = (TextView) Layout.findViewById(R.id.categories);
        mImage = (TextView) Layout.findViewById(R.id.product_picture);

        return Layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle bundle = new Bundle();
        if(bundle  != null) {
            bundle = this.getArguments();
            mScanContent = bundle.getString("Content");
            mScanFormat = bundle.getString("Format");
        }
        url = url+mScanContent;
        new JSONParse().execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
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
                Title = c.getString("product_name");
                Image = c.getString("image_url");
                Categories = c.getString("categories");
                Brands = c.getString("brands");
                Quantity = c.getString("quantity");

                customTitle = Title+" - "+Brands+" - "+Quantity;
                //Set JSON Data in TextView
                mTitle.setText(customTitle);
                mCategories.setText(Categories);
                mImage.setText(Image);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
