package com.nimina.kowama.calculatornetadmin.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nimina.kowama.calculatornetadmin.R;
import com.nimina.kowama.calculatornetadmin.algorithms.NetworkManager;
import com.nimina.kowama.calculatornetadmin.fragments.dialog.NetConfigDialog;

import java.util.HashMap;
import java.util.List;

public class VLSMFragment extends Fragment implements NetConfigDialog.NetConfigDialogListener {
    private EditText[] mIpAddressEditText;
    private EditText mIpMaskEditText;
    private SeekBar mIpMaskSeekBar;
    private TextView mResNetworkTextView;
    private TextView mResMaskTextView;
    private TextView mResHostsRangeTextView;
    private TextView mResBroadcastTextView;
    private TextView mResNetSizeTextView;
    private TableLayout mResultsTableLayout;

    private HashMap<String,Integer> mSubNetsMap = new HashMap<>();
    private List<NetworkManager.Subnet> mResultSubnets;



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
        mResultsTableLayout.setVisibility(View.INVISIBLE);

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
        mSubNetsMap = subNetsMap;
        // TODO: 21/01/2019
        //vlsm operattion
        Toast.makeText(getContext(),mSubNetsMap.toString(),Toast.LENGTH_SHORT).show();
    }
}
