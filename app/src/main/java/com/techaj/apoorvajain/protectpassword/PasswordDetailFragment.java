package com.techaj.apoorvajain.protectpassword;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techaj.apoorvajain.protectpassword.DB.DatabaseHelper;
import com.techaj.apoorvajain.protectpassword.Model.PasswordData;
import com.techaj.apoorvajain.protectpassword.Utils.AESCrypt;

import java.util.Objects;

public class PasswordDetailFragment extends Fragment {
    TextInputEditText etTitle, etUserName, etPassword, etNotes;
    TextInputLayout eTLayoutTitle;
    long position = -1;
    DatabaseHelper databaseHelper;
    Button btSave, btUpdate, btDelete;
    LinearLayout llSave, llUpdate;
    ImageButton imgBtnCopyToClipboard;
    AESCrypt aesCrypt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            // Get back arguments
            if (getArguments() != null) {
                position = getArguments().getLong("position", -1);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {

        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_password_detail, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        etTitle = view.findViewById(R.id.et_title);
        etNotes = view.findViewById(R.id.et_notes);
        etPassword = view.findViewById(R.id.et_password);
        etUserName = view.findViewById(R.id.et_user_name);
        eTLayoutTitle = view.findViewById(R.id.et_layout_title);
        btDelete = view.findViewById(R.id.bt_delete);
        btSave = view.findViewById(R.id.bt_save);
        btUpdate = view.findViewById(R.id.bt_update);
        llSave = view.findViewById(R.id.ll_save);
        llUpdate = view.findViewById(R.id.ll_update);
        imgBtnCopyToClipboard = view.findViewById(R.id.img_btn_clipboard);

        databaseHelper = new DatabaseHelper(getContext());
        aesCrypt = new AESCrypt();

        updateView(position);
        onSaveButtonClickListener();
        onUpdateButtonClickListener();
        onDeleteButtonClickListener();
        clearBackStackExceptOne();
        copyToClipboard();
        //  onTitleTextChangeListener();

    }

    // Activity is calling this to update view on Fragment
    public void updateView(long position) {
        if (position == -1) {
            llUpdate.setVisibility(View.GONE);
            llSave.setVisibility(View.VISIBLE);
            etTitle.setText("");
            etUserName.setText("");
            etPassword.setText("");
            etNotes.setText("");
        } else {
            llUpdate.setVisibility(View.VISIBLE);
            llSave.setVisibility(View.GONE);
            etTitle.setText(databaseHelper.getData(position).getTitle());
            etNotes.setText(databaseHelper.getData(position).getNotes());
         /*  byte[] bytes;
            Log.e("AJ", "db" + databaseHelper.getData(position).getPassword());
            try {
                bytes = aesCrypt.decrypt(databaseHelper.getData(position).getPassword());
            } catch (Exception e) {
                e.printStackTrace();
                bytes = "error".getBytes();

            }
            Log.e("AJ", bytes + " bytes");
            etPassword.setText(bytes.toString());*/
            etPassword.setText(databaseHelper.getData(position).getPassword());
            etUserName.setText(databaseHelper.getData(position).getUserName());

        }
        //Toast.makeText(getContext(), "Called By Fragment A: position - " + position, Toast.LENGTH_SHORT).show();
    }

    public void onSaveButtonClickListener() {
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etTitle.getText().toString().equals("")) {
                    PasswordData data = new PasswordData();

                    data.setTitle(etTitle.getText().toString());
                    data.setUserName(etUserName.getText().toString());
              /*      byte[] bytes;
                    try {
                        bytes = aesCrypt.encrypt(etPassword.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        bytes = "error".getBytes();

                    }
                    Log.e("AJ", bytes + "");

                    data.setPassword(bytes + "");*/
                    data.setPassword(etPassword.getText().toString());
                    data.setNotes(etNotes.getText().toString());


                    databaseHelper.insertData(data);
                    Toast.makeText(getContext(), "Data Saved Successfully !!!", Toast.LENGTH_SHORT).show();
                    updateListInLandscape();
                    //  updateListInPortrait();
                } else
                    Toast.makeText(getContext(), "Title can not be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

  /*  public void onTitleTextChangeListener() {
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("AJ", "inside before change");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("AJ", "inside while change");
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0)
                    Toast.makeText(getContext(), "Title can not be empty", Toast.LENGTH_SHORT).show();
                else

                    Log.e("AJ", "inside afterchange");

            }
        });
    }*/

    public void onUpdateButtonClickListener() {
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PasswordData data = new PasswordData();
                data.setTitle(etTitle.getText().toString());
                data.setUserName(etUserName.getText().toString());
                data.setPassword(etPassword.getText().toString());
                data.setNotes(etNotes.getText().toString());


                data.setId(position);
                Log.e("AJ", "update at " + position);
                if (databaseHelper.updateData(data) == 1)
                    Toast.makeText(getContext(), "Data Updated Successfully !!!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Some error occured !!! Please try again .", Toast.LENGTH_SHORT).show();
                //  updateListInPortrait();
                updateListInLandscape();

            }
        });
    }

    public void updateListInLandscape() {
        {
            PasswordListFragment firstFragment = new PasswordListFragment();
            // Add Fragment to FrameLayout (flContainer), using FragmentManager
            FragmentManager fm = getFragmentManager();
            assert fm != null;
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            FragmentTransaction ft = fm.beginTransaction();// begin  FragmentTransaction
            ft.replace(R.id.fl_container, firstFragment);                                // add    Fragment
            ft.commit();

        }
    }


    public void onDeleteButtonClickListener() {
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseHelper.deleteData(position);
                Toast.makeText(getContext(), "Data Deleted Successfully !!!", Toast.LENGTH_SHORT).show();
                updateListInLandscape();

            }
        });
    }

    public void clearBackStackExceptOne() {
        int backStackCount = getFragmentManager().getBackStackEntryCount();
        Log.e("AJ", "BS count  " + backStackCount);
        for (int i = 1; i < backStackCount; i++) {

            // Get the back stack fragment id.
            int backStackId = getFragmentManager().getBackStackEntryAt(i).getId();

            getFragmentManager().popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }
    }

    private void copyToClipboard() {

        imgBtnCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPassword.getText().toString().equals("")) {
                    ClipboardManager clipboard = (ClipboardManager)
                            Objects.requireNonNull(getContext()).getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("pwd", etPassword.getText().toString());
                    assert clipboard != null;
                    clipboard.setPrimaryClip(clip);
                    Snackbar.make(Objects.requireNonNull(getView()), "Copied to Clipboard", Snackbar.LENGTH_SHORT).show();

                } else
                    Snackbar.make(Objects.requireNonNull(getView()), "Nothing to Copy", Snackbar.LENGTH_SHORT).show();

            }
        });
    }

}

