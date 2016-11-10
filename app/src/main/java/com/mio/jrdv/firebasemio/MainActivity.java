package com.mio.jrdv.firebasemio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

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

    //para el intent de la cmara
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private ImageView MiFoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //para la foto:

          MiFoto = (ImageView) findViewById(R.id.fotoIamgeview);

        //si existe la rcupermaos de firebase:



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

            //TODO de moemnto a Settings
            // Intent intent = new Intent(LoadinActivity.this, DashboardActivity.class);
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

            startActivity(intent);





        }







        chequeargoogleplay();



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

                    Log.d("INFO","desde on create y recupernadode Firebas:");
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
          //  mDatabase.addListenerForSingleValueEvent(MiFireBaseListeener);


*/


                  final String NombredelPadre=pref.getString(PREF_NOMBRE_PADRE,"NONAME");//por defecto aun no

            Log.d("INFO", "El nombre del PADRES recueprado desde Pref:: en oncreate " +  NombredelPadre);

            if (!NombredelPadre.equals("NONAME")) {

                mDatabase.child("PADRES").child(NombredelPadre).addListenerForSingleValueEvent(


                        // mDatabase.child("PADRES").child(NombredelPadre).child("HIJOS").child(NombredelPadre).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //dataSnapshot.getRef().setValue(null);
                                //  HIJOS hijonew = dataSnapshot.child("PADRES").child(NombredelPadre).child("HIJOS").child(NombredelPadre).getValue(HIJOS.class);


                                PADRES miPadrees = dataSnapshot.getValue(PADRES.class);

                                Log.d("INFO","desde on create y recupernadode Firebas:");
                                Log.d("INFO", "El nombre del PADRES es:: " +  miPadrees.getNombre());
                                Log.d("INFO", "El email del PADRES es:: " +  miPadrees.getEmail());
                                Log.d("INFO", "El password del PADRES es:: " +  miPadrees.getPassword());
                                Log.d("INFO", "El UID del PADRES es:: " +  miPadrees.getPadrefirebaseuid());
                                Log.d("INFO", "El   PADRES  TIENE O NO FOTO:: " +  miPadrees.getFotoencoded64());



                                //solo recupera iagen si la tiene!!!!!
                                if (!miPadrees.getFotoencoded64().equals("NO FOTO")) {

                                    Bitmap imageBitmap = null;
                                    try {
                                        imageBitmap = decodeFromFirebaseBase64(miPadrees.getFotoencoded64());

                                        MiFoto.setImageBitmap(imageBitmap);//no mejor redonda:

                                        /*
                                        //TODO lo hago con una libreria:
                                        https://github.com/siyamed/android-shape-imageview

                                        RoundedBitmapDrawable img = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);

                                        //asi con un radio

                                        // img.setCornerRadius(150.0f);


                                        //asi es circular perfecta
                                        img.setCornerRadius(Math.min(img.getMinimumWidth(), img.getMinimumHeight())/2.0f);

                                        MiFoto.setImageDrawable(img);

                                        */


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }


                                }




                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                            }
                        });



            }








    }

    private void chequeargoogleplay() {

        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        switch(result) {
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Log.d(TAG,"SERVICE_VERSION_UPDATE_REQUIRED");
                break;
            case ConnectionResult.SUCCESS:
                Log.d(TAG, "Play service available success");
                break;
            default:
                Log.d(TAG, "unknown services result: " + result);

        }
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

       //
        //
         mDatabase.child("PADRES").child(newPadre.getNombre()).setValue(newPadre);
        //en vez de asi directamente de la riaz:

       // mDatabase.child(newPadre.getEmail()).setValue(newPadre);

       // addNewPADRE(newPadre);

    }

    public void Añadirhijo(View view) {

        //aqui añadimos un hijo


       // mDatabase.child("PADRES").child("YO").child("HIJOS").push().child("nombre").setValue("NEW HIJO");

        //con Java

        HIJOS newHijo= new HIJOS("gusemaila@gmail.com","password ","Jose Ramon Delgado","GUSTAVO",FireBaseUID);

        //2º)recupermaos nombre del padre desde PREF
        //TODO tendria q recyuperarlo desde firebase

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        final String NombredelPadre=pref.getString(PREF_NOMBRE_PADRE,"NONAME");//por defecto aun no

        Log.d("INFO", "El nombre del PADRES recueprado desde Pref:: " +  NombredelPadre);


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
              //  PADRES miPadrees = dataSnapshot.child("PADRES").child("Jose Ramon Delgado").getValue(PADRES.class);

                //en luigar de escribir a mano el nombre del padre com  child le metemos el que heos recuperado dede pref!!
                //YA SI CADA PADRE SOLO VERA A SUS HIJOS!!!!!

                PADRES miPadrees = dataSnapshot.child("PADRES").child(NombredelPadre).getValue(PADRES.class);


                Log.d("INFO", "El nombre del PADRES es:: " +  miPadrees.getNombre());
                Log.d("INFO", "El email del PADRES es:: " +  miPadrees.getEmail());
                Log.d("INFO", "El password del PADRES es:: " +  miPadrees.getPassword());
                Log.d("INFO", "El UID del PADRES es:: " +  miPadrees.getPadrefirebaseuid());




                //PARA LE HIJO SI QUE TENGO QUE RECUPERARLO DESDE FIREBASE SI NO EXISTE EN PREFS








               // Log.d(TAG, dataSnapshot.getKey() + ":" + dataSnapshot.getValue().toString());

               // Log.d("INFO 1", dataSnapshot.child("PADRES").child("PADRES").getKey() + ":" + dataSnapshot.getValue().toString());


               //Log.d("INFO 2",dataSnapshot.child("PADRES").toString());
                /*
                 DataSnapshot { key = PADRES, value = {Jose Ramon Delgado=
                 {nombre=Jose Ramon Delgado, email=ejemoploemail@gmail.com, password=password, padrefirebaseuid=d8jCMt8vX4o:APA91bEu0
                 ,
                  Jesus Perez={nombre=Jesus Perez, email=ejemoploemail33@gmail.com, password=password3, padrefirebaseuid= ficticiodadasdasdasseUID}} }


                 */
              //  Log.d("INFO 3",dataSnapshot.child("PADRES").child("Jose Ramon Delgado").toString());//!!OK
                /*
                 DataSnapshot { key = Jose Ramon Delgado, value = {nombre=Jose Ramon Delgado, email=ejemoploemail@gmail.com, password=password,
                  padrefirebaseuid=d8jCMt8vX4o:APA91bEu0bm1L3y4WkKGI8xG6NGsyYmaXac9Q h_5If0PI1b1TvUfgkq53joNUu_gausu8UH-Hw} }
                 */
                            /*
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

                            */

               // Log.d("INFO 7", "en el array hay " +   dataSnapshot.




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


      // mDatabase.child("PADRES").child(NombredelPadre).child("HIJOS").child(newHijo.getNombreHijo()).setValue(newHijo);

        //COMO NO TENGO NOI DE COMO SABER LE NOMBRE DEL HIJO!!LE PONGO EL MISMO NOMBRE DEL PADRE EN LA RAIZ
        mDatabase.child("PADRES").child(NombredelPadre).child("HIJOS").child(newHijo.getNombreHijo()).setValue(newHijo);

        //6º)chcequeamosn que lo crea ok:





    }

    public void Añadirpadre(View view) {


        PADREFireBaseUID=" ficticiodadasdasdasseUID";

        PADRES newPadre= new PADRES("ejemoploemail33@gmail.com","Jesus Perez","password3",PADREFireBaseUID,"NO FOTO");




        Log.d(TAG, "PADRES  CREADO "+newPadre.getNombre());


        //3º)creamos en la Database de FireBase el padre
        //con userid el nombre del padre:

       mDatabase.child("PADRES").child(newPadre.getNombre()).setValue(newPadre);
       // mDatabase.child("PADRES").child(newPadre.getNombre()).setValue(newPadre);

        //en vez d eso direcamente desde la raiz:
        //mDatabase.child(newPadre.getEmail()).setValue(newPadre);


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


    public void VerMisHijos(View view) {



        //1º)recupermaos nombre del padre desde PREF


        SharedPreferences pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        final String NombredelPadre=pref.getString(PREF_NOMBRE_PADRE,"NONAME");//por defecto aun no

        Log.d("INFO", "El nombre del PADRES recueprado desde Pref:: " +  NombredelPadre);



        //2º)recuepramos el nombre de los hijs desde FireBase(cuando sea el padre sera lo mismo)



        mDatabase.child("PADRES").child(NombredelPadre).child("HIJOS").orderByChild("mipadrees").equalTo(NombredelPadre).addListenerForSingleValueEvent(


       // mDatabase.child("PADRES").child(NombredelPadre).child("HIJOS").child(NombredelPadre).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //dataSnapshot.getRef().setValue(null);
                      //  HIJOS hijonew = dataSnapshot.child("PADRES").child(NombredelPadre).child("HIJOS").child(NombredelPadre).getValue(HIJOS.class);



                        //para ver todos los HIJOS:

                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            Log.i("HIJOS: ", child.getKey());

                            HIJOS hijonew = child.getValue(HIJOS.class);

                            Log.d("INFO", "El nombre del padre del HIJO es:: " +  hijonew.getMipadrees());
                            Log.d("INFO", "El nombre del HIJO es:: " +  hijonew.getNombreHijo());
                            Log.d("INFO", "El email del HIJO es:: " +  hijonew.getElemaildemipadrees());
                            Log.d("INFO", "El password del HIJO es:: " +  hijonew.getElpassworddemipadrees());
                            Log.d("INFO", "El UID del HIJO es:: " +  hijonew.getFirebaseuid());
                        }



                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TodoApp", "getUser:onCancelled", databaseError.toException());
                    }
                });



    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////FOTOS////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void hacerfoto(View view) {

        /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        */
        //lo hacems mejor con:

        EasyImage.openChooserWithGallery(this, "CHOOSE PICTURE", 0);

    }


    //AQUI RECIBIMOS EL INTENT EXTRA CON LA FOTO HECHA

    /*
    //MEJOR CON LA CLASS EASYIMAGE..
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            MiFoto.setImageBitmap(imageBitmap);
            encodeBitmapAndSaveToFirebase(imageBitmap);
        }
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                //onPhotosReturned(imageFiles);
                Log.d("INFO","foto cogida ok");

                //convierto el file en bitmap:


                Bitmap bitmap = BitmapFactory.decodeFile(imageFiles.get(0).getPath());
                MiFoto.setImageBitmap(bitmap);
                encodeBitmapAndSaveToFirebase(bitmap);

            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(MainActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });
    }

    //METODO PARA CODIFICAR Y SUBIR A FIREBASE  LA FOTO NUESTRA/HIJO

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
       /*
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(SyncStateContract.Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(mRestaurant.getPushId())
                .child("imageUrl");
        ref.setValue(imageEncoded);

        */

        //MIO:

        //1º)recupermaos nombre del padre desde PREF


        SharedPreferences pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        final String NombredelPadre=pref.getString(PREF_NOMBRE_PADRE,"NONAME");//por defecto aun no

        Log.d("INFO", "El nombre del PADRES recueprado desde Pref:: " +  NombredelPadre);
        mDatabase.child("PADRES").child(NombredelPadre).child("fotoencoded64").setValue(imageEncoded);
    }



    //PARA DECODIFCAR LA IMAGEN(STRING )DEVUELTA POR FIREBASE Y CONVERTIRLA EN BITMAP

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////FOTOS////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
