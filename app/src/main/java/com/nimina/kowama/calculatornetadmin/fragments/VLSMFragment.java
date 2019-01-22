package com.nimina.kowama.calculatornetadmin.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import com.nimina.kowama.calculatornetadmin.R;
import com.nimina.kowama.calculatornetadmin.algorithms.NetworkManager;
import com.nimina.kowama.calculatornetadmin.fragments.dialog.NetConfigDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VLSMFragment extends Fragment implements NetConfigDialog.NetConfigDialogListener {
    private EditText[] mIpAddressEditText;
    private EditText mIpMaskEditText;
    private SeekBar mIpMaskSeekBar;

    private ListView mResultListView;

    private HashMap<String,Integer> mSubNetsMap = new HashMap<>();
    private List<NetworkManager.Subnet> mResultSubNets = new ArrayList<>();
    private String mMajorNetwork;



    private void initViews(View rootView){
        mIpAddressEditText = new EditText[4];
        mIpAddressEditText[0] = rootView.findViewById(R.id.ipPartAEditText);
        mIpAddressEditText[1] = rootView.findViewById(R.id.ipPartBEditText);
        mIpAddressEditText[2] = rootView.findViewById(R.id.ipPartCEditText);
        mIpAddressEditText[3] = rootView.findViewById(R.id.ipPartDEditText);
        mIpMaskEditText = rootView.findViewById(R.id.ipMaskEditText);
        mIpMaskSeekBar  = rootView.findViewById(R.id.ipMaskSeekBar);


        mResultListView = rootView.findViewById(R.id.resultListView);

        clearAllViews();

        ImageButton clearImageButton = rootView.findViewById(R.id.clearImageButton);
        clearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll(v);
            }
        });

        Button netConfigButton = rootView.findViewById(R.id.netConfigButton);
        netConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNetConfigDialog();
            }
        });

    }

    private void openNetConfigDialog(){
        NetConfigDialog netConfigDialog = new NetConfigDialog();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_vlsm, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void applyNetworksMap(HashMap<String, Integer> subNetsMap) {
        mSubNetsMap     = subNetsMap;
        mMajorNetwork = "192.168.44.0/22";
        NetworkManager.checkIfValidNetworkAddress(mMajorNetwork);
        mResultSubNets  = NetworkManager.calcVLSM(mMajorNetwork, mSubNetsMap);
        // TODO: 21/01/2019
        SubnetResultAdapter subnetResultAdapter = new SubnetResultAdapter(getContext(),R.layout.subnet_result_layout,mResultSubNets);
        mResultListView.setAdapter(subnetResultAdapter);
    }

    private class SubnetResultAdapter extends ArrayAdapter<NetworkManager.Subnet>{
        private Context mContext;
        private int mResource;
        private List<NetworkManager.Subnet> mSubNetList = new ArrayList<>();

        public SubnetResultAdapter(Context context, int resource,List<NetworkManager.Subnet> subNetList) {
            super(context, resource, subNetList);
            mContext    = context;
            mResource   = resource;
            mSubNetList = subNetList;

        }

        @Override
        public int getCount() {
            return mSubNetList.size();
        }

        @Override
        public NetworkManager.Subnet getItem(int position) {
            return mSubNetList.get(position);
        }

        @SuppressLint("StringFormatInvalid")
        @NonNull
        @Override
        public View getView(int position,  View convertView,  ViewGroup parent) {
            // TODO: 21/01/2019
            LayoutInflater inflater= LayoutInflater.from(mContext);

            convertView = inflater.inflate(mResource,parent,false) ;
            try {
                TextView currNetworkTextView             = convertView.findViewById(R.id.resNetworkTextView);
                TextView currNetHostsRangeTextView       = convertView.findViewById(R.id.resHostsRangeTextView);
                TextView CurrNetBroadcastTextView        = convertView.findViewById(R.id.resBroadcastTextView);
                TextView currNetSizeRequiredTextView  = convertView.findViewById(R.id.resNetSizeRequiredTextView);
                TextView resNetSizeAllocatedTextView  = convertView.findViewById(R.id.resNetSizeAllocatedTextView);

                currNetworkTextView.setText(getItem(position).decMask+getItem(position).maskCIDR);
                currNetHostsRangeTextView.setText(getItem(position).range);
                CurrNetBroadcastTextView.setText(getItem(position).broadcast);
                int percentage = (int)(((float)getItem(position).neededSize / (float)getItem(position).allocatedSize)*100);
                currNetSizeRequiredTextView.setText(String.format(getResources().getString(R.string.res_net_size_req),getItem(position).neededSize));
                resNetSizeAllocatedTextView.setText(String.format(getResources().getString(R.string.res_net_size_alloc),getItem(position).allocatedSize,percentage));
            // TODO: 22/01/2019   remove
            }catch (Exception e){
                Log.i("Exception ", e.getMessage());

            }


            return convertView;
        }
    }



}
