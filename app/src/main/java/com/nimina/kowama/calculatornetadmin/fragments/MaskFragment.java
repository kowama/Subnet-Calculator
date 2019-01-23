package com.nimina.kowama.calculatornetadmin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.nimina.kowama.calculatornetadmin.R;
import com.nimina.kowama.calculatornetadmin.model.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class MaskFragment extends Fragment {
    private Spinner mMaskSpinner;
    private SeekBar mMaskSeekBar;
    private TextView mResMaskTextView;
    private TextView mRsWildcardMaskTextView;
    private TextView mResMaskCIDRTextView;
    private TextView mResNetSizeTextView;
    private TextView mResMaskBinTextView;
    private TextView mResWildcardMaskBinTextView;



    private void updateResultView(int mask){

        mResMaskTextView.setText(NetworkManager.toDecMask(mask));
        mRsWildcardMaskTextView.setText(NetworkManager.toDecWildCardMask(mask));
        mResMaskCIDRTextView.setText(getString(R.string.slash_mask, mask));
        mResNetSizeTextView.setText(String.valueOf(NetworkManager.findUsableHosts(mask)));
        mResMaskBinTextView.setText(Integer.toBinaryString(NetworkManager.toIntMask(mask)));
        mResWildcardMaskBinTextView.setText(Integer.toBinaryString(NetworkManager.toIntWildcardMask(mask)));
    }

    private void initViews(View rootView){
        mMaskSpinner                 = rootView.findViewById(R.id.maskSpinner);
        mMaskSeekBar                 = rootView.findViewById(R.id.maskSeekBar);

        mResMaskTextView             = rootView.findViewById(R.id.resMaskTextView);
        mRsWildcardMaskTextView      = rootView.findViewById(R.id.resWildcardMaskTextView);
        mResMaskCIDRTextView         = rootView.findViewById(R.id.resMaskCIDRTextView);
        mResNetSizeTextView          = rootView.findViewById(R.id.resNetSizeTextView);
        mResMaskBinTextView          = rootView.findViewById(R.id.resMaskBinTextView);
        mResWildcardMaskBinTextView  = rootView.findViewById(R.id.resWildcardMaskBinTextView);


        List<String> mMaskList = new ArrayList<>();
        for (int mask =0; mask<33; mask++){
            mMaskList.add(NetworkManager.toDecMask(mask));
        }
        Context  context= getContext();
        if (context != null){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, mMaskList);
            mMaskSpinner.setAdapter(adapter);
            /*set listener,  bind progressBar and Spinner*/
            setupViewsListener();
            /*set default mask*/
            mMaskSeekBar.setProgress(24);
        }

    }

    private void setupViewsListener(){
        mMaskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMaskSeekBar.setProgress(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mMaskSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMaskSpinner.setSelection(progress);
                updateResultView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater  inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mask, container, false);
        initViews(rootView);
        return rootView;
    }
}
