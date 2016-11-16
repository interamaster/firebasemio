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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import static com.mio.jrdv.firebasemio.MainActivity.PREF_EMAIL_PADRE;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_NOMBRE_PADRE;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_PASSWORD_PADRE;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_PadreONinoYaEligio;
import static com.mio.jrdv.firebasemio.MainActivity.PREF_UID_PADRE;

public class RegisterAccountPadre extends AppCompatActivity {

    private  String fullname;
    private  String email;
    private  String pass;
    private String FotoPath;
    private boolean FotoYatomada;

    //para el intent de la cmara
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private ImageView MiFoto;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account_padre);
        //To hide AppBar for fullscreen.
        ActionBar ab = getSupportActionBar();
        ab.hide();


        //Referencing EditText widgets and Button placed inside in xml layout file
        final EditText _txtfullname = (EditText) findViewById(R.id.txtname_reg);
        final EditText _txtemail = (EditText) findViewById(R.id.txtemail_reg);
        final EditText _txtpass = (EditText) findViewById(R.id.txtpass_reg);

        Button _btnreg = (Button) findViewById(R.id.btn_reg);

        //para la foto:

        MiFoto = (ImageView) findViewById(R.id.fotoIamgeviewRegister);



        //pongo 1 timer para que alos 2 seg cambie el icono de PADRE/HIJOpor la camera




        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                MiFoto.setImageResource(R.drawable.camera_icon);

                //lo hacemos animado mejo!!

                final Animation myAnim = AnimationUtils.loadAnimation(RegisterAccountPadre.this, R.anim.bounce); // Use bounce interpolator with amplitude 0.2 and frequency 20
                 BounceInterpolator interpolator = new BounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                MiFoto.startAnimation(myAnim);



            }
        }.start();




        //para iniciar FireBae

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //al principio aun no nhay foto

        FotoYatomada=false;

        //Register Button Click Event
        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean valid = true;



                  fullname = _txtfullname.getText().toString();
                  email = _txtemail.getText().toString();
                  pass = _txtpass.getText().toString();





                //no que chequee de verdad

                if (fullname.isEmpty()  || fullname.length() < 10 || fullname.length() > 40) {
                    _txtfullname.setError("Enter your real name please!!");
                    valid = false;

                } else {
                    _txtfullname.setError(null);
                }


                if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    _txtemail.setError("enter a valid email address");
                    valid = false;

                } else {
                    _txtemail.setError(null);
                }

                if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
                    _txtpass.setError("between 4 and 10 alphanumeric characters");
                    valid = false;
                } else {
                    _txtpass.setError(null);
                }


                if(!FotoYatomada) {
                    valid=false;
                    Log.d("INFO","falta aun la foto");
                }


                if(valid) {

                   // estan los campo ok que aparezca la foto y pulse

                    Log.d("INFO","campos ok listo para foto");

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

                    PADRES newPadre= new PADRES(email,fullname,pass,PADREFireBaseUID,"NO FOTO");


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


                    //2º)guardamos la foto encode64


                    Bitmap bitmap = BitmapFactory.decodeFile(FotoPath);
                    MiFoto.setImageBitmap(bitmap);
                    encodeBitmapAndSaveToFirebase(bitmap);

                    //3º)Salimos!!

                    //finish();
                }

            }
        });

    }

    public void RegistrationOK(){

        //Alert dialog after clicking the Register Account
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAccountPadre.this);
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

        Log.d("INFO","tomando foto");

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
                Log.d("INFO","foto cogida ok");
                FotoYatomada=true;

                //convierto el file en bitmap:


                Bitmap bitmap = BitmapFactory.decodeFile(imageFiles.get(0).getPath());
                MiFoto.setImageBitmap(bitmap);
                //encodeBitmapAndSaveToFirebase(bitmap);

                //guardamos el path en nuestra property

                FotoPath=(imageFiles.get(0).getPath());

                //en vez de asi com aun no se ha creado el PADRE solo ponemos la fot en la ImageView



            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(RegisterAccountPadre.this);
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
