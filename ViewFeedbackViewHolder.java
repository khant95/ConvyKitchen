package com.example.convykitchen;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

class ViewFeedbackViewHolder extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    TextView date,name,feedback,username;
    public ViewFeedbackViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage=itemView.findViewById(R.id.circleImageView);
        date=itemView.findViewById(R.id.date);
        feedback=itemView.findViewById(R.id.feedback);
        name=itemView.findViewById(R.id.name);
        username=itemView.findViewById(R.id.username);
    }
}
