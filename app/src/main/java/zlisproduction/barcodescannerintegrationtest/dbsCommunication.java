package zlisproduction.barcodescannerintegrationtest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentResult;

/**
 * Created by Florian.S on 30/05/2015.
 */
public class dbsCommunication {

    private static Toast toast = null;

    public static boolean checkNetworkState(Context pContext){
        ConnectivityManager cm = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        }
        return true;
    }
    /*
     *  Vérifie si le scan réalisé renvoi bien une information
     */
    public static boolean checkScan(IntentResult pResult, Context pContext){
        if (pResult == null) {
            toast = Toast.makeText(pContext.getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    /*
      * Regarde l'état du réseau et si le code barre est valide
      * @param : pResult, le résultat du scan
      * @param pContext : Le contexte de l'appli
      * @return : Si oui, retourne true
     */

    public static boolean checkScanAnswer(IntentResult pResult,Context pContext){
        if (dbsCommunication.checkNetworkState(pContext)) {
            toast = Toast.makeText(pContext.getApplicationContext(),
                    "Connexion internet OK", Toast.LENGTH_SHORT);
            if(checkScan(pResult,pContext) == false){
                return false;
            }
        } else {
            toast = Toast.makeText(pContext.getApplicationContext(),
                    "Connexion internet indisponible", Toast.LENGTH_SHORT);
            return false;
        }
        toast.show();
        return true;
    }
}
