package com.nimina.kowama.calculatornetadmin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nimina.kowama.calculatornetadmin.R;
import com.nimina.kowama.calculatornetadmin.model.NetworkManager;

import java.util.ArrayList;
import java.util.List;

public class MaskFragment extends Fragment {
    private Spinner mMaskSpinner;
    private SeekBar mMaskSeekBar;
    private List<String> mMaskList ;
    private TableLayout mResultTableLayout;
    private TextView mResMaskTextView;
    private TextView mRsWildcardMaskTextView;
    private TextView mResMaskCIDRTextView;
    private TextView mResNetSizeTextView;
    private TextView mResMaskBinTextView;
    private TextView mResWildcardMaskBinTextView;



    private void updateResultView(int maskCIDR){

        mResMaskTextView.setText(NetworkManager.toDecMask(maskCIDR));
        mRsWildcardMaskTextView.setText(NetworkManager.convertIpToQuartet(32-maskCIDR));
        mResMaskCIDRTextView.setText("/"+String.valueOf(maskCIDR));
        mResNetSizeTextView.setText(String.valueOf(Math.pow(2,32 - maskCIDR)-1));
        mResMaskBinTextView.setText(Integer.toBinaryString(maskCIDR));
        mResWildcardMaskBinTextView.setText(Integer.toBinaryString(32-maskCIDR));
    }
    /*init views */
    private void initViews(View rootView){
        mMaskSpinner                 = rootView.findViewById(R.id.maskSpinner);
        mResultTableLayout           = rootView.findViewById(R.id.resultTableLayout);
        mMaskSeekBar                 = rootView.findViewById(R.id.maskSeekBar);

        mResMaskTextView             = rootView.findViewById(R.id.resMaskTextView);
        mRsWildcardMaskTextView      = rootView.findViewById(R.id.resWildcardMaskTextView);
        mResMaskCIDRTextView         = rootView.findViewById(R.id.resMaskCIDRTextView);
        mResNetSizeTextView          = rootView.findViewById(R.id.resNetSizeTextView);
        mResMaskBinTextView          = rootView.findViewById(R.id.resMaskBinTextView);
        mResWildcardMaskBinTextView  = rootView.findViewById(R.id.resWildcardMaskBinTextView);


        mMaskList = new ArrayList<>();
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
