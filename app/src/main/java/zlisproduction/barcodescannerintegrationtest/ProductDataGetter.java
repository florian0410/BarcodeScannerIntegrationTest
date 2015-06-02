package zlisproduction.barcodescannerintegrationtest;

import android.os.AsyncTask;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Florian on 01/06/2015.
 */
public class ProductDataGetter extends AsyncTask<String, String, JSONObject> {

    private String Title = null;
    private String Image = null;
    private String Categories = null;
    private String url = null;
    private String customTitle = null;
    private String Brands = null;
    private String Quantity = null;

    public ProductDataGetter(String pUrl) {
        url = pUrl;
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public String getCategories() {
        return Categories;
    }

    public String getImage() {
        return Image;
    }

    @Override
    protected JSONObject doInBackground(String... args) {
        JSONParser jParser = new JSONParser();

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        /*try {
            // Getting JSON Array
            // name = json.getJSONArray("contacts");   // Quand démarre par [
            JSONObject p = json.getJSONObject("product");   // quand démarre par { ou quand une seule case

            // Storing  JSON item in a Variable
            Title = p.getString("product_name");
            Image = p.getString("image_url");
            Categories = p.getString("categories");
            Brands = p.getString("brands");
            Quantity = p.getString("quantity");

            //Prepare custom Title for display
            customTitle = Title+"-"+Brands+"-"+Quantity;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }*/
        return json;
    }

    @Override
    protected JSONObject onProgressUpdate(JSONObject json) {
        try {
            // Getting JSON Array
            // name = json.getJSONArray("contacts");   // Quand démarre par [
            JSONObject p = json.getJSONObject("product");   // quand démarre par { ou quand une seule case

            // Storing  JSON item in a Variable
            Title = p.getString("product_name");
            Image = p.getString("image_url");
            Categories = p.getString("categories");
            Brands = p.getString("brands");
            Quantity = p.getString("quantity");

            //Prepare custom Title for display
             customTitle = Title+"-"+Brands+"-"+Quantity;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
