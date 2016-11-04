package com.example.park.weatherapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Park on 16-09-16.
 */
public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.error_title).setMessage("There's an error! Please Try Again!")
                .setPositiveButton("OK", null);

        AlertDialog dialog = builder.create();
        return dialog;

    }
}
