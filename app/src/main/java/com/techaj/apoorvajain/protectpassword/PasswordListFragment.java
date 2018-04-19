package com.techaj.apoorvajain.protectpassword;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.techaj.apoorvajain.protectpassword.Adapter.CustomListAdapter;
import com.techaj.apoorvajain.protectpassword.DB.DatabaseHelper;
import com.techaj.apoorvajain.protectpassword.Model.PasswordData;

import java.util.ArrayList;
import java.util.List;

public class PasswordListFragment extends ListFragment {
    OnPasswordSelectedListener mCallback;


    List<PasswordData> list;
    ListView listView;
    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_password_list, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list = new ArrayList<>();
        Log.e("AJ", "on activity created");

        list = databaseHelper.getAllData();
        CustomListAdapter adapter = new CustomListAdapter(getContext(), R.layout.item_view, list);
        listView.setAdapter(adapter);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("AJ", "on view created");
        listView = (ListView) view.findViewById(R.id.list_view);
        databaseHelper = new DatabaseHelper(getContext());
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallback.onPasswordSelected(list.get(position).getId());
        //   long dataId=list.get(position).getId();
        //  PasswordData data =databaseHelper.getData(dataId);
        // TextView tvNotes,tvUsername,tvPassword,tvTitle,tvLAstUpdated;
        // Toast.makeText(getActivity(),""+data.getTitle()+data.getPassword()+" "+data.getLastUpdated(),Toast.LENGTH_LONG).show();

    }

    // Container Activity must implement this interface
    public interface OnPasswordSelectedListener {
        public void onPasswordSelected(long position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPasswordSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }


    }
/*  @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long dataId=list.get(position).getId();
        PasswordData data =databaseHelper.getData(dataId);
        TextView tvNotes,tvUsername,tvPassword,tvTitle,tvLAstUpdated;
        Toast.makeText(getActivity(),""+data.getTitle()+data.getPassword()+" "+data.getLastUpdated(),Toast.LENGTH_LONG).show();


    }*/
}
