
package com.nova.vpn;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    int blue = Color.rgb(40, 120, 255);
    boolean connected = false;
    TextView status;
    Button connect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(28, 30, 28, 30);
        root.setBackgroundColor(Color.rgb(7, 13, 29));

        TextView title = new TextView(this);
        title.setText("NOVA VPN");
        title.setTextColor(Color.WHITE);
        title.setTextSize(32);
        title.setTypeface(null, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Твоя сеть. Твои правила.");
        subtitle.setTextColor(blue);
        subtitle.setTextSize(16);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        status = new TextView(this);
        status.setText("\n● НЕ ПОДКЛЮЧЕНО\n");
        status.setTextColor(Color.LTGRAY);
        status.setTextSize(18);
        status.setGravity(Gravity.CENTER);
        root.addView(status);

        Spinner countries = new Spinner(this);
        String[] options = {
            "Выбери сервер",
            "Нидерланды",
            "Германия",
            "Финляндия"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            options
        );
        countries.setAdapter(adapter);
        root.addView(countries);

        connect = new Button(this);
        connect.setText("ПОДКЛЮЧИТЬСЯ");
        root.addView(connect);

        TextView note = new TextView(this);
        note.setText(
            "\nNova VPN · Версия 1.0\n" +
            "Для реального подключения нужен сервер."
        );
        note.setTextColor(Color.GRAY);
        note.setGravity(Gravity.CENTER);
        root.addView(note);

        connect.setOnClickListener(v -> {
            Toast.makeText(
                this,
                "Сначала настрой настоящий VPN-сервер.",
                Toast.LENGTH_LONG
            ).show();
        });

        setContentView(root);
    }
}
