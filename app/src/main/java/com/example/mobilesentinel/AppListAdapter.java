package com.example.mobilesentinel;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppListAdapter extends ArrayAdapter<ApplicationInfo> {
    private PackageManager packageManager;
    private List<ApplicationInfo> appList;

    public AppListAdapter(Context context, List<ApplicationInfo> appList) {
        super(context, 0, appList);
        this.packageManager = context.getPackageManager();
        this.appList = appList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_app, parent, false);
        }

        // Get the current app
        ApplicationInfo appInfo = appList.get(position);

        // Get views from the layout
        ImageView appIcon = convertView.findViewById(R.id.appIcon);
        TextView appName = convertView.findViewById(R.id.appName);

        // Set the app icon and name
        appIcon.setImageDrawable(appInfo.loadIcon(packageManager));
        appName.setText(appInfo.loadLabel(packageManager));

        return convertView;
    }
}