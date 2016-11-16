package com.mio.jrdv.firebasemio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static android.content.ContentValues.TAG;
import static com.mio.jrdv.firebasemio.MainActivity.FireBaseUID;
import static com.mio.jrdv.firebasemio.MainActivity.PADREFireBaseUID;
import static com.mio.jrdv.firebasemio.MainActivity.PREFS_NAME;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_EMAILDEMI_PADRE_ES;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_NOMBREDEMI_PADRE_ES;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_NOMBRE_PADRE;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_PASSWORDDEMI_PADRE_ES;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_PadreONinoYaEligio;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_UID_DE_MI_PADRE_ES;

public class RegisterAccountHijo extends AppCompatActivity {


    private String fullname;
    private String nameParent;
    private String pass;
    private String FotoPath;
    private boolean FotoYatomada;

    //para el intent de la cmara
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private ImageView MiFotoPadre;
    private ImageView MiFotoHijo;

    private DatabaseReference mDatabase;


    //para guardar lka info del padre

    private PADRES MiPadreChequeadoES;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account_hijo);
        //To hide AppBar for fullscreen.
        ActionBar ab = getSupportActionBar();
        ab.hide();


        //Referencing EditText widgets and Button placed inside in xml layout file
        final EditText ChildrenName = (EditText) findViewById(R.id.txtnamechildren_reg);
        final EditText CheckFullNameParent = (EditText) findViewById(R.id.txtname_check_children);
        final EditText CheckPassParent = (EditText) findViewById(R.id.txtpass_check_children);

        Button BotonCheckParent = (Button) findViewById(R.id.btn_check_parent);
        Button BotonCheckChildren = (Button) findViewById(R.id.btn_reg_children);

        //para la foto:

        MiFotoPadre = (ImageView) findViewById(R.id.fotoParentIamgeviewRegister);

        MiFotoHijo = (ImageView) findViewById(R.id.fotochildrenIamgeviewRegister);

        //pongo 1 timer para que alos 2 seg cambie el icono de PADRE/HIJOpor la camera


        new CountDownTimer(1000, 1000) {//delay 1 seg y tarda 1 seg

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                MiFotoHijo.setImageResource(R.drawable.camera_icon);

                //lo hacemos animado mejo!!

                final Animation myAnim = AnimationUtils.loadAnimation(RegisterAccountHijo.this, R.anim.bounce); // Use bounce interpolator with amplitude 0.2 and frequency 20
                BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                MiFotoHijo.startAnimation(myAnim);


            }
        }.start();


        //para iniciar FireBae

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //al principio aun no nhay foto

        FotoYatomada = false;

        //Check Parent  Button Click Event
        BotonCheckParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valid = true;


                nameParent = CheckFullNameParent.getText().toString();
                pass = CheckPassParent.getText().toString();


                //no que chequee de verdad


                if (nameParent.isEmpty() || nameParent.length() < 10 || nameParent.length() > 40) {
                    CheckFullNameParent.setError("enter a real  full Name");
                    valid = false;

                } else {
                    CheckFullNameParent.setError(null);
                }

                if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
                    CheckPassParent.setError("between 4 and 10 alphanumeric characters");
                    valid = false;
                } else {
                    CheckPassParent.setError(null);
                }


                if (valid) {

                    // estan los campo ok que aparezca la foto y pulse

                    Log.d("INFO", "campos ok listo para bajar foto padre y activar campos de registro hijo");


                    //1º)chequeamos que ese padre Fullname con ese password existen en FireBase:


                    //2º)recuepramos el nombre de los hijs desde FireBase(cuando sea el padre sera lo mismo)


                    mDatabase.child("PADRES").addListenerForSingleValueEvent(


                            // mDatabase.child("PADRES").child(NombredelPadre).child("HIJOS").child(NombredelPadre).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //dataSnapshot.getRef().setValue(null);
                                    //  HIJOS hijonew = dataSnapshot.child("PADRES").child(NombredelPadre).child("HIJOS").child(NombredelPadre).getValue(HIJOS.class);

                                    //para ver todos los PADRES:

                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                       // Log.i("PADRES: ", child.getKey());


                                        //PADRES miPadrees = dataSnapshot.child("PADRES").child(nameParent).getValue(PADRES.class);

                                        PADRES miPadrees = child.getValue(PADRES.class);

                                       // Log.d("INFO", "El nombre del PADRES es:: " + miPadrees.getNombre());
                                       // Log.d("INFO", "El email del PADRES es:: " + miPadrees.getEmail());
                                       // Log.d("INFO", "El password del PADRES es:: " + miPadrees.getPassword());
                                       // Log.d("INFO", "El UID del PADRES es:: " + miPadrees.getPadrefirebaseuid());


                                        if (miPadrees.getNombre().equalsIgnoreCase( nameParent)) {


                                            Log.d("INFO", "El nombre del PADRES CORRECTO ES:: " + miPadrees.getNombre());
                                            //le doy al vzlor a la property

                                            MiPadreChequeadoES=miPadrees;

                                            //ya se eleigio vamos a funcion para poner foto animada del padre y activar campos de
                                            //registro del niño

                                            //chequeamos el password
                                                    if ((miPadrees.getPassword().equals(pass))){

                                                        PadreChequeadoOK();

                                                    }
                                                    else{

                                                        Log.d("INFO","Parent passsord   wrong");
                                                        //TODO avisar de que esta mal el password
                                                    }



                                            /*
                                            11-16 11:55:20.839 32741-32741/com.mio.jrdv.firebasemio I/PADRES:: Jose Ramon Delgado
                                            11-16 11:55:20.839 32741-32741/com.mio.jrdv.firebasemio D/INFO: El nombre del PADRES es:: Jose Ramon Delgado
                                            11-16 11:55:20.839 32741-32741/com.mio.jrdv.firebasemio D/INFO: El email del PADRES es:: ejemoploemail@gmail.com
                                            11-16 11:55:20.839 32741-32741/com.mio.jrdv.firebasemio D/INFO: El password del PADRES es:: password
                                            11-16 11:55:20.849 32741-32741/com.mio.jrdv.firebasemio D/INFO: El UID del PADRES es:: cv_8692zjmA:APA91bHIDIPzwpqKynI46x1Sy63cQrJsXC6-bkVP3xqUU_AVEJJCGpklB1ZZLiWEkQfCGTEechP4cgdjZmzvqAuxvMZS3q-AgCa1TTGvFX02P-lkJCs8eVR0_iBNDUQ3_k-I23cAu2wH
                                            11-16 11:55:20.849 32741-32741/com.mio.jrdv.firebasemio D/INFO: El nombre del PADRES CORRECTO ES:: Jose Ramon Delgado
                                            11-16 11:55:20.849 32741-32741/com.mio.jrdv.firebasemio D/ContentValues: PADRES  CREADO Jose Ramon Delgado

                                             */


                                        }



                                    }


                                    if (MiPadreChequeadoES==null){
                                        Log.d("INFO","Parent name   wrong");
                                        //TODO avisar de que esta mal el nombre


                                    }

                                }
                                    @Override
                                    public void onCancelled (DatabaseError databaseError){
                                        // Getting item failed, log a message
                                        Log.w(TAG, "todoPost:onCancelled", databaseError.toException());
                                        // ...
                                    }
                                }

                                );



                            }

                }
            }

            );


            //Reg Childre  Button Click Event
            BotonCheckChildren.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View view){

                //TODO
                boolean valid = true;

                fullname = ChildrenName.getText().toString();


                if (fullname.isEmpty() || fullname.length() < 5 || fullname.length() > 30) {
                    ChildrenName.setError("Enter your real name please!!");
                    valid = false;

                } else {
                    ChildrenName.setError(null);
                }

                if (!FotoYatomada) {
                    valid = false;
                    Log.d("INFO", "falta aun la foto");
                }


                //3º)creamos en la Database de FireBase el HIJO
                //con userid el nombre del padre:

                //
                //
                //TODO  mDatabase.child("PADRES").child(newPadre.getNombre()).setValue(newPadre);


                //2º)guardamos la foto encode64


                Bitmap bitmap = BitmapFactory.decodeFile(FotoPath);
                MiFotoPadre.setImageBitmap(bitmap);
                encodeBitmapAndSaveToFirebase(bitmap);

                //3º)Salimos!!

                //finish();
            }
            }

            );


        }

    private void PadreChequeadoOK() {

        //aqui llegamos despues de saber quien es nuestro PADRE



        //1º)guardamos al PADRE en PREF

        //1º)guardamos al PADRE:

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

        //PADRES newPadre= new PADRES("ejemoploemail@gmail.com","Jose Ramon Delgado","password",PADREFireBaseUID,"NO FOTO");

        //idem con valores reales:




        edit.putString(PREF_NOMBREDEMI_PADRE_ES,MiPadreChequeadoES.getNombre());
        edit.putString(PREF_EMAILDEMI_PADRE_ES,MiPadreChequeadoES.getEmail());
        edit.putString(PREF_PASSWORDDEMI_PADRE_ES,MiPadreChequeadoES.getPassword());
        edit.putString(PREF_UID_DE_MI_PADRE_ES,MiPadreChequeadoES.getPadrefirebaseuid());

        // Commit the changes
        edit.commit();

        Log.d(TAG, "PADRES  CREADO "+MiPadreChequeadoES.getNombre());






        //2º)animamos la foto

        //pongo 1 timer para que alos 2 seg cambie el icono de PADRE/HIJOpor la camera


        new CountDownTimer(1000, 1000) {//delay 1 seg y tarda 1 seg

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                //MiFotoHijo.setImageResource(R.drawable.camera_icon);

                //solo recupera iagen si la tiene!!!!!
                if (!MiPadreChequeadoES.getFotoencoded64().equals("NO FOTO")) {

                    Bitmap imageBitmap = null;
                    try {
                        imageBitmap = decodeFromFirebaseBase64(MiPadreChequeadoES.getFotoencoded64());

                        MiFotoPadre.setImageBitmap(imageBitmap);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

                //lo hacemos animado mejo!!

                final Animation myAnim = AnimationUtils.loadAnimation(RegisterAccountHijo.this, R.anim.bounce); // Use bounce interpolator with amplitude 0.2 and frequency 20
                BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                MiFotoPadre.startAnimation(myAnim);


            }
        }.start();




        //3º)activamos los campos de niño para registrarse




    }

    public void RegistrationOK() {

        //Alert dialog after clicking the Register Account
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAccountHijo.this);
        builder.setTitle("Information");
        builder.setMessage("Your Account is Successfully Created.");
        builder.setPositiveButton("Okey", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Finishing the dialog and removing Activity from stack.
                dialogInterface.dismiss();
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////FOTOS////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void TomarFoto(View view) {

        Log.d("INFO", "tomando foto");

        //  FotoYatomada=true;//no meor en el activityresult qeu asi seguro que si la tiene!!
        EasyImage.openChooserWithGallery(this, "CHOOSE PICTURE", 0);


    }


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
                Log.d("INFO", "foto cogida ok");
                FotoYatomada = true;

                //convierto el file en bitmap:


                Bitmap bitmap = BitmapFactory.decodeFile(imageFiles.get(0).getPath());
                MiFotoPadre.setImageBitmap(bitmap);
                //encodeBitmapAndSaveToFirebase(bitmap);

                //guardamos el path en nuestra property

                FotoPath = (imageFiles.get(0).getPath());

                //en vez de asi com aun no se ha creado el PADRE solo ponemos la fot en la ImageView


            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(RegisterAccountHijo.this);
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

        //MIO:

        //1º)recupermaos nombre del padre desde PREF


        SharedPreferences pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        final String NombredelPadre = pref.getString(PREF_NOMBRE_PADRE, "NONAME");//por defecto aun no

        Log.d("INFO", "El nombre del PADRES recueprado desde Pref:: " + NombredelPadre);
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
