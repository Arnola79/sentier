package com.sentier.rando;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.core.content.FileProvider;
import androidx.core.content.pm.PackageInfoCompat;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/** Mise à jour de l'application : télécharge le nouvel APK publié sur GitHub puis ouvre l'installateur Android. */
@CapacitorPlugin(name = "ApkUpdater")
public class ApkUpdaterPlugin extends Plugin {

    // Seuls les APK publiés dans les releases du dépôt sont acceptés
    private static final String ALLOWED_PREFIX = "https://github.com/Arnola79/sentier/releases/";

    @PluginMethod
    public void info(PluginCall call) {
        try {
            Context ctx = getContext();
            PackageInfo p = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            JSObject r = new JSObject();
            r.put("build", PackageInfoCompat.getLongVersionCode(p));
            r.put("version", p.versionName);
            call.resolve(r);
        } catch (Exception e) {
            call.reject("Version inconnue");
        }
    }

    @PluginMethod
    public void install(final PluginCall call) {
        final String url = call.getString("url");
        if (url == null || !url.startsWith(ALLOWED_PREFIX) || !url.endsWith(".apk")) {
            call.reject("Adresse de mise à jour refusée");
            return;
        }
        new Thread(new Runnable() {
            @Override public void run() {
                final File f;
                try {
                    f = download(url);
                } catch (Exception e) {
                    call.reject("Téléchargement impossible : " + e.getMessage());
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        try {
                            Context ctx = getContext();
                            JSObject r = new JSObject();
                            if (Build.VERSION.SDK_INT >= 26 && !ctx.getPackageManager().canRequestPackageInstalls()) {
                                Intent s = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:" + ctx.getPackageName()));
                                s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                ctx.startActivity(s);
                                r.put("status", "permission");
                                call.resolve(r);
                                return;
                            }
                            Uri uri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".fileprovider", f);
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setDataAndType(uri, "application/vnd.android.package-archive");
                            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(i);
                            r.put("status", "installing");
                            call.resolve(r);
                        } catch (Exception e) {
                            call.reject("Installation impossible : " + e.getMessage());
                        }
                    }
                });
            }
        }).start();
    }

    private File download(String start) throws IOException {
        File dir = new File(getContext().getCacheDir(), "updates");
        if (!dir.exists() && !dir.mkdirs()) throw new IOException("dossier inaccessible");
        File f = new File(dir, "sentier-update.apk");
        String url = start;
        HttpURLConnection c = null;
        for (int hop = 0; hop < 6; hop++) {
            c = (HttpURLConnection) new URL(url).openConnection();
            c.setInstanceFollowRedirects(false);
            c.setConnectTimeout(10000);
            c.setReadTimeout(30000);
            c.setRequestProperty("User-Agent", "Sentier-app");
            int code = c.getResponseCode();
            if (code / 100 == 3) {
                String loc = c.getHeaderField("Location");
                c.disconnect();
                if (loc == null || !loc.startsWith("https://")) throw new IOException("redirection invalide");
                url = loc;
                continue;
            }
            if (code != 200) throw new IOException("HTTP " + code);
            break;
        }
        if (c == null) throw new IOException("connexion impossible");
        InputStream in = c.getInputStream();
        FileOutputStream o = new FileOutputStream(f);
        try {
            byte[] buf = new byte[65536];
            int n;
            while ((n = in.read(buf)) > 0) o.write(buf, 0, n);
        } finally {
            o.close();
            in.close();
            c.disconnect();
        }
        if (f.length() < 500000) throw new IOException("fichier incomplet");
        return f;
    }
}
