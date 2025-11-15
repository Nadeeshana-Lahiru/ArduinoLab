package com.example.arduinolab;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.viewHolder>{

    Context context;
    public ArrayList<TutorialItem> tutorialList;

    public ProjectAdapter(Context context, ArrayList<TutorialItem> tutorialList) {
        this.context = context;
        this.tutorialList = tutorialList;
    }

    @NonNull
    @Override
    public ProjectAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.viewHolder holder, int position) {
        TutorialItem item = tutorialList.get(position);
        holder.headerText.setText(item.getTitle());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowProjectDetails.class);
                intent.putExtra("TUTORIAL_ITEM", item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tutorialList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView headerText;
        RelativeLayout itemLayout;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            headerText = itemView.findViewById(R.id.headerTextID);
            itemLayout = itemView.findViewById(R.id.single_relative_layout_ID);

        }
    }
}
