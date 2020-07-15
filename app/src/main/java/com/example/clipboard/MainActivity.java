package com.example.clipboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ProjectsAdapter.OnProjectListener {

    private static Context context;
    private ArrayList<String> mProjectNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        // Delete this later
        trialCode();

        addProjectNames();

        RecyclerView rvProjects = findViewById(R.id.rvProjects);
        ProjectsAdapter adapter = new ProjectsAdapter(mProjectNames, this);
        rvProjects.setAdapter(adapter);
        rvProjects.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onProjectClick(int position) {
        Intent intent = new Intent(this, IndividualProject.class);
        intent.putExtra("projectName", mProjectNames.get(position));
        startActivity(intent);
    }

    // Delete this later
    private void trialCode() {
        //addProjectNames1();
        //createProjectDirectories();
        //writeToFilesInsideProjects();
    }

    // Delete this later
    private void addProjectNames1() {
        /*mProjectNames.add("Alpha");
        mProjectNames.add("Beta");
        mProjectNames.add("Gamma");*/

        try {
            File textFile = new File(context.getFilesDir(), "registrar.txt");
            FileWriter fileWriter = new FileWriter(textFile);
            fileWriter.append("Alpha\n");
            fileWriter.append("Beta\n");
            fileWriter.append("Gamma\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addProjectNames() {
        File mainRegistrarFile = new File(context.getFilesDir(), "registrar.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(mainRegistrarFile))) {
            String line = reader.readLine();
            while (line != null) {
                mProjectNames.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete this later
    private void createProjectDirectories() {
        File directory1 = new File(getApplicationContext().getFilesDir()
                + File.separator + "Alpha");
        directory1.mkdirs();
        File directory2 = new File(getApplicationContext().getFilesDir()
                + File.separator + "Beta");
        directory2.mkdirs();
        File directory3 = new File(getApplicationContext().getFilesDir()
                + File.separator + "Gamma");
        directory3.mkdirs();
    }

    // Delete this later
    private void writeToFilesInsideProjects() {
        File file = new File(getApplicationContext().getFilesDir(), "Alpha");

        String content = "20200526140157.txt\n20200526140205.txt\n20200526140213.txt\n" +
                "20200526140222.txt\n20200526140233.txt";

        try {
            File textFile = new File(file, "registrar.txt");
            FileWriter fileWriter = new FileWriter(textFile);
            fileWriter.append(content);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
