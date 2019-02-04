package cc.shrestha.assignment1;

import android.content.Intent;
import android.icu.util.Measure;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements MeasurementAdapter.OnItemClickListener{
    private static final String FILENAME = "file1.sav";
    private RecyclerView mRecyclerView;
    private MeasurementAdapter mMeasurementAdapter;
    private ArrayList<MeasurementItem> mMeasurementList;
    private MeasurementItem clickedItem;
    private boolean layout_type = false;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.recylerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //initialize arraylist for the logs
        mMeasurementList = new ArrayList<>();
        //load all saved logs from file
        loadMeasurementFromFile();

        FloatingActionButton fab = findViewById(R.id.fab);
        //button to add new log
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MeasurementActivity.class);
                intent.putExtra("TYPE", 1); // 1 = add, 2 = edit log
                startActivityForResult(intent,SECOND_ACTIVITY_REQUEST_CODE);
            }
        });


    }

    private void loadMeasurementFromFile() {
        //read from file and initialize the logs to the measurement item
        try {
            FileReader input = new FileReader(new File(getFilesDir(), FILENAME));
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<MeasurementItem>>(){}.getType();
            mMeasurementList = gson.fromJson(input,type);

            mMeasurementAdapter = new MeasurementAdapter(MainActivity.this,mMeasurementList);
            mRecyclerView.setAdapter(mMeasurementAdapter);
            mMeasurementAdapter.setOnItemClickListener(MainActivity.this);
        } catch (FileNotFoundException e) {
            mMeasurementList = new ArrayList<MeasurementItem>();
        }
    }
    private void saveMeasurementInFile(){
        //save the arraylist into the save file
        try {
            FileWriter output = new FileWriter(new File(getFilesDir(),FILENAME));
            Gson gson = new Gson();
            gson.toJson(mMeasurementList, output);
            output.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    public void onItemClick(int position){
        //one of the measurement log was clicked
        //open new activity with log values to be edited/updated
        //pass values through intent
        Intent measurementIntent = new Intent(this,MeasurementActivity.class);
        clickedItem = mMeasurementList.get(position);
        measurementIntent.putExtra("TYPE",2); // 1 = add, 2 = clicked
        measurementIntent.putExtra("DATE",clickedItem.getDate());
        measurementIntent.putExtra("TIME", clickedItem.getTime());
        measurementIntent.putExtra("SYSTOLIC", clickedItem.getSystolicPressure());
        measurementIntent.putExtra("DIASTOLIC", clickedItem.getDiastolicPressure());
        measurementIntent.putExtra("HEARTRATE", clickedItem.getHeartRate());
        measurementIntent.putExtra("COMMENT", clickedItem.getComment());
        startActivityForResult(measurementIntent,SECOND_ACTIVITY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //save the added data or the updated data
        super.onActivityResult(requestCode, resultCode, data);
        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == 1 || resultCode == 2) { //add
                // Get String data from Intent
                String returnDate = data.getStringExtra("DATE");
                String returnTime = data.getStringExtra("TIME");
                int returnSystolic = data.getIntExtra("SYSTOLIC", 0);
                int returnDiastolic = data.getIntExtra("DIASTOLIC", 0);
                int returnHeartRate = data.getIntExtra("HEARTRATE", 0);
                String returnComment = data.getStringExtra("COMMENT");
                if(resultCode == 1){
                    // add the new log data into the list
                    MeasurementItem newItem = new MeasurementItem(returnDate, returnTime, returnSystolic, returnDiastolic, returnHeartRate, returnComment);
                    mMeasurementList.add(newItem);
                }
                if(resultCode == 2){
                    // save the all data that has be edited
                    // update the clicked log with the new data
                    clickedItem.newDate(returnDate);
                    clickedItem.newTime(returnTime);
                    clickedItem.newSystolicPressure(returnSystolic);
                    clickedItem.newDiastolicPressure(returnDiastolic);
                    clickedItem.newHeartRate(returnHeartRate);
                    clickedItem.newComment(returnComment);
                }
            }
            if (resultCode == 3){ //delete
                mMeasurementList.remove(clickedItem);
            }
            //let the adapter know there has been data added/deleted/edited
            mMeasurementAdapter.notifyDataSetChanged();
            saveMeasurementInFile();
        }
    }

}
