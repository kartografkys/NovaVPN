
package com.nova.vpn;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

import com.wireguard.android.backend.GoBackend;
import com.wireguard.android.backend.Tunnel;
import com.wireguard.config.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    private GoBackend backend;
    private Config config;
    private boolean connected = false;
    private TextView status;
    private Button connect;

    private static final int PICK_CONFIG = 10;
    private static final int VPN_PERMISSION = 11;

    private final Tunnel tunnel = new Tunnel() {
        @Override
        public String getName() {
            return "Izotop";
        }

        @Override
        public void onStateChange(State state) {
            runOnUiThread(() -> {
                connected = state == State.UP;
                status.setText(connected
                    ? "● ПОДКЛЮЧЕНО"
                    : "● НЕ ПОДКЛЮЧЕНО");
                connect.setText(connected
                    ? "ОТКЛЮЧИТЬСЯ"
                    : "ПОДКЛЮЧИТЬСЯ");
            });
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backend = new GoBackend(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(28, 30, 28, 30);
        root.setBackgroundColor(Color.rgb(7, 13, 29));

        TextView title = new TextView(this);
        title.setText("ИЗОТОП");
        title.setTextColor(Color.WHITE);
        title.setTextSize(32);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        status = new TextView(this);
        status.setText("● НЕ ПОДКЛЮЧЕНО");
        status.setTextColor(Color.LTGRAY);
        status.setTextSize(18);
        status.setGravity(Gravity.CENTER);
        root.addView(status);

        Button importButton = new Button(this);
        importButton.setText("ИМПОРТИРОВАТЬ КОНФИГ");
        root.addView(importButton);

        connect = new Button(this);
        connect.setText("ПОДКЛЮЧИТЬСЯ");
        root.addView(connect);

        TextView info = new TextView(this);
        info.setText("Выбери файл WireGuard .conf");
        info.setTextColor(Color.LTGRAY);
        info.setGravity(Gravity.CENTER);
        root.addView(info);

        importButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, PICK_CONFIG);
        });

        connect.setOnClickListener(v -> {
            if (config == null) {
                Toast.makeText(this,
                    "Сначала импортируй конфиг",
                    Toast.LENGTH_LONG).show();
                return;
            }

            if (connected) {
                setVpnState(Tunnel.State.DOWN);
                return;
            }

            Intent permission = GoBackend.VpnService.prepare(this);
            if (permission != null) {
                startActivityForResult(permission, VPN_PERMISSION);
            } else {
                setVpnState(Tunnel.State.UP);
            }
        });

        setContentView(root);
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONFIG &&
                resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        getContentResolver().openInputStream(uri)))) {
                config = Config.parse(reader);
                Toast.makeText(this,
                    "Конфигурация импортирована",
                    Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                config = null;
                Toast.makeText(this,
                    "Ошибка конфигурации: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == VPN_PERMISSION) {
            if (resultCode == RESULT_OK) {
                setVpnState(Tunnel.State.UP);
            } else {
                Toast.makeText(this,
                    "Разрешение VPN не предоставлено",
                    Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setVpnState(Tunnel.State state) {
        new Thread(() -> {
            try {
                backend.setState(tunnel, state, config);
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this,
                    "Ошибка VPN: " + e.getMessage(),
                    Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
