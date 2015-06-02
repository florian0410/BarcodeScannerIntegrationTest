package zlisproduction.barcodescannerintegrationtest;

import android.media.Image;
import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian on 01/06/2015.
 */
public class ProductDataDisplayer extends AsyncTask<String, String, JSONObject> {

    private TextView TitleView = null;
    private TextView ImageView = null;
    private TextView CategoriesView = null;
    private String url = null;

    public ProductDataDisplayer(String pUrl, TextView pTitleView, TextView pImageView, TextView pCategoriesView,) {
        url = pUrl;
        TitleView = pTitleView;
        ImageView = pImageView;
        CategoriesView = pCategoriesView;
    }

    @Override
    protected JSONObject doInBackground(String... args) {
        JSONParser jParser = new JSONParser();

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        String title,image,categories,brands,quantity = null;
        try {
            // Getting JSON Array
            // name = json.getJSONArray("contacts");   // Quand démarre par [
            JSONObject p = json.getJSONObject("product");   // quand démarre par { ou quand une seule case

            // Storing  JSON item in a Variable
            title = p.getString("product_name");
            image = p.getString("image_url");
            categories = p.getString("categories");
            brands = p.getString("brands");
            quantity = p.getString("quantity");

            //Set JSON Data in TextView
            TitleView.setText(title +);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
