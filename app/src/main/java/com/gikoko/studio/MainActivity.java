package com.gikoko.studio;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.apksig.ApkSigner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends Activity {
    private static final int PICK_APK = 101;
    private static final int PICK_KEYSTORE = 102;
    private static final int SAVE_SIGNED_APK = 103;
    private static final int PICK_PLUGIN = 104;

    private final int bg = Color.rgb(4, 13, 25);
    private final int panel = Color.rgb(9, 29, 43);
    private final int green = Color.rgb(0, 235, 166);
    private LinearLayout page;
    private Uri apkUri;
    private Uri keystoreUri;
    private File signedTemp;
    private EditText aliasInput, storePasswordInput, keyPasswordInput;
    private TextView signerStatus;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(3, 12, 22));
        getWindow().setNavigationBarColor(Color.rgb(3, 12, 22));
        showHome();
    }

    private int dp(float n) {
        return (int)(n * getResources().getDisplayMetrics().density + .5f);
    }

    private GradientDrawable shape(int color, int stroke) {
        GradientDrawable d = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{color, Color.rgb(7, 42, 52), Color.rgb(5, 19, 32)});
        d.setCornerRadius(dp(18));
        d.setStroke(dp(1), stroke);
        return d;
    }

    private TextView text(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private void beginPage(String subtitle) {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(bg);
        page = column();
        page.setPadding(dp(16), dp(14), dp(16), dp(26));
        scroll.addView(page);
        setContentView(scroll);

        TextView brand = text("G.I  ◈  STUDIO", 27, green, true);
        page.addView(brand);
        TextView powered = text("Powered by I KO KO   •   " + subtitle, 10,
            Color.rgb(198, 232, 231), true);
        page.addView(powered);

        View line = new View(this);
        line.setBackgroundColor(green);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, dp(2));
        lp.topMargin = dp(12);
        lp.bottomMargin = dp(16);
        page.addView(line, lp);
    }

    private void heading(String s) {
        TextView t = text(s, 20, Color.WHITE, true);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.topMargin = dp(8);
        p.bottomMargin = dp(8);
        page.addView(t, p);
    }

    private void paragraph(String s) {
        TextView t = text(s, 13, Color.rgb(185, 211, 220), false);
        t.setGravity(Gravity.TOP);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.bottomMargin = dp(12);
        page.addView(t, p);
    }

    private Button button(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(12);
        b.setTextColor(Color.rgb(2, 24, 30));
        b.setAllCaps(false);
        b.setBackground(shape(green, green));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, dp(48));
        p.bottomMargin = dp(9);
        page.addView(b, p);
        b.setOnClickListener(v -> action.run());
        return b;
    }

    private EditText field(String hint, boolean secret) {
        EditText e = new EditText(this);
        e.setSingleLine(true);
        e.setTextSize(14);
        e.setHint(hint);
        e.setHintTextColor(Color.rgb(120, 155, 166));
        e.setTextColor(Color.WHITE);
        e.setPadding(dp(12), 0, dp(12), 0);
        e.setBackground(shape(panel, Color.rgb(0, 111, 105)));
        if (secret) e.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        else e.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, dp(50));
        p.bottomMargin = dp(10);
        page.addView(e, p);
        return e;
    }

    private void showHome() {
        beginPage("MOBILE DEVELOPMENT ENVIRONMENT");
        LinearLayout hero = column();
        hero.setPadding(dp(16), dp(18), dp(16), dp(18));
        hero.setBackground(shape(panel, green));
        TextView eyebrow = text("CYBER WORKSPACE  /  VERSION 0.0.1", 10, green, true);
        hero.addView(eyebrow);
        TextView title = text("Build. Sign. Extend.", 26, Color.WHITE, true);
        LinearLayout.LayoutParams tp = new LinearLayout.LayoutParams(-1, -2);
        tp.topMargin = dp(8);
        hero.addView(title, tp);
        TextView sub = text("A mobile-first Android development toolkit.", 13,
            Color.rgb(174, 204, 216), false);
        tp = new LinearLayout.LayoutParams(-1, -2);
        tp.topMargin = dp(8);
        hero.addView(sub, tp);
        page.addView(hero);

        heading("WORKSPACE");
        button("PROJECT EXPLORER", () -> showProjectExplorer());
        button("CODE EDITOR", () -> showEditorInfo());
        button("BUILD CENTER  •  APK OUTPUT", () -> showBuildCenter());
        button("APK SIGNER", () -> showSigner());
        button("PLUGIN MANAGER", () -> showPlugins());
        button("DEVICE & BUILD DIAGNOSTICS", () -> showDiagnostics());
        button("SETTINGS & ABOUT", () -> showAbout());

        TextView footer = text("G I KO KO STUDIO  •  ANDROID 6+  •  ARMV7 COMPATIBILITY TARGET",
            10, Color.rgb(116, 164, 173), true);
        footer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams fp = new LinearLayout.LayoutParams(-1, dp(52));
        fp.topMargin = dp(10);
        page.addView(footer, fp);

        AlphaAnimation anim = new AlphaAnimation(.72f, 1f);
        anim.setDuration(1400);
        anim.setRepeatMode(AlphaAnimation.REVERSE);
        anim.setRepeatCount(AlphaAnimation.INFINITE);
        hero.startAnimation(anim);
    }

    private void showProjectExplorer() {
        beginPage("PROJECT EXPLORER");
        heading("Project Explorer");
        paragraph("Choose a project archive or source file from your phone. This first iteration uses Android's secure file picker and does not scan your storage automatically.");
        button("PICK PROJECT ZIP", () -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.setType("application/zip");
            i.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(i, 201);
        });
        button("BACK TO WORKSPACE", () -> showHome());
    }

    private void showEditorInfo() {
        beginPage("CODE WORKSPACE");
        heading("Code Editor");
        paragraph("Editor foundation is planned for Java, Kotlin, XML, JSON and Gradle with syntax highlighting, tabs, find/replace and project file operations. This screen is a roadmap, not yet a full code editor.");
        button("OPEN PROJECT EXPLORER", () -> showProjectExplorer());
        button("BACK TO WORKSPACE", () -> showHome());
    }

    private void showBuildCenter() {
        beginPage("BUILD CENTER");
        heading("Build Center");
        paragraph("Build APK from Android source projects using the GitHub Actions cloud runner. The current workflow builds this Studio repository; arbitrary imported-project upload is not connected yet.");
        button("OPEN GITHUB ACTIONS", () -> openUrl("https://github.com/gikoko611/G--I-KO-KO-STUDIO/actions"));
        button("OPEN REPOSITORY", () -> openUrl("https://github.com/gikoko611/G--I-KO-KO-STUDIO"));
        button("PICK SOURCE PROJECT ZIP", () -> {
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.setType("application/zip");
            i.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(i, 201);
        });
        button("BACK TO WORKSPACE", () -> showHome());
    }

    private void showSigner() {
        beginPage("LOCAL APK SIGNER");
        heading("APK Signer");
        paragraph("Sign an APK locally using your own JKS or PKCS12 keystore. The APK and signing passwords stay on this device; they are not sent to GitHub or a server. Signing does not modify the app's code or bypass its protections.");
        button("1  •  SELECT APK TO SIGN", () -> pickDocument(PICK_APK, "application/vnd.android.package-archive"));
        button("2  •  SELECT KEYSTORE (.JKS / .P12)", () -> pickDocument(PICK_KEYSTORE, "*/*"));

        aliasInput = field("Keystore alias (required)", false);
        storePasswordInput = field("Keystore password", true);
        keyPasswordInput = field("Private-key password (often same)", true);

        signerStatus = text("Choose the APK and keystore to begin.", 12,
            Color.rgb(185, 211, 220), false);
        LinearLayout.LayoutParams sp = new LinearLayout.LayoutParams(-1, -2);
        sp.topMargin = dp(5);
        sp.bottomMargin = dp(12);
        page.addView(signerStatus, sp);

        button("SIGN APK LOCALLY", () -> signSelectedApk());
        button("BACK TO WORKSPACE", () -> showHome());
    }

    private void pickDocument(int request, String mime) {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.setType(mime);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(i, request);
    }

    private void signSelectedApk() {
        if (apkUri == null || keystoreUri == null) {
            toast("Select both an APK and a keystore first.");
            return;
        }
        String alias = aliasInput.getText().toString().trim();
        String storePass = storePasswordInput.getText().toString();
        String keyPass = keyPasswordInput.getText().toString();
        if (alias.isEmpty() || storePass.isEmpty() || keyPass.isEmpty()) {
            toast("Enter alias and both passwords.");
            return;
        }
        signerStatus.setText("Signing locally… please keep the app open.");
        new Thread(() -> {
            try {
                File inputApk = copyUriToCache(apkUri, "input-to-sign.apk");
                File keyFile = copyUriToCache(keystoreUri, "user-keystore.bin");
                KeyStore ks = loadKeyStore(keyFile, storePass.toCharArray());
                if (!ks.containsAlias(alias)) throw new Exception("Alias not found in keystore.");
                Key key = ks.getKey(alias, keyPass.toCharArray());
                if (!(key instanceof PrivateKey)) throw new Exception("Selected alias has no private key.");
                Certificate[] chain = ks.getCertificateChain(alias);
                if (chain == null || chain.length == 0) throw new Exception("Certificate chain is missing.");
                List<X509Certificate> certs = new ArrayList<>();
                for (Certificate cert : chain) {
                    if (!(cert instanceof X509Certificate)) throw new Exception("Unsupported certificate type.");
                    certs.add((X509Certificate) cert);
                }
                File output = new File(getCacheDir(), "signed-" + System.currentTimeMillis() + ".apk");
                ApkSigner.SignerConfig signerConfig = new ApkSigner.SignerConfig.Builder(
                    "GI-KO-KO-STUDIO", (PrivateKey) key, certs).build();
                new ApkSigner.Builder(java.util.Collections.singletonList(signerConfig))
                    .setInputApk(inputApk)
                    .setOutputApk(output)
                    .setMinSdkVersion(23)
                    .setV1SigningEnabled(true)
                    .setV2SigningEnabled(true)
                    .setV3SigningEnabled(true)
                    .build()
                    .sign();
                signedTemp = output;
                runOnUiThread(() -> {
                    signerStatus.setText("Signed successfully. Choose where to save the output APK.");
                    Intent save = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                    save.addCategory(Intent.CATEGORY_OPENABLE);
                    save.setType("application/vnd.android.package-archive");
                    save.putExtra(Intent.EXTRA_TITLE, "GI-KO-KO-signed.apk");
                    startActivityForResult(save, SAVE_SIGNED_APK);
                });
            } catch (Exception e) {
                String message = e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage();
                runOnUiThread(() -> signerStatus.setText("Signing failed: " + message));
            }
        }).start();
    }

    private KeyStore loadKeyStore(File file, char[] password) throws Exception {
        Exception last = null;
        String[] types = new String[]{"PKCS12", "JKS"};
        for (String type : types) {
            try (InputStream in = new FileInputStream(file)) {
                KeyStore ks = KeyStore.getInstance(type);
                ks.load(in, password);
                return ks;
            } catch (Exception e) {
                last = e;
            }
        }
        throw new Exception("Could not open keystore. Check format and password.", last);
    }

    private File copyUriToCache(Uri uri, String name) throws Exception {
        File outFile = new File(getCacheDir(), name);
        try (InputStream in = getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(outFile)) {
            if (in == null) throw new Exception("Cannot read selected file.");
            byte[] buffer = new byte[8192];
            int n;
            while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
        }
        return outFile;
    }

    private void showPlugins() {
        beginPage("PLUGIN MANAGER");
        heading("Plugin Manager");
        paragraph("Import a plugin manifest JSON, review its metadata and enable or disable it. For safety, this release accepts declarative manifests only; it does not execute arbitrary code from plugin ZIPs.");
        button("IMPORT PLUGIN MANIFEST (.JSON)", () -> pickDocument(PICK_PLUGIN, "application/json"));
        JSONArray plugins = readPlugins();
        if (plugins.length() == 0) {
            paragraph("No plugins installed yet. A manifest must contain id, name and version. Optional fields: description and actions.");
        }
        for (int i = 0; i < plugins.length(); i++) {
            JSONObject plugin = plugins.optJSONObject(i);
            if (plugin == null) continue;
            final String id = plugin.optString("id", "");
            final boolean enabled = plugin.optBoolean("enabled", true);
            LinearLayout card = column();
            card.setPadding(dp(12), dp(12), dp(12), dp(12));
            card.setBackground(shape(panel, Color.rgb(0, 111, 105)));
            TextView title = text(plugin.optString("name", "Unnamed plugin") + "  •  v" +
                plugin.optString("version", "0"), 15, green, true);
            card.addView(title);
            TextView desc = text(plugin.optString("description", "No description supplied."),
                12, Color.rgb(190, 215, 224), false);
            LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(-1, -2);
            dlp.topMargin = dp(6);
            card.addView(desc, dlp);
            TextView state = text(enabled ? "STATUS: ENABLED" : "STATUS: DISABLED",
                11, enabled ? green : Color.LTGRAY, true);
            dlp = new LinearLayout.LayoutParams(-1, -2);
            dlp.topMargin = dp(6);
            card.addView(state, dlp);
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
            cp.bottomMargin = dp(10);
            page.addView(card, cp);
            Button toggle = button(enabled ? "DISABLE PLUGIN" : "ENABLE PLUGIN",
                () -> setPluginEnabled(id, !enabled));
            Button remove = button("REMOVE PLUGIN", () -> removePlugin(id));
        }
        button("BACK TO WORKSPACE", () -> showHome());
    }

    private JSONArray readPlugins() {
        try {
            return new JSONArray(getSharedPreferences("studio_plugins", MODE_PRIVATE)
                .getString("plugins", "[]"));
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    private void savePlugins(JSONArray array) {
        getSharedPreferences("studio_plugins", MODE_PRIVATE).edit()
            .putString("plugins", array.toString()).apply();
    }

    private void importPlugin(Uri uri) {
        try (InputStream in = getContentResolver().openInputStream(uri)) {
            if (in == null) throw new Exception("Could not read manifest.");
            byte[] bytes = readAll(in);
            JSONObject plugin = new JSONObject(new String(bytes, java.nio.charset.StandardCharsets.UTF_8));
            String id = plugin.optString("id", "").trim();
            String name = plugin.optString("name", "").trim();
            String version = plugin.optString("version", "").trim();
            if (id.isEmpty() || name.isEmpty() || version.isEmpty()) {
                throw new Exception("Manifest needs non-empty id, name and version fields.");
            }
            if (!id.matches("[A-Za-z0-9._-]{2,80}")) throw new Exception("Plugin id contains unsupported characters.");
            if (plugin.has("actions") && !(plugin.opt("actions") instanceof JSONArray)) {
                throw new Exception("The actions field must be a JSON array.");
            }
            plugin.put("enabled", true);
            JSONArray old = readPlugins();
            JSONArray next = new JSONArray();
            for (int i = 0; i < old.length(); i++) {
                JSONObject item = old.optJSONObject(i);
                if (item != null && !id.equals(item.optString("id"))) next.put(item);
            }
            next.put(plugin);
            savePlugins(next);
            toast("Plugin manifest imported.");
            showPlugins();
        } catch (Exception e) {
            toast("Plugin import failed: " + e.getMessage());
        }
    }

    private byte[] readAll(InputStream in) throws Exception {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
        return out.toByteArray();
    }

    private void setPluginEnabled(String id, boolean enabled) {
        JSONArray old = readPlugins();
        for (int i = 0; i < old.length(); i++) {
            JSONObject item = old.optJSONObject(i);
            if (item != null && id.equals(item.optString("id"))) {
                try {
                    item.put("enabled", enabled);
                } catch (org.json.JSONException ignored) { }
            }
        }
        savePlugins(old);
        showPlugins();
    }

    private void removePlugin(String id) {
        JSONArray old = readPlugins();
        JSONArray next = new JSONArray();
        for (int i = 0; i < old.length(); i++) {
            JSONObject item = old.optJSONObject(i);
            if (item != null && !id.equals(item.optString("id"))) next.put(item);
        }
        savePlugins(next);
        showPlugins();
    }

    private void showDiagnostics() {
        beginPage("DEVICE & BUILD DIAGNOSTICS");
        heading("Device & Build Diagnostics");
        paragraph("Compatibility baseline: Android 6.0+ (API 23). The current APK is a Java-based Android app and does not bundle native ARM libraries. Build logs and actual device smoke tests are still required before calling a release verified.");
        button("OPEN BUILD LOGS", () -> openUrl("https://github.com/gikoko611/G--I-KO-KO-STUDIO/actions"));
        button("BACK TO WORKSPACE", () -> showHome());
    }

    private void showAbout() {
        beginPage("SETTINGS & ABOUT");
        heading("G I KO KO STUDIO");
        paragraph("Version 0.0.1 • Package com.gikoko.studio\nPowered by I KO KO\nModern Cyber Workspace");
        paragraph("APK signing uses your selected local keystore. Keep a secure backup of your signing key and never share its passwords.");
        button("BACK TO WORKSPACE", () -> showHome());
    }

    private void openUrl(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
            toast("Could not open link.");
        }
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) return;
        Uri uri = data.getData();
        if (requestCode == PICK_APK) {
            apkUri = uri;
            if (signerStatus != null) signerStatus.setText("APK selected. Now select your keystore.");
            toast("APK selected.");
        } else if (requestCode == PICK_KEYSTORE) {
            keystoreUri = uri;
            if (signerStatus != null) signerStatus.setText("Keystore selected. Enter alias and passwords, then sign.");
            toast("Keystore selected.");
        } else if (requestCode == SAVE_SIGNED_APK) {
            if (signedTemp == null) {
                toast("Signed output file is missing.");
                return;
            }
            try (InputStream in = new FileInputStream(signedTemp);
                 OutputStream out = getContentResolver().openOutputStream(uri)) {
                if (out == null) throw new Exception("Cannot open output destination.");
                byte[] buffer = new byte[8192];
                int n;
                while ((n = in.read(buffer)) != -1) out.write(buffer, 0, n);
                if (signerStatus != null) signerStatus.setText("Saved signed APK successfully.");
                toast("Signed APK saved.");
            } catch (Exception e) {
                toast("Could not save signed APK: " + e.getMessage());
            }
        } else if (requestCode == PICK_PLUGIN) {
            importPlugin(uri);
        } else if (requestCode == 201) {
            toast("Project selected: " + uri.getLastPathSegment() +
                ". Arbitrary project cloud-build upload is not connected yet.");
        }
    }
}
