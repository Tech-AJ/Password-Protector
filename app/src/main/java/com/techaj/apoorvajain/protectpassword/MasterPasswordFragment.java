package com.techaj.apoorvajain.protectpassword;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.techaj.apoorvajain.protectpassword.DB.DatabaseHelper;
import com.techaj.apoorvajain.protectpassword.Utils.AESCrypt;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasterPasswordFragment extends Fragment {


    OnCorrectPasswordEnteredListener mCallback;
    TextInputEditText etMasterPassword;
    TextInputLayout eTLayoutMasterPassword;
    DatabaseHelper databaseHelper;
    AESCrypt aesCrypt;
    Button btSubmitMasterPassword;
    boolean isFirstLogin;

    public MasterPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_master_password, container, false);
    }

    // Container Activity must implement this interface
    public interface OnCorrectPasswordEnteredListener {
        public void onCorrectPassword();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etMasterPassword = view.findViewById(R.id.et_master_pwd);
        eTLayoutMasterPassword = view.findViewById(R.id.et_layout_master_pwd);
        btSubmitMasterPassword = view.findViewById(R.id.bt_submit_master_pwd);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        databaseHelper = new DatabaseHelper(getContext());
        try {
            aesCrypt = AESCrypt.getInstance();
        } catch (KeyStoreException | InvalidAlgorithmParameterException | NoSuchProviderException | IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
            Log.e("AJ", "error initializing AESCrupt" + e);
        }


        isFirstLogin = (databaseHelper.matchIV() == null);      // check if master password is not set already


        onSubmitButtonClickListener();
        setMasterPasswordTextChangeListener();
    }


    void onSubmitButtonClickListener() {

        btSubmitMasterPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!etMasterPassword.getText().toString().equals("")) {
                    AESCrypt.setIV(etMasterPassword.getText().toString());
                    DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                    if (!isFirstLogin) {

                        try {
                            aesCrypt.decrypt(databaseHelper.matchIV(), getContext());
                        } catch (Exception e) {
                            e.printStackTrace();

                            Toast.makeText(getContext(), "Password not matched", Toast.LENGTH_SHORT).show();
                            return;

                        }

                        mCallback.onCorrectPassword();

                    } else {

                        String encryptedIV;
                        try {
                            encryptedIV = (aesCrypt.encrypt(AESCrypt.getIV(), getContext()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("AJ", "error encrypting" + e);
                            return;
                        }

                        databaseHelper.insertIV(encryptedIV);

                        Log.e("AJ", "data inserted");
                        mCallback.onCorrectPassword();
                    }

                } else {
                    Toast.makeText(getContext(), "Password can not be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnCorrectPasswordEnteredListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCorrectPasswordEnteredListener");
        }


    }

    private void setMasterPasswordTextChangeListener() {
        etMasterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 12)
                    eTLayoutMasterPassword.setError("Master Password can be 1-12 characters long!!!");
                else if (s.length()==0)
                    eTLayoutMasterPassword.setError("Master Password can not be empty!!!");
                else
                    eTLayoutMasterPassword.setError("");

                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                if (!isFirstLogin) {

                    try {
                        AESCrypt.setIV(s.toString());
                        aesCrypt.decrypt(databaseHelper.matchIV(), getContext());
                        mCallback.onCorrectPassword();

                    } catch (Exception e) {
                        e.printStackTrace();

                        //  Toast.makeText(getContext(), "Password not matched", Toast.LENGTH_SHORT).show();
                        // return;

                    }
                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
