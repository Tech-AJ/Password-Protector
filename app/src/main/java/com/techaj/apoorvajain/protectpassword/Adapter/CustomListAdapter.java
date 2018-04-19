package com.techaj.apoorvajain.protectpassword.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.techaj.apoorvajain.protectpassword.Model.PasswordData;
import com.techaj.apoorvajain.protectpassword.R;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<PasswordData>
{
    int mLayoutID;
    private Context mContext;
    private List<PasswordData> pdList = new ArrayList<PasswordData>();

    public CustomListAdapter(@NonNull Context context, int layoutID,List<PasswordData> pdList) {
        super(context, R.layout.item_view,pdList);
        mContext = context;
        mLayoutID = layoutID;
        this.pdList=pdList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
       PasswordData data= pdList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(mLayoutID, parent, false);
        }
        // Lookup view for data population
        TextView title = convertView.findViewById(R.id.title);
        TextView password = (TextView) convertView.findViewById(R.id.password);
        TextView lastUpdated=convertView.findViewById(R.id.date);
        // Populate the data into the template view using the data object
        title.setText(data.getTitle());
        password.setText(data.getPassword());
        lastUpdated.setText(data.getLastUpdated());
        // Return the completed view to render on screen
        return convertView;
    }
}
