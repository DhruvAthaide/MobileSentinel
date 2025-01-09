package com.example.mobilesentinel;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.graphics.Color;
import java.util.List;

public class AppListAdapter extends ArrayAdapter<ApplicationInfo> {

    private PackageManager packageManager;

    public AppListAdapter(Context context, List<ApplicationInfo> appList) {
        super(context, 0, appList);
        packageManager = context.getPackageManager();
    }@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.app_list_adapter, parent, false);
        }

        ApplicationInfo appInfo = getItem(position);

        ImageView appIconImageView = view.findViewById(R.id.appIcon);
        TextView appNameTextView = view.findViewById(R.id.appName);

        Drawable appIcon = appInfo.loadIcon(packageManager);
        String appName = appInfo.loadLabel(packageManager).toString();

        appIconImageView.setImageDrawable(appIcon);
        appNameTextView.setText(appName);

        // Resolve and apply the text color from the theme
        TypedValue typedValue = new TypedValue();
        boolean resolved = getContext().getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        // If the Theme Color is not working then it will use the Specific Hardcoded Color
        int textColor = resolved ? typedValue.data : Color.parseColor("#296DFF");
        appNameTextView.setTextColor(textColor);

        // Log the Position and ConvertView status and Color Status for Confirmation
        Log.d("AppListAdapter", "Position: " + position);
        Log.d("AppListAdapter", "ConvertView is null: " + (convertView == null));
        Log.d("AppListAdapter", "Resolved color: " + textColor);

        // Set visibility of the views
        appIconImageView.setVisibility(View.VISIBLE);
        appNameTextView.setVisibility(View.VISIBLE);
        // appNameTextView.setTextColor(Color.WHITE);

        return view;
    }
}