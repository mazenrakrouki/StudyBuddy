package com.example.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class StudentSessionAdapter extends RecyclerView.Adapter<StudentSessionAdapter.ViewHolder> {
    private List<StudySession> sessions;
    private String currentUserId;
    private OnJoinClickListener joinClickListener;
    private Context context;

    public interface OnJoinClickListener {
        void onJoin(StudySession session);
    }

    public StudentSessionAdapter(List<StudySession> sessions,
                                 String currentUserId,
                                 OnJoinClickListener joinClickListener,
                                 Context context) {
        this.sessions = sessions;
        this.currentUserId = currentUserId;
        this.joinClickListener = joinClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_session, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudySession session = sessions.get(position);

        // Populate views with session data
        holder.textSubject.setText(session.getSubject());
        holder.textDescription.setText(session.getDescription());
        holder.textLevel.setText(session.getLevel());
        holder.textDate.setText(session.getDate());
        holder.textLocation.setText(session.getLocation());

        holder.btnJoin.setOnClickListener(v -> {
            if (joinClickListener != null) {
                joinClickListener.onJoin(session);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessions == null ? 0 : sessions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSubject, textDescription, textLevel, textDate, textLocation;
        Button btnJoin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textSubject = itemView.findViewById(R.id.textSubject);
            textDescription = itemView.findViewById(R.id.textDescription);
            textLevel = itemView.findViewById(R.id.textLevel);
            textDate = itemView.findViewById(R.id.textDate);
            textLocation = itemView.findViewById(R.id.textLocation);
            btnJoin = itemView.findViewById(R.id.btnJoin);
        }
    }
}