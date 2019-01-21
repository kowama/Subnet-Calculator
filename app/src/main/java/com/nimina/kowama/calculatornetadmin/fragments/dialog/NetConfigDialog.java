package com.nimina.kowama.calculatornetadmin.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.nimina.kowama.calculatornetadmin.R;

import java.util.HashMap;

public class NetConfigDialog extends DialogFragment {
    HashMap<String, Integer> mSubNetsMap = new HashMap<>(); // [name: size]
    private NetConfigDialog.NetConfigDialogListener mNetConfigDialogListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.net_config_dialog, null);
        builder.setView(view)
                .setTitle(R.string.label_net_conf_d_title)
                .setNegativeButton(R.string.button_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                }).setPositiveButton(R.string.button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 21/01/2019
                        //fill the subnet list and send it
                        mSubNetsMap.put("A",110);
                        mSubNetsMap.put("B",60);
                        mSubNetsMap.put("C",60);
                       mNetConfigDialogListener.applyNetworksMap(mSubNetsMap);
                    }
        });
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mNetConfigDialogListener = (NetConfigDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "Must implement NetConfigDialogListener");
        }
    }
    public interface  NetConfigDialogListener{
        void applyNetworksMap(HashMap<String,Integer> subNetsMap);
    }
}
