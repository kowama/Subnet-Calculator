package com.nimina.kowama.calculatornetadmin.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nimina.kowama.calculatornetadmin.R;
import com.nimina.kowama.calculatornetadmin.model.NetworkManager;

public class SubnetFragment extends Fragment {
    private EditText[] mIpAddressEditText;
    private EditText mIpMaskEditText;
    private SeekBar mIpMaskSeekBar;
    private TextView mResNetworkTextView;
    private TextView mResMaskTextView;
    private TextView mResHostsRangeTextView;
    private TextView mResBroadcastTextView;
    private TextView mResNetSizeTextView;
    private TableLayout mResultsTableLayout;

    private void initViews(View rootView){
        mIpAddressEditText = new EditText[4];
        mIpAddressEditText[0] = rootView.findViewById(R.id.ipPartAEditText);
        mIpAddressEditText[1] = rootView.findViewById(R.id.ipPartBEditText);
        mIpAddressEditText[2] = rootView.findViewById(R.id.ipPartCEditText);
        mIpAddressEditText[3] = rootView.findViewById(R.id.ipPartDEditText);
        mIpMaskEditText = rootView.findViewById(R.id.ipMaskEditText);
        mIpMaskSeekBar  = rootView.findViewById(R.id.ipMaskSeekBar);

        mResultsTableLayout    = rootView.findViewById(R.id.resultTableLayout);
        mResNetworkTextView    = rootView.findViewById(R.id.resNetworkTextView);
        mResMaskTextView       = rootView.findViewById(R.id.resMaskTextView);
        mResHostsRangeTextView = rootView.findViewById(R.id.resHostsRangeTextView);
        mResBroadcastTextView  = rootView.findViewById(R.id.resBroadcastTextView);
        mResNetSizeTextView    = rootView.findViewById(R.id.resNetSizeTextView);

        clearAllViews();

        setListenerOnEditableViews();
        ImageButton clearImageButton = rootView.findViewById(R.id.clearImageButton);
        clearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll(v);
            }
        });


    }

    private void setListenerOnEditableViews(){
        for (int i=0; i<mIpAddressEditText.length; i++){
            final int finalI = i;
            mIpAddressEditText[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    /*move cursor when reach 3 char*/
                    if (s.length() >=3){
                        moveCursorToNext(finalI);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(s.length() >0){
                        try {

                            if (s.length()>0 &&Integer.parseInt(s.toString()) < 0 || Integer.parseInt(s.toString()) > 255){
                                mIpAddressEditText[finalI].setTextColor(Color.RED);
                                mResultsTableLayout.setVisibility(View.INVISIBLE);
                            }
                            else{
                                mIpAddressEditText[finalI].setTextColor(Color.BLACK);
                                updateResult();
                            }
                        }catch (NumberFormatException e){
                            Log.e("Exception",Log.getStackTraceString(e));

                        }catch (Exception e){
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }


                    }

                }
            });
            /*move cursor when dot pressed*/
            mIpAddressEditText[i].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_NUMPAD_DOT) {
                        moveCursorToNext(finalI);
                    }
                    return false;
                }
            });
        }

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
                    try {
                        if (Integer.parseInt(s.toString()) < 1 || Integer.parseInt(s.toString()) >30){
                            mIpMaskEditText.setTextColor(Color.RED);
                            mResultsTableLayout.setVisibility(View.INVISIBLE);
                        }else {
                            mIpMaskEditText.setTextColor(Color.BLACK);
                            mIpMaskSeekBar.setProgress(Integer.parseInt(mIpMaskEditText.getText().toString()));
                            updateResult();
                        }

                    }catch (NumberFormatException e){
                        Log.e("Exception",Log.getStackTraceString(e));

                    }catch (Exception e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    mIpMaskEditText.setSelection(mIpMaskEditText.getText().length());
                }
            }
        });

        mIpMaskSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mIpMaskEditText.setText(String.valueOf(progress));
                updateResult();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void moveCursorToNext(int currentIndex){
        EditText next;
        if (currentIndex+1 < mIpAddressEditText.length)
            next = mIpAddressEditText[currentIndex+1];
        else
            next = mIpMaskEditText;

        Selection.setSelection(next.getText(), next.getSelectionStart());
        next.requestFocus();
    }

    private void clearAllViews(){
        mResultsTableLayout.setVisibility(View.INVISIBLE);

        for (final EditText ipPartEditText : mIpAddressEditText){
            ipPartEditText.setText("");
        }
        mIpMaskEditText.setText("");

    }

    private void updateResult(){
        try {
            String ipAddress =  mIpAddressEditText[0].getText().toString()+"."
                    + mIpAddressEditText[1].getText().toString()+"."
                    + mIpAddressEditText[2].getText().toString()+"."
                    + mIpAddressEditText[3].getText().toString()+"/"
                    + mIpMaskEditText.getText().toString();

            NetworkManager.Subnet mIpNetwork = new NetworkManager.Subnet(ipAddress);

            mResNetworkTextView.setText(mIpNetwork.address);
            mResMaskTextView.setText(mIpNetwork.decMask);
            mResHostsRangeTextView.setText(mIpNetwork.range);
            mResBroadcastTextView.setText(mIpNetwork.broadcast);
            mResNetSizeTextView.setText(String.valueOf(mIpNetwork.allocatedSize));

            mResultsTableLayout.setVisibility(View.VISIBLE);
            //** hide Keyboard ?

        }catch (IllegalArgumentException e){
            /*-----expected EXCEPTION -------*/
            mResultsTableLayout.setVisibility(View.INVISIBLE);
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }




    }

    public void clearAll(View view){
        clearAllViews();
        Snackbar.make(view, "Clear", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subnet, container, false);
        initViews(rootView);
        return rootView;
    }
}
