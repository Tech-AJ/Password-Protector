package com.techaj.apoorvajain.protectpassword.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techaj.apoorvajain.protectpassword.DB.DatabaseHelper;
import com.techaj.apoorvajain.protectpassword.Model.PasswordData;
import com.techaj.apoorvajain.protectpassword.R;
import com.techaj.apoorvajain.protectpassword.Utils.AESCrypt;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<PasswordData> {
    int mLayoutID;
    private Context mContext;
    private List<PasswordData> pdList;
    DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
    AESCrypt aesCrypt;


    public CustomListAdapter(@NonNull Context context, int layoutID, List<PasswordData> pdList) {
        super(context, R.layout.item_view, pdList);
        mContext = context;
        mLayoutID = layoutID;
        this.pdList = pdList;
        databaseHelper = new DatabaseHelper(mContext);
        try {
            aesCrypt = AESCrypt.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        PasswordData data = pdList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutID, parent, false);
        }
        // Lookup view for data population
        TextView title = convertView.findViewById(R.id.title);
        TextView password = (TextView) convertView.findViewById(R.id.password);
        TextView userName = convertView.findViewById(R.id.user_nam);
        TextView lastUpdated = convertView.findViewById(R.id.date);
        // Populate the data into the template view using the data object
        title.setText(data.getTitle());
        userName.setText(data.getUserName());
        try {
            //  AESCrypt aesCrypt=new AESCrypt();
            password.setText(aesCrypt.decrypt(//databaseHelper.getData(data.getId())
                    data.getPassword() + "", getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        lastUpdated.setText(data.getLastUpdated());
        // Return the completed view to render on screen
        return convertView;
    }
}
