package com.nimina.kowama.calculatornetadmin.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.nimina.kowama.calculatornetadmin.R;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class NetConfigDialog extends DialogFragment {
    private HashMap<String, Integer> mSubNetsHashMap = new HashMap<>(); // [name: size]
    private NetConfigDialog.NetConfigDialogListener mNetConfigDialogListener;
    private ListView mSubNetsHashMapListView;
    private FloatingActionButton mAddSubNetFloatingActionButton;


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
                       mNetConfigDialogListener.applyNetworksMap(mSubNetsHashMap);
                    }
        });

        /*** init ***/
        mSubNetsHashMapListView        = view.findViewById(R.id.subNetHashMapListView);
        mAddSubNetFloatingActionButton = view.findViewById(R.id.addSubNetFloatingActionButton);

        /*** listener ***/
        mAddSubNetFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubNetsHashMap.put("Network "+String.valueOf(mSubNetsHashMap.size()+1), null);
                updateSubNetsHashMapListView();
            }
        });

        /**initial subnet **/
        mSubNetsHashMap.put("Network 1",110);
        mSubNetsHashMap.put("Network 2",60);
        mSubNetsHashMap.put("Network 3",60);
        updateSubNetsHashMapListView();

        return builder.create();
    }
    private void updateSubNetsHashMapListView(){
        SubNetHashMapAdapter subNetHashMapAdapter = new SubNetHashMapAdapter(getContext(),R.layout.subnet_size_input,mSubNetsHashMap);
        mSubNetsHashMapListView.setAdapter(subNetHashMapAdapter);
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



    private class SubNetHashMapAdapter extends BaseAdapter {
        private Context mContext;
        private HashMap<String, Integer> mData;
        private String[] mKeys;
        private int mResource;

        public SubNetHashMapAdapter(Context context, int resource,HashMap<String, Integer> data){
            mContext = context;
            mData    = data;
            mResource = resource;
            mKeys    = mData.keySet().toArray(new String[data.size()]);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Integer getItem(int position) {
            return mData.get(mKeys[position]);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            LayoutInflater inflater= LayoutInflater.from(mContext);

            convertView = inflater.inflate(mResource,parent,false) ;
            try {
                EditText subNetNameEditText       = convertView.findViewById(R.id.subNetNameEditText);
                EditText subNetSizeEditText       = convertView.findViewById(R.id.subNetSizeEditText);
                ImageButton delSubNetButton       = convertView.findViewById(R.id.delSubNetButton);

                final String name = mKeys[pos];
                Integer size = getItem(pos);

                subNetNameEditText.setText(name);
                subNetSizeEditText.setText(String.valueOf(size));

                delSubNetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mData.remove(name);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }

            return convertView;
        }
    }

}
