package cc.shrestha.assignment1;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class MeasurementActivity extends AppCompatActivity {
    //variable to be mapped to
    Calendar mCalendar;
    EditText editTextDate;
    EditText editTextTime;
    EditText editTextSystolic;
    EditText editTextDiastolic;
    EditText editTextHeartRate;
    EditText editTextComment;
    Button addButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        Intent intent = getIntent();
        //initialize variables with objects from layout
        mCalendar = Calendar.getInstance();
        editTextDate = findViewById(R.id.editTextDate);
        editTextTime = findViewById(R.id.editTextTime);
        editTextSystolic = findViewById(R.id.editTextSystolic);
        editTextDiastolic = findViewById(R.id.editTextDiastolic);
        editTextHeartRate = findViewById(R.id.editTextHeartRate);
        editTextComment = findViewById(R.id.editTextComment);
        addButton = findViewById(R.id.buttonAdd); //save or add
        cancelButton = findViewById(R.id.buttonCancel); //delete or cancel
        //check if user is adding or editing
        final int intentType = intent.getIntExtra("TYPE",0); // 1 = add, 2 = edit
        if(intentType == 2){ // Edit Measurement
            //User wants to edit the entered data
            //Set the text to the corresponding variables from layout
            editTextDate.setText(intent.getStringExtra("DATE"));
            editTextTime.setText(intent.getStringExtra("TIME"));
            editTextSystolic.setText(intent.getIntExtra("SYSTOLIC",0) + "");
            editTextDiastolic.setText(intent.getIntExtra("DIASTOLIC",0) + "");
            editTextHeartRate.setText(intent.getIntExtra("HEARTRATE",0) + "");
            editTextComment.setText(intent.getStringExtra("COMMENT"));
            addButton.setEnabled(true);
            CheckPressure(editTextSystolic.getText().toString(), editTextDiastolic.getText().toString());
            //change the button names
            addButton.setText("Save");
            cancelButton.setText("Delete");
        }

        //https://www.codingdemos.com/android-datepicker-button/
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //popup dialog to pick date
                int mYear = mCalendar.get(Calendar.YEAR);
                int mMonth = mCalendar.get(Calendar.MONTH);
                int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MeasurementActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editTextDate.setText(year + "/" + month + "/" + day);
                    }
                }, mYear, mMonth, mDay);
                if(!editTextDate.getText().toString().trim().isEmpty()) {
                    String[] yearMonthDay = editTextDate.getText().toString().split("/");
                    int year = Integer.parseInt(yearMonthDay[0]);
                    int month = Integer.parseInt(yearMonthDay[1]);
                    int day = Integer.parseInt(yearMonthDay[2]);
                    datePickerDialog.updateDate(year, month, day);
                }
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });
        //https://www.codingdemos.com/android-timepicker-edittext/
        editTextTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //popup dialog to pick time
                int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = mCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(MeasurementActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if(minutes < 10){
                            editTextTime.setText(hourOfDay + ":0" + minutes);
                        }else{
                            editTextTime.setText(hourOfDay + ":" + minutes);
                        }
                    }
                }, mHour, mMinute, true);
                if(!editTextTime.getText().toString().trim().isEmpty()) {
                    String[] hourMinute = editTextTime.getText().toString().split(":");
                    int hour = Integer.parseInt(hourMinute[0]);
                    int min = Integer.parseInt(hourMinute[1]);
                    timePickerDialog.updateTime(hour, min);
                }
                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });
        //https://www.youtube.com/watch?v=Vy_4sZ6JVHM
        //make sure there has been text entered
        editTextDate.addTextChangedListener(displayTextWatcher);
        editTextTime.addTextChangedListener(displayTextWatcher);
        editTextSystolic.addTextChangedListener(displayTextWatcher);
        editTextDiastolic.addTextChangedListener(displayTextWatcher);
        editTextHeartRate.addTextChangedListener(displayTextWatcher);

        addButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //pass back the values from edit text variables to be stored/updated
                Intent intent = new Intent();
                intent.putExtra("DATE",editTextDate.getText().toString());
                intent.putExtra("TIME",editTextTime.getText().toString());
                intent.putExtra("SYSTOLIC",Integer.parseInt(editTextSystolic.getText().toString()));
                intent.putExtra("DIASTOLIC",Integer.parseInt(editTextDiastolic.getText().toString()));
                intent.putExtra("HEARTRATE",Integer.parseInt(editTextHeartRate.getText().toString()));
                intent.putExtra("COMMENT",editTextComment.getText().toString());
                if(intentType == 1) { // add the user input
                    setResult(1, intent);
                }else if(intentType == 2){ // save the updated information
                    setResult(2, intent);
                }
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(intentType == 2){ //check if the user is editing the item
                    setResult(3); //set it so it will delete the object
                }
                finish();
            }
        });


    }
    private TextWatcher displayTextWatcher = new TextWatcher() {

        //check to make sure user had input in all required fields
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String dateInput = editTextDate.getText().toString().trim();
            String timeInput = editTextTime.getText().toString().trim();
            String systolicInput = editTextSystolic.getText().toString().trim();
            String diastolicInput = editTextDiastolic.getText().toString().trim();
            String heartRateInput = editTextHeartRate.getText().toString().trim();
            boolean buttonStatus = !dateInput.isEmpty() && !timeInput.isEmpty() && !systolicInput.isEmpty() && !diastolicInput.isEmpty() && !heartRateInput.isEmpty();
            addButton.setEnabled(buttonStatus);
            CheckPressure(systolicInput,diastolicInput);
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private void CheckPressure(String systolicInput, String diastolicInput){
        if(!editTextSystolic.getText().toString().isEmpty() && (Integer.parseInt(systolicInput) < 90 || Integer.parseInt(systolicInput) > 140)){
            editTextSystolic.setTextColor(Color.RED);
        }else{
            editTextSystolic.setTextColor(Color.BLACK);
        }
        if(!editTextDiastolic.getText().toString().isEmpty() && (Integer.parseInt(diastolicInput) < 60 || Integer.parseInt(diastolicInput) > 90)){
            editTextDiastolic.setTextColor(Color.RED);
        }else{
            editTextDiastolic.setTextColor(Color.BLACK);
        }
    }
}

/*
editTextDate.setText(intent.getStringExtra("DATE"));
editTextTime.setText(intent.getStringExtra("TIME"));
editTextSystolic.setText(intent.getIntExtra("SYSTOLIC",0));
editTextDiastolic.setText(intent.getIntExtra("DIASTOLIC",0));
editTextHeartRate.setText(intent.getIntExtra("HEARTRATE",0));
editTextComment.setText(intent.getStringExtra("COMMENT"));

String mDate = intent.getStringExtra("DATE");
String mTime = intent.getStringExtra("TIME");
int mSystolic = intent.getIntExtra("SYSTOLIC",0);
int mDiastolic = intent.getIntExtra("DIASTOLIC",0);
int mHeartRate = intent.getIntExtra("HEARTRATE",0);
String mComment = intent.getStringExtra("COMMENT");
editTextDate.setText(mDate);
editTextTime.setText(mTime);
editTextSystolic.setText(""+mSystolic);
editTextDiastolic.setText(""+mDiastolic);
editTextHeartRate.setText(""+mHeartRate);
editTextComment.setText(mComment);
*/