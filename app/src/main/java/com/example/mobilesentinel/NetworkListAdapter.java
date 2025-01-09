package com.example.mobilesentinel;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class NetworkListAdapter extends ArrayAdapter<ScanResult> {
    private List<ScanResult> wifiList;

    public NetworkListAdapter(Context context, List<ScanResult> wifiList) {
        super(context, 0, wifiList);
        this.wifiList = wifiList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.network_list_adapter, parent, false);
        }

        ScanResult scanResult = wifiList.get(position);
        TextView wifiName = convertView.findViewById(R.id.wifiName);
        ImageView lockIcon = convertView.findViewById(R.id.lockIcon);

        wifiName.setText(scanResult.SSID);

        // Check if the network is secured
        String capabilities = scanResult.capabilities;
        boolean isSecured = capabilities.contains("WEP") || capabilities.contains("WPA") || capabilities.contains("WPA2");

        // Set the lock icon based on the security status
        if (isSecured) {
            lockIcon.setImageResource(R.drawable.ic_lock); // Lock icon for secured networks
        } else {
            lockIcon.setImageResource(R.drawable.ic_open_lock); // Open lock icon for unsecured networks
        }

        return convertView;
    }
}