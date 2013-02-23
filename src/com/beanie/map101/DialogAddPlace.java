
package com.beanie.map101;

import com.google.android.gms.maps.model.LatLng;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DialogAddPlace extends Dialog {

    private LatLng latLng;

    private MapLocation location;

    public DialogAddPlace(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public DialogAddPlace(Context context, int theme) {
        super(context, theme);
        init();
    }

    public DialogAddPlace(Context context) {
        super(context);
        init();
    }

    private void init() {
        setTitle(R.string.label_add_place);
        setContentView(R.layout.dialog_add_place);

        Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveNewPlace();
            }
        });
        Button buttonCancel = (Button) findViewById(R.id.buttonCancel);

        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void saveNewPlace() {
        EditText editTextName = (EditText) findViewById(R.id.editTextName);
        EditText editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        Log.i(getClass().getName(), "Location: " + latLng.toString());
        Log.i(getClass().getName(), "Name: " + name);
        Log.i(getClass().getName(), "Description: " + description);

        location = new MapLocation();
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        location.setName(name);
        location.setDescription(description);

        DBHelper helper = new DBHelper(getContext());
        helper.open();
        location.set_id(helper.addNewLocation(location));
        helper.close();

        dismiss();
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public MapLocation getLocation() {
        return location;
    }

}
