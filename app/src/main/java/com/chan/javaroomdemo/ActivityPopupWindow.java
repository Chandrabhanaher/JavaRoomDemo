package com.chan.javaroomdemo;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ActivityPopupWindow extends AppCompatActivity implements CustomSpinner.OnSpinnerEventsListener {
     private CustomSpinner customSpinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window);



        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.3));

        WindowManager.LayoutParams params =    getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);


        ImageView btnClose = findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v->{
            finish();
        });

        final AutoCompleteTextView customerAutoTV = findViewById(R.id.customerTextView);

        ArrayList<String> customerList = getCustomerList();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityPopupWindow.this, android.R.layout.simple_spinner_item, customerList);
        customerAutoTV.setAdapter(adapter);

        customSpinner = findViewById(R.id.customSpinner);
        customSpinner.setSpinnerEventsListener(this);
        customSpinner.setAdapter(adapter);

    }
    private ArrayList<String> getCustomerList()
    {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("James");
        customers.add("Mary");
        customers.add("Paul");
        customers.add("Michael");
        customers.add("William");
        customers.add("Daniel");
        customers.add("Thomas");
        customers.add("Sarah");
        customers.add("Sophia");
        return customers;
    }

    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        customSpinner.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_spinner_fruit_up));
    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        customSpinner.setBackground(ContextCompat.getDrawable(this,R.drawable.bg_spinner_fruit));
    }
}
