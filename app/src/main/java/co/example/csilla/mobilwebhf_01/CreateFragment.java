package co.example.csilla.mobilwebhf_01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.location.places.ui.PlacePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateFragment extends DialogFragment {

    public static final String TAG = "CreateFragment";
    int PLACE_PICKER_REQUEST = 2;

    // UI
    private EditText editPlaceName;
    private Button buttonPlacePicker;
    private TextView tvCoord;
    private TextView tvAddr;
    private EditText editAlarmNumber;
    private SeekBar simpleSeekBar;

    // Listener
    private CreatedListener listener;

    //Az onAttach hívás során ellenőrizzük, hogy van-e listener objektum beregisztrálva a dialógusunk számára
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getTargetFragment() != null) {
            try { listener = (CreatedListener) getTargetFragment();
            } catch (ClassCastException ce) { Log.e(TAG, "Target Fragment does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        } else {
            try { listener = (CreatedListener) activity;
            } catch (ClassCastException ce) { Log.e(TAG, "Parent Activity does not implement fragment interface!");
            } catch (Exception e) {
                Log.e(TAG, "Unhandled exception!");
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create, container, false);

        // Dialog cimenek beallitasa
        getDialog().setTitle(R.string.add_place);

        // UI elem referenciak elkerese
        editPlaceName = (EditText) root.findViewById(R.id.placeName);
        buttonPlacePicker = (Button) root.findViewById(R.id.placePickerButton);
        tvCoord = (TextView) root.findViewById(R.id.placeCoord);
        tvAddr = (TextView) root.findViewById(R.id.placeAddress);
        editAlarmNumber = (EditText) root.findViewById(R.id.number);
        simpleSeekBar = (SeekBar) root.findViewById(R.id.seekbar);
        int seekBarValue= simpleSeekBar.getProgress();
        buttonPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try{
                    intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (Exception ex) {}
            }
        });

        editAlarmNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    //update seekbar value after entering a number
                    simpleSeekBar.setProgress(Integer.parseInt(s.toString()));
                } catch(Exception ex){}
            }
        });

        editAlarmNumber.setText(String.valueOf(new Integer(seekBarValue)));
        editAlarmNumber.setSelection(editAlarmNumber.getText().length());
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * This listener method will be invoked if any change is made in the SeekBar.
             * @param seekBar
             * @param progresValue
             * @param fromUser
             */
            public void onProgressChanged (SeekBar seekBar, int progresValue, boolean fromUser) {
                try{
                    editAlarmNumber.setText(String.valueOf(new Integer(simpleSeekBar.getProgress())));
                    editAlarmNumber.setSelection(editAlarmNumber.getText().length());
                } catch (Exception ex) {}
            }

            /**
             * This listener method will be invoked at the start of user’s touch event. Whenever a user touch the thumb for dragging this method will automatically called.
             * @param seekBar
             */
            public void onStartTrackingTouch(SeekBar seekBar) {}

            /**
             * This listener method will be invoked at the end of user touch event. Whenever a user stop dragging the thump this method will be automatically called.
             * @param seekBar
             */
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        // A gombok esemenykezeloinek beallitasa
        Button btnOk = (Button) root.findViewById(R.id.btnCreatePlace);
        btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.parseInt(editAlarmNumber.getText().toString()) < 1 || Integer.parseInt(editAlarmNumber.getText().toString()) > 50) {
                    editAlarmNumber.requestFocus();
                    editAlarmNumber.setError(getResources().getString(R.string.errorAlarm));
                    return;
                }

                if (tvCoord.getText().toString().isEmpty()) {
                    tvCoord.requestFocus();
                    tvCoord.setError(getResources().getString(R.string.errorCoord));
                    return;
                }

                if (editPlaceName.getText().toString().isEmpty()) {
                    editPlaceName.requestFocus();
                    editPlaceName.setError(getResources().getString(R.string.errorName));
                    return;
                }

                if (listener != null) {
                    listener.onPlaceCreated(new Place(
                            editPlaceName.getText().toString() + "_" + createtimeStamp(),
                            editPlaceName.getText().toString(),
                            tvCoord.getText().toString(),
                            tvAddr.getText().toString(),
                            editAlarmNumber.getText().toString()
                    ));
                    Log.v("idididiidididi", "-------------------++++++++++++++++++++++----------------" +editPlaceName.getText().toString() + "_" + createtimeStamp());
                }
                dismiss();
            }
        });

        Button btnCancel = (Button) root.findViewById(R.id.btnCancelCreatePlace);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        return root;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, getContext());

                //String toastMsg = String.format("OK Hely: %s", place.getName() + place.toString());
                Log.v("place", "-------------------------------++++++++++++++++++++++-----------------------------"
                        + place.toString());

                editPlaceName.setText(place.getName());
                String c = place.getLatLng().toString();
                tvCoord.setText(c.substring(c.indexOf("(")+1, c.indexOf(")")-1));
                tvAddr.setText(place.getAddress());
                //Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                //String toastMsg = String.format("Cancel");
                //Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    // Listener interface
    public interface CreatedListener {
        public void onPlaceCreated(Place newPlace);
    }

    public String createtimeStamp(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Log.v("idididiidididi", "------------------->ididididi<----------------" +timeStamp);
        return timeStamp;
    }
}
