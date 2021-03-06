package com.kowama.subnetcalculator.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kowama.subnetcalculator.MainActivity;
import com.kowama.subnetcalculator.R;
import com.kowama.subnetcalculator.model.NetworkManager;
import com.kowama.subnetcalculator.fragments.dialog.NetConfigDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VLSMFragment extends Fragment implements NetConfigDialog.NetConfigDialogListener {
    private EditText[] mIpAddressEditText;
    private EditText mIpMaskEditText;
    private SeekBar mIpMaskSeekBar;

    private ListView mResultListView;

    private HashMap<String,Integer> mSubNetsMap;
    private List<NetworkManager.Subnet> mResultSubNets;
    private String mMajorNetwork;


    private void initViews(View rootView){
        mIpAddressEditText = new EditText[4];
        mIpAddressEditText[0] = rootView.findViewById(R.id.ipPartAEditText);
        mIpAddressEditText[1] = rootView.findViewById(R.id.ipPartBEditText);
        mIpAddressEditText[2] = rootView.findViewById(R.id.ipPartCEditText);
        mIpAddressEditText[3] = rootView.findViewById(R.id.ipPartDEditText);
        mIpMaskEditText = rootView.findViewById(R.id.ipMaskEditText);
        mIpMaskSeekBar  = rootView.findViewById(R.id.ipMaskSeekBar);


        //init some var
        mSubNetsMap  = new HashMap<>();
        mResultSubNets = new ArrayList<>();

        mResultListView = rootView.findViewById(R.id.resultListView);

        clearAllViews();

        setListenerOnEditableViews(rootView);

    }
    private void setListenerOnEditableViews(View rootView){
        /* ip Part A.B.C.D EditTextListener */
        for (int i=0; i<mIpAddressEditText.length; i++) {
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
                    try {
                        if(s.length() >0){
                            if (s.length()>0 &&Integer.parseInt(s.toString()) < 0 || Integer.parseInt(s.toString()) > 255){
                                mIpAddressEditText[finalI].setTextColor(Color.RED);
                            }
                            else{
                                mIpAddressEditText[finalI].setTextColor(Color.BLACK);
                                //OK
                            }
                        }
                    }catch (NumberFormatException e){
                        Log.e("Exception",Log.getStackTraceString(e));

                    }catch (Exception e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

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
        /*ip Mask /xx EditTextListener */

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
                        }else {
                            mIpMaskEditText.setTextColor(Color.BLACK);
                            mIpMaskSeekBar.setProgress(Integer.parseInt(mIpMaskEditText.getText().toString()));
                            //OK
                        }
                        mIpMaskEditText.setSelection(mIpMaskEditText.getText().length());
                    }catch (NumberFormatException e){
                        Log.e("Exception",Log.getStackTraceString(e));


                    }catch (Exception e){
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });

        /* ip Mask /xx EditText binding with the SeekBar */
        mIpMaskSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mIpMaskEditText.setText(String.valueOf(progress));
                //OK
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*Clear  ButtonListener */
        ImageButton clearImageButton = rootView.findViewById(R.id.clearImageButton);
        clearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll(v);
            }
        });

        /* OpenDialog  ButtonListener */

        Button netConfigButton = rootView.findViewById(R.id.netConfigButton);
        netConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMajorNetwork = mIpAddressEditText[0].getText().toString()+"."
                        + mIpAddressEditText[1].getText().toString()+"."
                        + mIpAddressEditText[2].getText().toString()+"."
                        + mIpAddressEditText[3].getText().toString()+"/"
                        + mIpMaskEditText.getText().toString();
                if(NetworkManager.checkIfValidNetworkAddress(mMajorNetwork)){
                    openNetConfigDialog();
                }else Snackbar.make(view, "Major Network Address not valid", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                MainActivity.hideSoftKeyboard(getActivity());
            }
        });
    }
    private void openNetConfigDialog(){
        NetConfigDialog netConfigDialog = new NetConfigDialog();
        assert getFragmentManager() != null;
        netConfigDialog.show(getFragmentManager(),getString(R.string.net_conf_dialog));
        netConfigDialog.setTargetFragment(VLSMFragment.this,1);
    }
    private void clearAllViews(){
        for (final EditText ipPartEditText : mIpAddressEditText){
            ipPartEditText.setText("");
        }
        mIpMaskEditText.setText("");
    }
    public void clearAll(View view){
        clearAllViews();
        Snackbar.make(view, "Clear", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        mResultListView.setVisibility(View.INVISIBLE);
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
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vlsm, container, false);
        initViews(rootView);
        return rootView;
    }
    @Override
    public void applyNetworksMap(HashMap<String, Integer> subNetsMap) {
        mSubNetsMap     = subNetsMap;
        mResultSubNets  = NetworkManager.calcVLSM(mMajorNetwork, mSubNetsMap);
        SubnetResultAdapter subnetResultAdapter = new SubnetResultAdapter(getContext(),R.layout.subnet_result_layout,mResultSubNets);
        mResultListView.setAdapter(subnetResultAdapter);
        mResultListView.setVisibility(View.VISIBLE);

    }
    private class SubnetResultAdapter extends ArrayAdapter<NetworkManager.Subnet>{
        private Context mContext;
        private int mResource;
        private List<NetworkManager.Subnet> mSubNetList;

        SubnetResultAdapter(Context context, int resource, List<NetworkManager.Subnet> subNetList) {
            super(context, resource, subNetList);
            mContext    = context;
            mResource   = resource;
            mSubNetList = subNetList;

        }

        @Override
        public int getCount() {
            return mSubNetList.size();
        }

        @NonNull
        @Override
        public NetworkManager.Subnet getItem(int position) {
            return mSubNetList.get(position);
        }

        @SuppressLint({"StringFormatInvalid", "ViewHolder"})
        @NonNull
        @Override
        public View getView(int position, View convertView, @NotNull ViewGroup parent) {
            LayoutInflater inflater= LayoutInflater.from(mContext);

            convertView = inflater.inflate(mResource,parent,false) ;
            try {
                TextView curResNetworkNameTextView       = convertView.findViewById(R.id.resNetworkNameTextView);
                TextView currNetworkTextView             = convertView.findViewById(R.id.resNetworkTextView);
                TextView currNetHostsRangeTextView       = convertView.findViewById(R.id.resHostsRangeTextView);
                TextView CurrNetBroadcastTextView        = convertView.findViewById(R.id.resBroadcastTextView);
                TextView currNetSizeRequiredTextView     = convertView.findViewById(R.id.resNetSizeRequiredTextView);
                TextView resNetSizeAllocatedTextView     = convertView.findViewById(R.id.resNetSizeAllocatedTextView);

                curResNetworkNameTextView.setText(getItem(position).name);
                currNetworkTextView.setText(Html.fromHtml((getString(R.string.res_network,getItem(position).address,getItem(position).maskCIDR))));
                currNetHostsRangeTextView.setText(getItem(position).range);
                CurrNetBroadcastTextView.setText(getItem(position).broadcast);
                int percentage = (int)(((float)getItem(position).neededSize / (float)getItem(position).allocatedSize)*100);
                currNetSizeRequiredTextView.setText(Html.fromHtml(getString(R.string.res_net_size_req,getItem(position).neededSize)));
                resNetSizeAllocatedTextView.setText(Html.fromHtml(getString(R.string.res_net_size_alloc,getItem(position).allocatedSize,percentage)));
            }catch (NullPointerException e){
                Log.e("Exception", Log.getStackTraceString(e));
            }
            catch (Exception e){
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

            }
            return convertView;
        }
    }



}
