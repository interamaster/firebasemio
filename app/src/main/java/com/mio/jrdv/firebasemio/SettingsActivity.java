package com.mio.jrdv.firebasemio;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import top.wefor.circularanim.CircularAnim;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().hide();


    }

    public void eligiopadrebutton(View view) {

        //si elegiio padre



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
