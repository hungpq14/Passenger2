package com.fit.uet.passengerapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by phamtruong on 3/10/17.
 */

public class DialogUtils {
    public static void showAlertDialog(Context c, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setTitle(message);
        alertDialogBuilder.setPositiveButton("OK", listener);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static void showConfirmDialog(Context c, String title, String message, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("BUY", positiveListener);
        alertDialogBuilder.setNegativeButton("CANCEL", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
