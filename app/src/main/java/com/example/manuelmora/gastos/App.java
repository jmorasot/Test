package com.example.manuelmora.gastos;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/*
* @author Juan Mora
* @since 11/09/2018 13:01
*/

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Realm database initialization.
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
