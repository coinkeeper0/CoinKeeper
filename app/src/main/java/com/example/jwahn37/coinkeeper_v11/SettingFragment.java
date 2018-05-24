package com.example.jwahn37.coinkeeper_v11;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.jwahn37.coinkeeper_v11.R;

public class SettingFragment extends DialogFragment {

    Switch set_pred;
    EditText price_up;
    EditText price_down;
    String money;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v;


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v=inflater.inflate(R.layout.setting, null)).
                setCancelable(false)
        //setMessage(R.string.project_name)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        money = price_up.getText().toString();
                        Log.d(money,"money"); //okay
                    }
                });

        set_pred = v.findViewById(R.id.set_prediction);
        price_up = v.findViewById(R.id.set_price_up);
        EditText price_down = v.findViewById(R.id.set_price_down);



        // Create the AlertDialog object and return it
        return builder.create();
    }
}
