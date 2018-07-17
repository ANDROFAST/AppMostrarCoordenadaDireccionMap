package com.androidmorefast.pc.appmostrardireccionmapa;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements View.OnClickListener {

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_coordenadas);
        layout.setOnClickListener(this);
        }

@Override
public void onClick(View v) {
        Intent i = new Intent(MainActivity.this, GestionarCoordenada.class);
        startActivity(i);
        }
}