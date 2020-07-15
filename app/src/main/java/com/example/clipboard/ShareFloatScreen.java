package com.example.clipboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShareFloatScreen extends AppCompatActivity implements ProjectsAdapter.OnProjectListener {

    private ArrayList<String> mProjectNames = new ArrayList<>();

    private Intent intent;
    private String action;
    private String type;

    String newFloatName;

    String TAG = "ShareFloatScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_shared_float);

        intent = getIntent();
        action = intent.getAction();
        type = intent.getType();

        // Delete this later
        trialCode();

        RecyclerView rvProjects = findViewById(R.id.rvProjectsShareView);
        ProjectsAdapter adapter = new ProjectsAdapter(mProjectNames, this);
        rvProjects.setAdapter(adapter);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onProjectClick(int position) {
        createFloat(mProjectNames.get(position));
    }

    // Delete this later
    private void trialCode() {
        mProjectNames.add("Alpha");
        mProjectNames.add("Beta");
        mProjectNames.add("Gamma");
    }

    private void createFloat(String projectName) {
        if(Intent.ACTION_SEND.equals(action) && type != null) {
            if("text/plain".equals(type)) {
                newFloatName = getNewFloatName(".txt");
                createTextFloat(projectName);
            }
        }
    }

    private String getNewFloatName(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDate = Calendar.getInstance().getTime();
        String fileName = dateFormat.format(currentDate) + format;
        return fileName;
    }

    void createTextFloat(String projectName) {
        File projectDirectory = new File(getApplicationContext().getFilesDir(), projectName);
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);

        // Create a new text file
        if(sharedText != null) {
            try {
                File textFile = new File(projectDirectory, newFloatName);
                FileWriter fileWriter = new FileWriter(textFile);
                fileWriter.append(sharedText);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Append the file record in registrar file
        try {
            File file = new File(projectDirectory, "registrar.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(newFloatName + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }
}
