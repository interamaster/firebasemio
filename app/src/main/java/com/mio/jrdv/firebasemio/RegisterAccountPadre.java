package com.mio.jrdv.firebasemio;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterAccountPadre extends AppCompatActivity {

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
        final EditText _txtmobile = (EditText) findViewById(R.id.txtmobile_reg);
        Button _btnreg = (Button) findViewById(R.id.btn_reg);

        //Register Button Click Event
        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String fullname = _txtfullname.getText().toString();
                String email = _txtemail.getText().toString();
                String pass = _txtpass.getText().toString();
                String mobile = _txtmobile.getText().toString();


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
        });

    }


}
