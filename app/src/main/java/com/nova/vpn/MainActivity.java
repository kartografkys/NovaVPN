
package com.nova.vpn;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    private final int bg = Color.rgb(8, 5, 13);
    private final int pink = Color.rgb(255, 35, 135);
    private final int cyan = Color.rgb(0, 230, 255);
    private TextView status;
    private Button connect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(24, 32, 24, 24);
        root.setBackgroundColor(bg);

        TextView title = new TextView(this);
        title.setText("ИЗОТОП");
        title.setTextColor(pink);
        title.setTextSize(34);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("V P N  •  СВОБОДА В КАЖДОМ ИЗОТОПЕ");
        subtitle.setTextColor(cyan);
        subtitle.setTextSize(11);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        TextView character = new TextView(this);
        character.setText("\nКАТЯ\n\nТВОЙ ИНТЕРНЕТ.\nТВОИ ПРАВИЛА.\n");
        character.setTextColor(Color.WHITE);
        character.setTextSize(22);
        character.setGravity(Gravity.CENTER);
        character.setBackgroundColor(Color.rgb(25, 8, 24));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(
            -1, 0, 1
        );
        cp.setMargins(0, 22, 0, 12);
        root.addView(character, cp);

        OrbitView orbit = new OrbitView();
        root.addView(orbit, new LinearLayout.LayoutParams(
            -1, 210
        ));

        status = new TextView(this);
        status.setText("● НЕ ПОДКЛЮЧЕНО");
        status.setTextColor(Color.LTGRAY);
        status.setTextSize(15);
        status.setGravity(Gravity.CENTER);
        root.addView(status);

        connect = new Button(this);
        connect.setText("ИМПОРТИРОВАТЬ КОНФИГ");
        connect.setTextColor(Color.WHITE);
        connect.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(pink)
        );
        root.addView(connect);

        TextView footer = new TextView(this);
        footer.setText("\n◉  БЕЗОПАСНОСТЬ     ◉  ИЗОТОП VPN");
        footer.setTextColor(cyan);
        footer.setGravity(Gravity.CENTER);
        footer.setTextSize(11);
        root.addView(footer);

        connect.setOnClickListener(v -> {
            android.content.Intent intent =
                new android.content.Intent(
                    android.content.Intent.ACTION_OPEN_DOCUMENT
                );
            intent.setType("*/*");
            intent.addCategory(
                android.content.Intent.CATEGORY_OPENABLE
            );
            startActivityForResult(intent, 10);
        });

        setContentView(root);
    }

    class OrbitView extends View {
        Paint p = new Paint(3);

        OrbitView() {
            super(MainActivity.this);
        }

        @Override
        protected void onDraw(Canvas c) {
            super.onDraw(c);
            float x = getWidth() / 2f;
            float y = getHeight() / 2f;

            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(3);
            p.setColor(pink);
            c.drawCircle(x, y, 67, p);

            p.setColor(cyan);
            c.save();
            c.rotate(35, x, y);
            c.drawOval(new RectF(x - 115, y - 32,
                x + 115, y + 32), p);
            c.restore();

            p.setColor(pink);
            c.save();
            c.rotate(-35, x, y);
            c.drawOval(new RectF(x - 115, y - 32,
                x + 115, y + 32), p);
            c.restore();

            p.setStyle(Paint.Style.FILL);
            p.setColor(pink);
            c.drawCircle(x + 85, y - 43, 7, p);

            p.setColor(cyan);
            c.drawCircle(x - 90, y + 40, 7, p);

            p.setColor(Color.WHITE);
            p.setTextAlign(Paint.Align.CENTER);
            p.setTextSize(38);
            c.drawText("⏻", x, y + 12, p);

            p.setTextSize(13);
            c.drawText("ИЗОТОП", x, y + 100, p);
        }
    }
}
