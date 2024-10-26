package com.example.mobilesentinel;

import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class SnifferService extends VpnService {

    private ParcelFileDescriptor vpnInterface;

    @Override
    public void onCreate() {
        super.onCreate();
        vpnInterface = establishVPN();
    }

    private ParcelFileDescriptor establishVPN() {
        Builder builder = new Builder();
        builder.addAddress("10.0.0.2", 24);
        builder.addRoute("0.0.0.0", 0);
        return builder.setSession("Sniffer").establish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            vpnInterface.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}