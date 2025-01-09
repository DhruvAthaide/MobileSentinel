package com.example.mobilesentinel;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PermissionAnalyzerActivity extends AppCompatActivity {
    private ListView listViewApps;
    private PackageManager packageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission_analyzer_activity);

        listViewApps = findViewById(R.id.listView_apps);
        packageManager = getPackageManager();

        // Get the list of installed apps
        List<ApplicationInfo> appList = getInstalledApplications();

        // Use the custom adapter to display the app icons and names
        AppListAdapter adapter = new AppListAdapter(this, appList);
        listViewApps.setAdapter(adapter);

        // Set item click listener to show permissions for the selected app using a lambda expression
        listViewApps.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(PermissionAnalyzerActivity.this, AppPermissionDetailsActivity.class);
            intent.putExtra("packageName", appList.get(position).packageName);
            startActivity(intent);
        });
    }

    private List<ApplicationInfo> getInstalledApplications() {
        List<ApplicationInfo> appList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // If running on Android 11+, handle package visibility restrictions
            List<ApplicationInfo> allApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            for (ApplicationInfo appInfo : allApps) {
                if (packageManager.getLaunchIntentForPackage(appInfo.packageName) != null) {
                    appList.add(appInfo);
                }
            }
        } else {
            // For older versions, get the list directly
            appList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        }
        return appList;
    }
}