package com.tohandesign.spendingtrackingapp;

import android.app.Application;
import android.content.Context;

import com.tohandesign.spendingtrackingapp.purchase.BillingDataSource;
import com.tohandesign.spendingtrackingapp.purchase.TrivialDriveRepository;

public class App extends Application {
    private static Context context;

    private static App app;

    public AppContainer appContainer;

    public static final String[] INAPP_SKUS = new String[]{"ip_packet_point_01", "ip_packet_point_02", "ip_packet_point_03", "ip_packet_point_04", "ip_packet_point_05", "ip_packet_point_06"};
    public static final String[] AUTO_CONSUME_SKUS = new String[]{"ip_packet_point_01", "ip_packet_point_02", "ip_packet_point_03", "ip_packet_point_04", "ip_packet_point_05", "ip_packet_point_06"};

    // Container of objects shared across the whole app
    public class AppContainer {
        final BillingDataSource billingDataSource = BillingDataSource.getInstance(
                App.this,
                INAPP_SKUS,
                new String[]{},
                AUTO_CONSUME_SKUS);
        final public TrivialDriveRepository trivialDriveRepository = new TrivialDriveRepository(
                billingDataSource);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        app = this;
        appContainer = new AppContainer();
    }

    public static App get() {
        return app;
    }

    public static Context getContext() {
        return context;
    }

}