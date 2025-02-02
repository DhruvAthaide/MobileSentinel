package com.example.mobilesentinel;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class NetworkSecuritySSLActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network_security_ssl_activity);

        Button scanButton = findViewById(R.id.scanButton);
        resultTextView = findViewById(R.id.resultTextView);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanAppsForCleartextTraffic();
            }
        });
    }

    private void scanAppsForCleartextTraffic() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        StringBuilder report = new StringBuilder();

        for (ApplicationInfo app : apps) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(app.packageName, PackageManager.GET_CONFIGURATIONS);
                int flags = packageInfo.applicationInfo.flags;
                if ((flags & ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC) != 0) {
                    report.append(app.loadLabel(pm)).append(" uses cleartext HTTP!\n");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (report.length() == 0) {
            report.append("No apps using cleartext HTTP found.");
        }

        resultTextView.setText(report.toString());
    }
}