package com.example.clipboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<String> mProjectNames;
    private OnProjectListener mOnProjectListener;

    public ProjectsAdapter(ArrayList<String> projectNames, OnProjectListener onProjectListener) {
        mProjectNames = projectNames;
        mOnProjectListener = onProjectListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnProjectListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Fill Project Name
        holder.projectName.setText(mProjectNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mProjectNames.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemSwiped(int position) {

    }

    public interface OnProjectListener {
        void onProjectClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView projectName;
        OnProjectListener onProjectListener;

        public ViewHolder(@NonNull View itemView, OnProjectListener onProjectListener) {
            super(itemView);

            projectName = itemView.findViewById(R.id.project_name);

            this.onProjectListener = onProjectListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProjectListener.onProjectClick(getAdapterPosition());
        }
    }
}
