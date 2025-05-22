package com.example.studybuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TutorSessionAdapter extends RecyclerView.Adapter<TutorSessionAdapter.SessionViewHolder> {

    private List<StudySession> sessions;
    private OnDeleteClickListener deleteClickListener;
    private Context context;

    public interface OnDeleteClickListener {
        void onDeleteClick(StudySession session);
    }

    public TutorSessionAdapter(List<StudySession> sessions, OnDeleteClickListener listener, Context context) {
        this.sessions = sessions;
        this.deleteClickListener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tutor_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        StudySession session = sessions.get(position);
        holder.bind(session);

        // Delete button handling
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(session);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessions != null ? sessions.size() : 0;
    }

    static class SessionViewHolder extends RecyclerView.ViewHolder {
        private final TextView textSubject, textDescription, textLevel, textDate, textLocation;
        private final Button btnDelete;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            textSubject = itemView.findViewById(R.id.textSubject);
            textDescription = itemView.findViewById(R.id.textDescription);
            textLevel = itemView.findViewById(R.id.textLevel);
            textDate = itemView.findViewById(R.id.textDate);
            textLocation = itemView.findViewById(R.id.textLocation);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(StudySession session) {
            textSubject.setText(session.getSubject());
            textDescription.setText(session.getDescription());
            textLevel.setText(session.getLevel());
            textDate.setText(session.getDate());
            textLocation.setText(session.getLocation());
        }
    }
}