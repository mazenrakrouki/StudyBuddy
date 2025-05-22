package com.example.studybuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.SessionViewHolder> {

    private List<StudySession> sessions;
    private OnJoinClickListener joinClickListener;

    public interface OnJoinClickListener {
        void onJoinClick(StudySession session);
    }

    public SessionAdapter(List<StudySession> sessions, OnJoinClickListener listener) {
        this.sessions = sessions;
        this.joinClickListener = listener;
    }

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session, parent, false);
        return new SessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        StudySession session = sessions.get(position);
        holder.bind(session);

        holder.btnJoin.setOnClickListener(v -> {
            if (joinClickListener != null) {
                joinClickListener.onJoinClick(session);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessions != null ? sessions.size() : 0;
    }

    public void updateSessions(List<StudySession> newSessions) {
        sessions = newSessions;
        notifyDataSetChanged();
    }

    static class SessionViewHolder extends RecyclerView.ViewHolder {
        private final TextView textSubject;
        private final TextView textDescription;
        private final TextView textLevel;
        private final TextView textDate;
        private final TextView textLocation;
        private final Button btnJoin;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            textSubject = itemView.findViewById(R.id.textSubject);
            textDescription = itemView.findViewById(R.id.textDescription);
            textLevel = itemView.findViewById(R.id.textLevel);
            textDate = itemView.findViewById(R.id.textDate);
            textLocation = itemView.findViewById(R.id.textLocation);
            btnJoin = itemView.findViewById(R.id.btnJoin);
        }

        public void bind(StudySession session) {
            textSubject.setText(session.getSubject());
            textDescription.setText(session.getDescription());
            textLevel.setText(session.getLevel());
            textDate.setText(session.getDate());
            textLocation.setText(session.getLocation());

            // Masquer le bouton si l'utilisateur est déjà inscrit
            if (session.getParticipants() != null &&
                    session.getParticipants().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                btnJoin.setEnabled(false);
                btnJoin.setText("Déjà inscrit");
            } else {
                btnJoin.setEnabled(true);
                btnJoin.setText("Rejoindre");
            }
        }
    }
}