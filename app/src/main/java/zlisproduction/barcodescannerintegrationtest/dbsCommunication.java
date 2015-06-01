package zlisproduction.barcodescannerintegrationtest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Florian.S on 30/05/2015.
 */
public class dbsCommunication {

    public static boolean checkNetworkState(Context pContext){
        ConnectivityManager cm = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        }
        return true;
    }
}
