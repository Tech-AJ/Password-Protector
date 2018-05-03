package com.techaj.apoorvajain.protectpassword;

import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.techaj.apoorvajain.protectpassword.DB.DatabaseHelper;

public class PasswordActivity extends AppCompatActivity implements
        PasswordListFragment.OnPasswordSelectedListener , MasterPasswordFragment.OnCorrectPasswordEnteredListener {

    DatabaseHelper databaseHelper;
    Fragment fragPassList, fragPassDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        databaseHelper = new DatabaseHelper(this);

        if (savedInstanceState == null) {
            // Instance of first fragment
            MasterPasswordFragment masterPasswordFragment= new MasterPasswordFragment();
            // Add Fragment to FrameLayout (flContainer), using FragmentManager
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
            ft.add(R.id.fl_container, masterPasswordFragment);                                // add    Fragment
            ft.commit();// commit FragmentTransaction
        }



     /*   if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
           PasswordDetailFragment secondFragment = new PasswordDetailFragment();
            Bundle args = new Bundle();
            args.putInt("position", -1);
            secondFragment.setArguments(args);          // (1) Communicate with Fragment using Bundle
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
           ft2.add(R.id.fl_container2,secondFragment);                 // add    Fragment
            ft2.commit();                                                            // commit FragmentTransaction
        }

*/


    }


    public void fabClickListener() {

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PasswordDetailFragment secondFragment = new PasswordDetailFragment();
                Bundle args = new Bundle();
                args.putInt("position", -1);

                secondFragment.setArguments(args);


                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container2, secondFragment) // replace flContainer
                            //.addToBackStack(null)
                            .commit();


                } else {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fl_container, secondFragment) // replace flContainer
                            .addToBackStack(null)
                            .commit();

                }

            }
        });
    }


    @Override
    public void onPasswordSelected(long position,String masterPassword) {
        //Toast.makeText(this, "Called By Fragment A: position - "+ , Toast.LENGTH_SHORT).show();

        PasswordDetailFragment secondFragment = PasswordDetailFragment.newInstance(position);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container2, secondFragment) // replace flContainer
                    //.addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, secondFragment) // replace flContainer
                    .addToBackStack(null)
                    .commit();
        }

    }


    @Override
    public void onCorrectPassword() {
        // Instance of first fragment
        Fragment firstFragment =  new PasswordListFragment();
        // Add Fragment to FrameLayout (flContainer), using FragmentManager
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();// begin  FragmentTransaction
        ft.replace(R.id.fl_container, firstFragment);                                // add    Fragment
        ft.commit();// commit FragmentTransaction
        fabClickListener();
    }
}
