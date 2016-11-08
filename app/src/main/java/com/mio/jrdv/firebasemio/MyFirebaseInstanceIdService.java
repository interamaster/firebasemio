package com.mio.jrdv.firebasemio;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.mio.jrdv.firebasemio.MainActivity.PADREFireBaseUID;
import static com.mio.jrdv.firebasemio.MainActivity.PREFS_NAME;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_EMAIL_PADRE;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_NOMBRE_PADRE;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_PASSWORD_PADRE;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_PadreONinoYaEligio;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_UID_PADRE;

/**
 * Created by joseramondelgado on 05/11/16.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private  DatabaseReference mDatabase;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // TODO: Implement this method to send any registration to your app's servers.
        guardarToken_enPREFYFireBase(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void guardarToken_enPREFYFireBase(String token) {
        // Add custom implementation, as needed.


        //y lo guardamos

        //guardamos el nombre elegido
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        // We need an editor object to make changes
        SharedPreferences.Editor edit = pref.edit();

        //1º)decimos que ya hemos elegido entre niñio o padre

        edit.putBoolean(PREF_PadreONinoYaEligio,true);

        //2º) le damos valores al objeto padre y lo guardamos en PREF
        //Y GUARDAMOS EL VALOR DE PADREFireBaseUID el valor del UID ACTUAL!!!
        //esto es asi al ser un padre si fuera hijo tendria que guardar el suyo luego


        PADREFireBaseUID=token;
        PADRES newPadre= new PADRES("ejemoploemail@gmail.com","Jose Ramon Delgado","password",PADREFireBaseUID,"NO FOTO");



        edit.putString(PREF_NOMBRE_PADRE,newPadre.getNombre());
        edit.putString(PREF_EMAIL_PADRE,newPadre.getEmail());
        edit.putString(PREF_PASSWORD_PADRE,newPadre.getPassword());
        edit.putString(PREF_UID_PADRE,newPadre.getPadrefirebaseuid());

        // Commit the changes
        edit.commit();

        Log.d(TAG, "PADRES  CREADO "+newPadre.getNombre());


        //3º)creamos en la Database de FireBase el padre
        //con userid el nombre del padre:

         mDatabase.child("PADRES").child(newPadre.getNombre()).setValue(newPadre);
        //no desed la raiz
       // mDatabase.child(newPadre.getEmail()).setValue(newPadre);

    }


}
