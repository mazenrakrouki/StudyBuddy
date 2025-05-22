package com.example.studybuddy;

import java.util.HashMap;
import java.util.Map;

public class StudySession {
    private String id;
    private String subject;
    private String description;
    private String level;
    private String date;
    private String location;
    private String creatorId;
    private Map<String, Boolean> participants;

    // Constructeur vide obligatoire pour Firebase
    public StudySession() {
        participants = new HashMap<>();
    }

    // Constructeur avec paramètres
    public StudySession(String subject, String description, String level,
                        String date, String location, String creatorId) {
        this.subject = subject;
        this.description = description;
        this.level = level;
        this.date = date;
        this.location = location;
        this.creatorId = creatorId;
        this.participants = new HashMap<>();
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCreatorId() { return creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; }

    public Map<String, Boolean> getParticipants() {
        return participants;
    }

    // Méthode pour ajouter un participant
    public void addParticipant(String userId) {
        participants.put(userId, true);
    }
    public void removeParticipant(String userId) {
        participants.remove(userId);
    }
}