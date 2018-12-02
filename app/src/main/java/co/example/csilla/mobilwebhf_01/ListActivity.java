package co.example.csilla.mobilwebhf_01;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gordonwong.materialsheetfab.MaterialSheetFab;

import co.example.csilla.mobilwebhf_01.design.Fab;
import io.realm.Realm;
import io.realm.RealmResults;

public class ListActivity extends AppCompatActivity
        implements  CreateFragment.CreatedListener,
        View.OnClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    // adapter
    private RecyclerViewAdapter adapter;
    private View recyclerView;
    // FAB
    private MaterialSheetFab materialSheetFab;

    // Realm
    private Realm realm;
    // Engedély
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 401;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        realm = Realm.getDefaultInstance();

        setupFab();
        //setupAlarm();
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.v("START", "START---START---START---START---START---START---START---START---START");
        recyclerView = findViewById(R.id.place_list);
        assert recyclerView != null;
        showLocationPreview();
    }

    //Sets up the Floating action button.
    private void setupFab() {

        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.colorAccent);
        final int fabColor = getResources().getColor(R.color.colorAccent);

        // Create material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        // Set material sheet item click listeners
        /*findViewById(R.id.fab_sheet_item_addAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //???
                materialSheetFab.hideSheet();
            }
        });*/
        findViewById(R.id.fab_sheet_item_addPlace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateFragment createFragment = new CreateFragment();
                FragmentManager fm = getSupportFragmentManager();
                createFragment.show(fm, CreateFragment.TAG);
                materialSheetFab.hideSheet();
            }
        });
    }


    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            navigateUpTo(new Intent(this, MapsActivity.class));
            return true;
        }
        return true;
    }
/*
    private void setupAlarm(){
        List<Alarm> alarms = alarmQuery();

        for (int i=0; i<alarms.size(); i++){
            if(alarms.get(i).getActive()){
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, alarms.get(i).getHour());
                calendar.set(Calendar.MINUTE, alarms.get(i).getMin());

                //Create a new PendingIntent and add it to the AlarmManager
                Intent intent = new Intent(this, AlarmReceiverActivity.class);
                intent.putExtra("Alarm type", alarms.get(i).getType());
                if(alarms.get(i).getName() != null) intent.putExtra("Alarm name", alarms.get(i).getName());
                intent.putExtra("Alarm hour", alarms.get(i).getHour());
                intent.putExtra("Alarm minute", alarms.get(i).getMin());

                PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                alarmIntent = PendingIntent.getActivity(this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                if(alarms.get(i).getRepeat() != null)
                    am.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
                //******setInexactRepeating() -> nem adható meg egyedi ismétlési gyakoriság, mint a setRepeating()-nél
                //******konstansokat kell használni: INTERVAL_FIFTEEN_MINUTES, INTERVAL_DAY
            }
        }
    }
*/
    /*private List<Alarm> alarmQuery(){
        Log.v("alarmQuery", " alarmQuery--alarmQuery--alarmQuery--alarmQuery--alarmQuery--alarmQuery ");
        final RealmResults<Alarm> alarms = realm.where(Alarm.class).findAllAsync();
        alarms.load();
        Log.v("alarms: ", "******************0000000000000000000000000000000000000000000000000000******************* " + alarms);
        return alarms;
    }*/

    @Override
    public void onClick(View v) {
        Toast.makeText(this, R.string.sheet_item_pressed, Toast.LENGTH_SHORT).show();
        //materialSheetFab.hideSheet();
    }

    ////////////
    //  Realm //    ********************************************************************************************************
    ////////////

    private void setupRealm() {
        setupRecyclerView((RecyclerView) recyclerView);
        adapter = (RecyclerViewAdapter) ((RecyclerView) recyclerView).getAdapter();
    }

    //az adapter feltöltésée a Realm-be mentett adatok alapján
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Log.v("SETUP RV", "SETUP RV---SETUP RV---SETUP RV---SETUP RV---SETUP RV---SETUP RV");
        RealmResults<Place> result = realm.where(Place.class).findAllAsync();
        result.load();
        recyclerView.setAdapter(new RecyclerViewAdapter(result, ListActivity.this));
    }

    @Override
    public void onPlaceCreated(Place newPlace) {
        Log.v("PLANT CREATED", "PLANT CREATED --- PLANT CREATED --- PLANT CREATED --- PLANT CREATED --- PLANT CREATED");
        save_to_database(   newPlace.getId(),
                newPlace.getPlaceName(),
                newPlace.getPlaceCoordinates(),
                newPlace.getPlaceAddress(),
                newPlace.getAlarmDistance());
    }

    private void refresh_database() {
        Log.v("REFRESH", "REFRESH---REFRESH---REFRESH---REFRESH---REFRESH---REFRESH---REFRESH---REFRESH---REFRESH" + adapter.getItemCount());
        onStop();
        onStart();
    }

    private void save_to_database(final String Id, final String Place, final String PlaceCoord,
                                  final String PlaceAddr, final String AlarmDist) {
        Log.v("SAVE", "SAVE---SAVE---SAVE---SAVE---SAVE---SAVE---SAVE---SAVE---SAVE ");
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Place place = bgRealm.createObject(Place.class);
                place.setId(Id);
                place.setPlaceName(Place);
                place.setPlaceCoordinates(PlaceCoord);
                place.setPlaceAddress(PlaceAddr);
                place.setAlarmDistance(AlarmDist);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() { // Transaction was a success.
                Log.v("Success", "------------------->OK<----------------");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) { // Transaction failed and was automatically canceled.
                Log.e("Failed", error.getMessage());
            }
        });
        refresh_database();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        realm.close();
    }

    ///////////////
    // Engedély //    ********************************************************************************************************
    //////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty. - ha a kérést elCancellálták, az eredménytömb üres
                // Request for camera permission.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the contacts-related task you need to do. - engedély megadva
                    setupRealm();
                }
            }
        }
    }

    private void showLocationPreview() {
        // Check if the Location permission has been granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            // Permission is already available, start camera preview
            setupRealm();
        } else {
            // Permission is missing and must be requested.
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            setupRealm();
        }
    }
}

