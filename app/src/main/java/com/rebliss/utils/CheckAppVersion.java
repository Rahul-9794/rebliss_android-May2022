package com.rebliss.utils;
import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.IOException;

public class CheckAppVersion extends IntentService {
    public CheckAppVersion() {
        super("InterestApp");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String latestVersion;
        String currentVersion = intent.getExtras().getString("currentVersion");
        try {
         String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=" + getPackageName();
            String newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                    .first()
                    .ownText();
            latestVersion = newVersion;
            if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                sendBroadcast(new Intent(CheckAppVersion.this, UpdateReceiver.class));
            }
        } catch (HttpStatusException e) {

        } catch (IOException e) {


        } catch (NullPointerException ex) {
        }
    }


}
