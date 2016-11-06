package com.mio.jrdv.firebasemio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    private String padrename;


    //para las Pref
    public static final String PREFS_NAME = "MyPrefsFile";
    public static  final String PREF_PadreONinoYaEligio="PadreoHijoYaElegido";

    //para als pref si es padre
    public static  final String PREF_NOMBRE_PADRE="nombrepadre";
    public static  final String PREF_EMAIL_PADRE="emailpadre";
    public static  final String PREF_PASSWORD_PADRE="passwordpadre";
    public static  final String PREF_UID_PADRE="uidpadre";

    //para las pref si es hijo
    public static  final String PREF_NOMBREDEMI_PADRE_ES="nombredemipadre";
    public static  final String PREF_EMAILDEMI_PADRE_ES="emaildemipadre";
    public static  final String PREF_PASSWORDDEMI_PADRE_ES="passworddemipadre";

    //para para poder enviar/recibir push

    public static  String PADREFireBaseUID;
    public static  String FireBaseUID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //En el arranque inicial de tu app, el SDK FCM genera un token de registro para la instancia de app cliente.
        Log.d("FCM", "Instance ID: " + FirebaseInstanceId.getInstance().getToken());

        //y lo guaradmos para poder enviar/recibir push
        FireBaseUID=FirebaseInstanceId.getInstance().getToken();




        mDatabase = FirebaseDatabase.getInstance().getReference();

        //creamos el padre YO asi no px los duplica mejor en añadir hijo metod

       //mDatabase.child("PADRES").push().setValue("YO");


        //esto da ok:
        /*
        Log.d("INFO",mDatabase.child("PADRES").getKey());

        Log.d("INFO",mDatabase.child("PADRES").child("YO NEW").getKey());


        11-04 15:02:05.940 20825-20825/com.mio.jrdv.firebasemio D/INFO: PADRES
        11-04 15:02:05.940 20825-20825/com.mio.jrdv.firebasemio D/INFO: YO NEW
           */



        //En una pref q decida si ya eleigio ser PADRES o HIJO

        //recuperamos el valor PadreoHijoYaElegido

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        boolean PadreoHijoYaElegido=pref.getBoolean(PREF_PadreONinoYaEligio,false);//por defecto aun no

        Log.d("INFO", "PADRES o HIJOS Ya Elegido?: " +  PadreoHijoYaElegido);



        if (!PadreoHijoYaElegido){
            //aun no se eligio entre padre e hijo
            //TODO new activity para elegir

            //de moento sera padre y relleno a mano los datos

            crearPAdreFicticio();


        }





/*


        if ((mDatabase.child("PADRES").child("YO")).getKey().toString()  == String.valueOf("YO NEW")){


            mDatabase.child("PADRES").child("YO").push().child("nombre PADRES").setValue("nombre del padre");

        }

        else{

            mDatabase.child("PADRES").child("YO NEW").push().child("nombre PADRES").setValue("nombre del padre");
        }

*/


        //3º)los recupermoas desde FireBase:

/*
        ValueEventListener MiFireBaseListeener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get item object and use the values to update the UI


                for (DataSnapshot child : dataSnapshot.getChildren()) {


                    PADRES miPadrees = child.getValue(PADRES.class);

                    Log.d("INFO", "El nombre del PADRES es:: " +  miPadrees.getNombre());
                    Log.d("INFO", "El email del PADRES es:: " +  miPadrees.getEmail());
                    Log.d("INFO", "El password del PADRES es:: " +  miPadrees.getPassword());
                    Log.d("INFO", "El UID del PADRES es:: " +  miPadrees.getPadrefirebaseuid());

                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting item failed, log a message
                Log.w(TAG, "todoPost:onCancelled", databaseError.toException());
                // ...
            }
        };
    // mDatabase.addListenerForSingleValueEvent(MiFireBaseListeener);
        mDatabase.addValueEventListener(MiFireBaseListeener);*/



    }




    public void crearPAdreFicticio() {

        //TODO quitar y hacer en new activity


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


        PADREFireBaseUID=FireBaseUID;

        PADRES newPadre= new PADRES("ejemoploemail@gmail.com","Jose Ramon Delgado","password",PADREFireBaseUID);



        edit.putString(PREF_NOMBRE_PADRE,newPadre.getNombre());
        edit.putString(PREF_EMAIL_PADRE,newPadre.getEmail());
        edit.putString(PREF_PASSWORD_PADRE,newPadre.getPassword());
        edit.putString(PREF_UID_PADRE,newPadre.getPadrefirebaseuid());

        // Commit the changes
        edit.commit();

        Log.d(TAG, "PADRES  CREADO "+newPadre.getNombre());


        //3º)creamos en la Database de FireBase el padre
        //con userid el nombre del padre:

       //
        //
         mDatabase.child("PADRES").child(newPadre.getNombre()).setValue(newPadre);

       // addNewPADRE(newPadre);

    }

    public void Añadirhijo(View view) {

        //aqui añadimos un hijo


       // mDatabase.child("PADRES").child("YO").child("HIJOS").push().child("nombre").setValue("NEW HIJO");

        //con Java

        HIJOS newHijo= new HIJOS("ejemoploemail@gmail.com","password","Jose Ramon Delgado","GUSTAVO",FireBaseUID);

        //2º)recupermaos nombre del padre desde PREF
        //TODO tendria q recyuperarlo desde firebase

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String NombredelPadre=pref.getString(PREF_NOMBRE_PADRE,"NONAME");//por defecto aun no

        Log.d("INFO", "El nombre del PADRES es:: " +  NombredelPadre);


        //3º)los recupermoas desde FireBase:


        ValueEventListener MiFireBaseListeener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

              //esto da el debug:
                //  DataSnapshot { key = null, value = {PADRES={Jose Ramon Delgado={email=ejemoploemail@gmail.com, password=password2,
                // HIJOS={GUSTAVO={mipadrees=Jose Ramon Delgado, elEmaildeMiPadreEs=ejemoploemail@gmail.com,
                // fireBaseUID=duk56qcAWJs:APA91bE45MYlqfyxwQer8x8qheGFQuq3kDQRql6L. . . ,
                // elPassworddeMiPadreEs=password, nombreHijo=GUSTAVO}}, nombre=Jose Ramon Delgado,
                // padrefirebaseUID=duk56qcAWJs:APA91bE45MYlqfyxwQer8x8qheGFQuq3kD. . }}} }
                //
                // Get item object and use the values to update the UI
                PADRES miPadrees = dataSnapshot.child("PADRES").child("Jose Ramon Delgado").getValue(PADRES.class);


                Log.d("INFO", "El nombre del PADRES es:: " +  miPadrees.getNombre());
                Log.d("INFO", "El email del PADRES es:: " +  miPadrees.getEmail());
                Log.d("INFO", "El password del PADRES es:: " +  miPadrees.getPassword());
                Log.d("INFO", "El UID del PADRES es:: " +  miPadrees.getPadrefirebaseuid());


                Log.d(TAG, dataSnapshot.getKey() + ":" + dataSnapshot.getValue().toString());

                Log.d("INFO 1", dataSnapshot.child("PADRES").child("PADRES").getKey() + ":" + dataSnapshot.getValue().toString());


                Log.d("INFO 2",dataSnapshot.child("PADRES").toString());
                /*
                 DataSnapshot { key = PADRES, value = {Jose Ramon Delgado=
                 {nombre=Jose Ramon Delgado, email=ejemoploemail@gmail.com, password=password, padrefirebaseuid=d8jCMt8vX4o:APA91bEu0
                 ,
                  Jesus Perez={nombre=Jesus Perez, email=ejemoploemail33@gmail.com, password=password3, padrefirebaseuid= ficticiodadasdasdasseUID}} }


                 */
                Log.d("INFO 3",dataSnapshot.child("PADRES").child("Jose Ramon Delgado").toString());//!!OK
                /*
                 DataSnapshot { key = Jose Ramon Delgado, value = {nombre=Jose Ramon Delgado, email=ejemoploemail@gmail.com, password=password,
                  padrefirebaseuid=d8jCMt8vX4o:APA91bEu0bm1L3y4WkKGI8xG6NGsyYmaXac9Q h_5If0PI1b1TvUfgkq53joNUu_gausu8UH-Hw} }
                 */

                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    Log.d("INFO 4",child.getValue().toString());
                    Log.d("INFO 5",child.getKey().toString());//PADRES




                }


                List<String> lst = new ArrayList<String>(); // Result will be holded Here
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    lst.add(String.valueOf(dsp.getKey())); //add result into array list

                    Log.d("INFO 6", "en el array hay " +  lst.toString());
                }


                Log.e("Count " ,""+dataSnapshot.getChildrenCount());//1 solo PADRES!!!



                Log.d("INFO 7", "en el array hay " +   dataSnapshot.




                //NOW YOU HAVE ARRAYLIST WHICH HOLD RESULTS


                /*
                      Log.e("Count " ,""+dataSnapshot.getChildrenCount());

                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        PADRES newPadre3 = postSnapshot.getValue(PADRES.class);
                        Log.e("Get Data", newPadre3.getNombre());
            */
                    /*
                    PADRES miPadrees2 = child.getValue(PADRES.class);


                    Log.d("INFO", "El nombre del PADRES es:: " +  miPadrees2.getNombre());
                    Log.d("INFO", "El email del PADRES es:: " +  miPadrees2.getEmail());
                    Log.d("INFO", "El password del PADRES es:: " +  miPadrees2.getPassword());
                    Log.d("INFO", "El UID del PADRES es:: " +  miPadrees2.getPadrefirebaseuid());
                    */




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting item failed, log a message
                Log.w(TAG, "todoPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        //mDatabase.addValueEventListener(MiFireBaseListeener);
        //idem pero solo lo chequea una vez

        mDatabase.addListenerForSingleValueEvent(MiFireBaseListeener);





        //5º)creamos el HIJO si los valores que ha metido del padre son correctos


     //   mDatabase.child("PADRES").child(NombredelPadre).child("HIJOS").child(newHijo.getNombreHijo()).setValue(newHijo);

    }

    public void Añadirpadre(View view) {


        PADREFireBaseUID=" ficticiodadasdasdasseUID";

        PADRES newPadre= new PADRES("ejemoploemail33@gmail.com","Jesus Perez","password3",PADREFireBaseUID);




        Log.d(TAG, "PADRES  CREADO "+newPadre.getNombre());


        //3º)creamos en la Database de FireBase el padre
        //con userid el nombre del padre:

       // mDatabase.child("PADRES").child(newPadre.getNombre()).setValue(newPadre);
        mDatabase.child("PADRES").child(newPadre.getNombre()).setValue(newPadre);


        //addNewPADRE(newPadre);

    }




    public void addNewPADRE(PADRES model) {
        /*
        // Create new to-do item at /user/$userid/$itemkey
        String key = mDatabase.child("PADRES").child(model.getNombre()).push().getKey();
        Map<String, Object> todoValues = model.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/PADRES/" + model.getNombre() + "/" + key, todoValues);
        mDatabase.updateChildren(childUpdates);
        */



        //otro:



    }


}
