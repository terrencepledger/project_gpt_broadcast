package org.gptchurch.broadcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.gptchurch.broadcast.ui.broadcastPage.BroadcastLogActivity;
import org.gptchurch.broadcast.ui.emailPage.EmailActivity;
import org.gptchurch.broadcast.ui.sermonsPage.SermonsActivity;

public class MainActivity extends AppCompatActivity {
//TODO add settings page
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButtons();

    }

    public void connectButtons() {

        findViewById(R.id.emailPageMenuBtn).setOnClickListener(
                v -> startActivityForResult(
                        new Intent(v.getContext(), EmailActivity.class),
                        0
                )
        );

        findViewById(R.id.sermonsPageMenuBtn).setOnClickListener(
                v -> startActivityForResult(
                        new Intent(v.getContext(), SermonsActivity.class),
                        0
                )
        );

        findViewById(R.id.broadcastPageMenuBtn).setOnClickListener(
                v -> startActivityForResult(
                        new Intent(v.getContext(), BroadcastLogActivity.class),
                        0
                )
        );

    }

}