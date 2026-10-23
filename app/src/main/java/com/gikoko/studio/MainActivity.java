package com.gikoko.studio;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private final int bg = Color.rgb(4, 13, 25);
    private final int panel = Color.rgb(9, 29, 43);
    private final int green = Color.rgb(0, 235, 166);
    private LinearLayout content;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(3, 12, 22));
        getWindow().setNavigationBarColor(Color.rgb(3, 12, 22));
        buildUi();
    }

    private GradientDrawable shape(int color, int stroke) {
        GradientDrawable d = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{color, Color.rgb(7, 42, 52), Color.rgb(5, 19, 32)});
        d.setCornerRadius(dp(18));
        d.setStroke(dp(1), stroke);
        return d;
    }
    private int dp(float n) { return (int)(n * getResources().getDisplayMetrics().density + .5f); }

    private TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s); t.setTextSize(size); t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }
    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }
    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(bg);
        LinearLayout page = column();
        page.setPadding(dp(16), dp(14), dp(16), dp(26));
        scroll.addView(page);
        setContentView(scroll);

        TextView brand = text("G.I  ◈  STUDIO", 27, green, true);
        page.addView(brand);
        TextView powered = text("Powered by I KO KO   •   ANDROID DEVELOPMENT ENVIRONMENT", 10, Color.rgb(198, 232, 231), true);
        page.addView(powered);
        View line = new View(this); line.setBackgroundColor(green);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(2));
        lp.topMargin = dp(12); lp.bottomMargin = dp(16); page.addView(line, lp);

        LinearLayout hero = column();
        hero.setPadding(dp(16), dp(18), dp(16), dp(18));
        hero.setBackground(shape(panel, green));
        TextView eyebrow = text("CYBER WORKSPACE  /  MOBILE IDE", 10, green, true);
        hero.addView(eyebrow);
        TextView title = text("Build something
extraordinary.", 26, Color.WHITE, true);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.topMargin = dp(8); hero.addView(title, tp);
        TextView sub = text("A compact development studio designed for your Android phone.", 13, Color.rgb(174, 204, 216), false);
        tp = new LinearLayout.LayoutParams(-1, -2); tp.topMargin = dp(8); hero.addView(sub, tp);
        page.addView(hero);

        TextView section = text("WORKSPACE MODULES", 12, green, true);
        tp = new LinearLayout.LayoutParams(-1, -2); tp.topMargin = dp(22); tp.bottomMargin = dp(10); page.addView(section, tp);

        content = column();
        page.addView(content);
        addModule("01", "PROJECT EXPLORER", "Project files • folder structure", "Open project browser");
        addModule("02", "CODE EDITOR", "Java • XML • Gradle source workspace", "Open code workspace");
        addModule("03", "BUILD CENTER", "Gradle build • APK output • build status", "Build information");
        addModule("04", "DEVICE & LOGS", "Device compatibility • logs • diagnostics", "Diagnostics");
        addModule("05", "SETTINGS", "Theme • editor preferences • about", "Studio settings");

        TextView version = text("G I KO KO STUDIO   •   VERSION 0.0.1", 11, Color.rgb(116, 164, 173), true);
        version.setGravity(Gravity.CENTER);
        tp = new LinearLayout.LayoutParams(-1, dp(48)); tp.topMargin = dp(12); page.addView(version, tp);

        AlphaAnimation anim = new AlphaAnimation(.65f, 1f);
        anim.setDuration(1200); anim.setRepeatMode(AlphaAnimation.REVERSE);
        anim.setRepeatCount(AlphaAnimation.INFINITE);
        brand.startAnimation(anim);
    }

    private void addModule(String num, String title, String desc, String action) {
        LinearLayout card = column();
        card.setPadding(dp(14), dp(13), dp(14), dp(13));
        card.setBackground(shape(panel, Color.rgb(0, 111, 105)));
        LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
        cp.bottomMargin = dp(10); content.addView(card, cp);

        TextView n = text(num + "   /   " + title, 14, green, true);
        card.addView(n);
        TextView d = text(desc, 12, Color.rgb(190, 215, 224), false);
        LinearLayout.LayoutParams dp = new LinearLayout.LayoutParams(-1, -2);
        dp.topMargin = dp(5); card.addView(d, dp);

        Button b = new Button(this);
        b.setText(action.toUpperCase());
        b.setTextSize(10);
        b.setTextColor(Color.rgb(2, 24, 30));
        b.setAllCaps(false);
        b.setBackground(shape(green, green));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(44));
        bp.topMargin = dp(10); card.addView(b, bp);
        b.setOnClickListener(v -> openModule(title));
    }

    private void openModule(String title) {
        if (title.contains("PROJECT")) {
            showMessage("Project Explorer: full file operations are planned for the next iteration.");
        } else if (title.contains("CODE")) {
            showMessage("Code workspace: editor module is the next development step.");
        } else if (title.contains("BUILD")) {
            showMessage("APK builds run through GitHub Actions cloud workflow.");
        } else if (title.contains("DEVICE")) {
            showMessage("Minimum Android 6.0; target Android 9 compatibility baseline.");
        } else {
            showMessage("G I KO KO STUDIO • Version 0.0.1");
        }
    }
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
