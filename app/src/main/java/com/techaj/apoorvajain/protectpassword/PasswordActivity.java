package com.techaj.apoorvajain.protectpassword;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

Log.e("Ajjj","fab");
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.itemShare:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                    String sAux = "\nHey, this is a very useful Password Manager app to save your password with 3 way authentication security. " +
                            "\nDownload It Now\n";
                    sAux = sAux + "http://play.google.com/store/apps/details?id=" + getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    if (i.resolveActivity(getPackageManager()) != null) {
                        startActivity(Intent.createChooser(i, "Share via"));
                    }
                } catch (Exception e) {
                    //e.toString();
                }
                return true;
            case R.id.itemRateUs:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
                flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                goToMarket.addFlags(flags);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));

                }
                return true;
            case R.id.itemMoreApps:
                uri = Uri.parse("market://search?q=pub:Tech AJ");
                goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
                flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
                goToMarket.addFlags(flags);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/developer?id=Tech+AJ")));
                    return true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }
}
