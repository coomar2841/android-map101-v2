
package com.beanie.map101;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class DialogAddPlace extends Dialog {

    private boolean isEditMode = true;

    private LatLng latLng;

    private MapLocation location;

    private EditText editTextName;

    private EditText editTextDescription;

    public DialogAddPlace(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public DialogAddPlace(Context context, int theme) {
        super(context, theme);
        init();
    }

    public DialogAddPlace(Context context, boolean isEditMode) {
        super(context);
        this.isEditMode = isEditMode;
        init();
    }

    private void init() {
        if (isEditMode) {
            setTitle(R.string.label_add_place);
        } else {
            setTitle(R.string.label_place_info);
        }
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

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);

        if (!isEditMode) {
            editTextName.setEnabled(false);
            editTextDescription.setEnabled(false);
            buttonSave.setVisibility(View.GONE);
        }
    }

    private void saveNewPlace() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description)) {
            Toast.makeText(getContext(), getContext().getString(R.string.label_add_name_desc),
                    Toast.LENGTH_LONG).show();
            return;
        }
        if (doesNameExist(name)) {
            Toast.makeText(getContext(),
                    getContext().getString(R.string.label_title_already_exists), Toast.LENGTH_LONG)
                    .show();
            return;
        }
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

    private boolean doesNameExist(String name) {
        boolean exists = false;
        DBHelper helper = new DBHelper(getContext());
        helper.open();
        exists = helper.doesNameExist(name);
        helper.close();
        return exists;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;

        TextView textViewLocation = (TextView) findViewById(R.id.textViewLocation);

        textViewLocation.setText(String.format("Lat/Long %.2f/%.2f", latLng.latitude,
                latLng.longitude));
    }

    public void displayLocationInfo(MapLocation location) {
        editTextName.setText(location.getName());
        editTextDescription.setText(location.getDescription());
        setLatLng(location.getLatLng());
    }

    public MapLocation getLocation() {
        return location;
    }

}
