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
    private static final int THIRD_ACTIVITY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MeasurementActivity.class);
                intent.putExtra("TYPE", 1); // 1 = add, 2 = clicked
                startActivityForResult(intent,SECOND_ACTIVITY_REQUEST_CODE);
            }
        });
        mRecyclerView = findViewById(R.id.recylerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMeasurementList = new ArrayList<>();
        loadMeasurementFromFile();
    }

    private void loadMeasurementFromFile() {
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
        Log.d("test","should work");
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
        super.onActivityResult(requestCode, resultCode, data);
        // Check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == 1) { //add
                // Get String data from Intent
                String returnDate = data.getStringExtra("DATE");
                String returnTime = data.getStringExtra("TIME");
                int returnSystolic = data.getIntExtra("SYSTOLIC", 0);
                int returnDiastolic = data.getIntExtra("DIASTOLIC", 0);
                int returnHeartrate = data.getIntExtra("HEARTRATE", 0);
                String returnComment = data.getStringExtra("COMMENT");

                MeasurementItem newItem = new MeasurementItem(returnDate, returnTime, returnSystolic, returnDiastolic, returnHeartrate, returnComment);
                mMeasurementList.add(newItem);
                mMeasurementAdapter.notifyDataSetChanged();
                saveMeasurementInFile();
            }
            if (resultCode == 2) { //edit
                // Get String data from Intent and Store
                clickedItem.newDate(data.getStringExtra("DATE"));
                clickedItem.newTime(data.getStringExtra("TIME"));
                clickedItem.newSystolicPressure(data.getIntExtra("SYSTOLIC",0));
                clickedItem.newDiastolicPressure(data.getIntExtra("DIASTOLIC",0));
                clickedItem.newHeartRate(data.getIntExtra("HEARTRATE",0));
                clickedItem.newComment(data.getStringExtra("COMMENT"));
            }
            if (resultCode == 3){ //delete
                mMeasurementList.remove(clickedItem);
            }
            mMeasurementAdapter.notifyDataSetChanged();
            saveMeasurementInFile();
        }
    }

}
