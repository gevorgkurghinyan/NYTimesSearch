package com.gevkurg.nytimessearch.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.gevkurg.nytimessearch.R;

import java.io.IOException;

/**
 * Created by gkurghin on 9/22/17.
 */

public class Helper {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            final java.lang.Process process = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = process.waitFor();
            return (exitValue == 0);
        }catch (IOException ex){
            ex.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showSnackBarForInternetConnection(View view, Context context) {
        Snackbar.make(view,
                R.string.no_internet_text,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.snackbar_internet_action_text,
                        getSnackBarActionOnClickListener(context))
                .setActionTextColor(Color.YELLOW)
                .setDuration(5000)
                .show();
    }

    public static void showSnackBar(View view, Context context, int resourceId) {
        Snackbar.make(view,
                resourceId,
                Snackbar.LENGTH_INDEFINITE)
                .setDuration(5000)
                .show();
    }

    private static View.OnClickListener getSnackBarActionOnClickListener(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(settingsIntent);
            }
        };
    }
}
