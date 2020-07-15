package com.example.clipboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class IndividualProject extends AppCompatActivity {

    private static Context context;
    private ArrayList<Float> mAllFloats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_project);

        context = getApplicationContext();

        Intent intent = getIntent();
        String projectName = intent.getStringExtra("projectName");

        try {
            getFloatsToFill(projectName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        RecyclerView rvFloats = findViewById(R.id.rvFloats);
        FloatsAdapter adapter = new FloatsAdapter(mAllFloats, projectName, context);
        rvFloats.setAdapter(adapter);
        rvFloats.setLayoutManager(new LinearLayoutManager(this));

        // To handle swipe on a Float
        FloatTouchHelper floatTouchHelper = new FloatTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(floatTouchHelper);
        adapter.setTouchHelper(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(rvFloats);
    }

    private void getFloatsToFill(String projectName) throws FileNotFoundException {

        // List all files inside projectName directory
        ArrayList<String> allFloatsPresent = getFloatFileNames(projectName);

        for(String pathName:allFloatsPresent) {

            // Handle text file by filling their data in Float
            if (pathName.endsWith(".txt")) {
                File file = new File(pathName);
                StringBuilder stringBuilder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    while (line != null) {
                        stringBuilder.append(line).append('\n');
                        line = reader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    String contents = stringBuilder.toString();
                    mAllFloats.add(new Float(contents));
                }
            }
        }
    }

    // Get the names of Float files to open
    private ArrayList<String> getFloatFileNames (String projectName)
    {
        ArrayList<String> list = new ArrayList<>();

        File projectDirectory = new File(context.getFilesDir(), projectName);
        File registrarFile = new File(projectDirectory, "registrar.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(registrarFile))) {
            String line = reader.readLine();
            while (line != null) {
                list.add(context.getFilesDir() + File.separator
                        + projectName + File.separator + line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}

