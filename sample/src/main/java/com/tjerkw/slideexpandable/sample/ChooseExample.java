package com.tjerkw.slideexpandable.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseExample extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_activity);

        findViewById(R.id.bt_example).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseExample.this, ExampleActivity.class));
            }
        });

        findViewById(R.id.bt_example_listener).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseExample.this, ExampleAnimListenerActivity.class));
            }
        });
    }
}
