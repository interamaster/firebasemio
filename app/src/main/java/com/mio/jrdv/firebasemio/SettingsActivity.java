package com.mio.jrdv.firebasemio;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import top.wefor.circularanim.CircularAnim;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton eligiopadrebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().hide();


        eligiopadrebutton = (ImageButton) findViewById(R.id.parentbutton);

        eligiopadrebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this, RegisterAccountPadre.class);

                ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(SettingsActivity.this, v, v.getTransitionName());
                startActivity(i, options.toBundle());
            }
        });




    }

    public void eligiopadrebutton(View view) {

        //si elegiio padre
        //esto lo he quitado para hacer otr



        CircularAnim.fullActivity(SettingsActivity.this, view)
                .colorOrImageRes(R.color.colorPrimary)
                .go(new CircularAnim.OnAnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(SettingsActivity.this)
                                .toBundle();

                        Intent intent = new Intent(SettingsActivity.this, LoadinActivity.class);
                        startActivity(intent);

                        //acabamos para que vuelva a MainActivity
                        finish();
                    }
                });


    }


    public void eligiohijobutton(View view) {


    }
}
