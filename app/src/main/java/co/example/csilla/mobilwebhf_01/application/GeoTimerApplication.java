package co.example.csilla.mobilwebhf_01.application;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GeoTimerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // The default Realm file is "default.realm" in Context.getFilesDir();
        // we'll change it to "myrealm.realm"
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
    }
}
