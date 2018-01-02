package com.androguro.usb.horizon;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;


public class AlertDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Context context = getActivity(); // normally 'this' is used but getActivity() gets the activity that the Dialog Fragment is created in.
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.error_text))
                .setMessage(context.getString(R.string.error_message))
                .setPositiveButton(context.getString(R.string.error_ok_button_text), null);


        AlertDialog dialog = builder.create();
                 return dialog;
    }


}
