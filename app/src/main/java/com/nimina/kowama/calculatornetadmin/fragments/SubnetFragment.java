package com.nimina.kowama.calculatornetadmin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nimina.kowama.calculatornetadmin.algorithms.IpNetwork;
import com.nimina.kowama.calculatornetadmin.R;

public class SubnetFragment extends Fragment {
    private EditText[] mIpAddressEditText;
    private EditText mIpMaskEditText;
    private SeekBar mIpMaskSeekBar;
    private TextView mSubnetResultTextView;
    private IpNetwork mIpNetwork;

    private void updateResult(){
        try {
            mIpNetwork = new IpNetwork(Integer.parseInt(mIpAddressEditText[0].getText().toString()),
                    Integer.parseInt(mIpAddressEditText[1].getText().toString()),
                    Integer.parseInt(mIpAddressEditText[2].getText().toString()),
                    Integer.parseInt(mIpAddressEditText[3].getText().toString()),
                    Integer.parseInt(mIpMaskEditText.getText().toString()));

            String result = String.format(getResources().getString(R.string.subnet_result),
                    mIpNetwork.networkAddressDec(),
                    mIpNetwork.subnetMaskDec(),
                    mIpNetwork.Hosts(),
                    mIpNetwork.broadcastAddressDec(),
                    mIpNetwork.availableHost(),
                    mIpNetwork.networkAddressBin(),
                    mIpNetwork.subnetMaskBin(),
                    mIpNetwork.firstHostBin(),
                    mIpNetwork.lastHostBin(),
                    mIpNetwork.broadcastAddressBin());
            mSubnetResultTextView.setText(result);
        }catch (IllegalArgumentException e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subnet, container, false);
        mIpAddressEditText = new EditText[4];
        mIpAddressEditText[0] = rootView.findViewById(R.id.ipPartAEditText);
        mIpAddressEditText[1] = rootView.findViewById(R.id.ipPartBEditText);
        mIpAddressEditText[2] = rootView.findViewById(R.id.ipPartCEditText);
        mIpAddressEditText[3] = rootView.findViewById(R.id.ipPartDEditText);
        mIpMaskEditText = rootView.findViewById(R.id.ipMaskEditText);
        mIpMaskSeekBar  = rootView.findViewById(R.id.ipMaskSeekBar);

        mSubnetResultTextView = rootView.findViewById(R.id.subnetResultTextView);

        for (final EditText ipPartEditText : mIpAddressEditText)
            ipPartEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() >0){
                        if (s.length()>0 &&Integer.parseInt(s.toString()) < 0 || Integer.parseInt(s.toString()) > 255)
                            ipPartEditText.setTextColor(Color.RED);
                        else
                            ipPartEditText.setTextColor(Color.BLACK);
                            updateResult();
                    }

                }
            });

        mIpMaskEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if(s.length() > 0){
                   if (Integer.parseInt(s.toString()) < 1 || Integer.parseInt(s.toString()) >30){
                       mIpMaskEditText.setTextColor(Color.RED);
                   }else {
                       mIpMaskEditText.setTextColor(Color.BLACK);
                       mIpMaskSeekBar.setProgress(Integer.parseInt(mIpMaskEditText.getText().toString()));
                       updateResult();
                   }
                   mIpMaskEditText.setSelection(mIpMaskEditText.getText().length());
               }

            }
        });

        mIpMaskSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mIpMaskEditText.setText(Integer.toString(progress));
                updateResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        return rootView;
    }
}
