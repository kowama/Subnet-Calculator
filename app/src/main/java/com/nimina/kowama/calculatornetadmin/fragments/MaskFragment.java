package com.nimina.kowama.calculatornetadmin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.nimina.kowama.calculatornetadmin.R;
import com.nimina.kowama.calculatornetadmin.model.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class MaskFragment extends Fragment {
    private Spinner mMaskSpinner;
    private List<String> mMaskList ;


    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mask, container, false);
        /*init views */
        mMaskSpinner = rootView.findViewById(R.id.maskSpinner);
        mMaskList = new ArrayList<>();
        for (int mask =1; mask<31; mask++){
            mMaskList.add(NetworkManager.toDecMask(mask));
        }
            Context  context= getContext();
            if (context != null){
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mMaskList);
                mMaskSpinner.setAdapter(adapter);
            }else {
                Log.e("ContextERROR","context is null");
            }

        return rootView;
    }
}
