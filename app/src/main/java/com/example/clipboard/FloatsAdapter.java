package com.example.clipboard;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FloatsAdapter extends RecyclerView.Adapter<FloatsAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<Float> mAllFloats;
    private ItemTouchHelper mTouchHelper;
    private String mProjectName;
    private Context mContext;

    public FloatsAdapter(ArrayList<Float> allFloats, String projectName, Context context) {
        mAllFloats = allFloats;
        mProjectName = projectName;
        mContext = context;
    }

    public void setTouchHelper(ItemTouchHelper mTouchHelper) {
        this.mTouchHelper = mTouchHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_float, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Set a text float
        holder.textFloatContent.setText(mAllFloats.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mAllFloats.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        changePositionOfRecord(fromPosition + 1, toPosition + 1);
        Float fromFloat = mAllFloats.get(fromPosition);
        mAllFloats.remove(fromPosition);
        mAllFloats.add(toPosition, fromFloat);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        removeFile(position + 1);
        removeRecord(position + 1);
        mAllFloats.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnTouchListener, GestureDetector.OnGestureListener {

        TextView textFloatContent;
        GestureDetector gestureDetector;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gestureDetector = new GestureDetector(itemView.getContext(), this);

            textFloatContent = itemView.findViewById(R.id.text_float_content);

            itemView.setOnTouchListener(this);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mTouchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
    }

    private void removeRecord(int position) {

        int currentPosition = 0;
        String pathname = mContext.getFilesDir() + File.separator + mProjectName +
                File.separator + "registrar.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathname));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                currentPosition++;
                if (currentPosition == position) {
                    continue;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            String inputString = inputBuffer.toString();

            FileOutputStream fileOutputStream = new FileOutputStream(pathname);
            fileOutputStream.write(inputString.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeFile(int position) {

        int currentPosition = 0;
        String pathname = mContext.getFilesDir() + File.separator + mProjectName +
                File.separator + "registrar.txt";
        String fileName = new String();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathname));

            while ((fileName = reader.readLine()) != null) {
                currentPosition++;
                if (currentPosition == position) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileName = mContext.getFilesDir() + File.separator + mProjectName +
                File.separator + fileName;

        File fileToDelete = new File(fileName);
        fileToDelete.delete();
    }

    private void changePositionOfRecord(int fromPosition, int toPosition) {

        int currentPosition = 0;
        String fileName = new String(); // To hold the file which is to be moved.
        String pathname = mContext.getFilesDir() + File.separator + mProjectName +
                File.separator + "registrar.txt";

        // Remove the file from old position and save its name to add later.
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathname));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                currentPosition++;
                if (currentPosition == fromPosition) {
                    fileName = line;
                    continue;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            String inputString = inputBuffer.toString();

            FileOutputStream fileOutputStream = new FileOutputStream(pathname);
            fileOutputStream.write(inputString.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Add the file to new position.
        currentPosition = 0;
        Boolean fileAdded = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathname));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = reader.readLine()) != null) {
                currentPosition++;
                if (currentPosition == toPosition) {
                    inputBuffer.append(fileName);
                    inputBuffer.append('\n');
                    fileAdded = true;
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            if(!fileAdded)
            {
                inputBuffer.append(fileName);
                inputBuffer.append('\n');
            }

            String inputString = inputBuffer.toString();

            FileOutputStream fileOutputStream = new FileOutputStream(pathname);
            fileOutputStream.write(inputString.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
