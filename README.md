StudyBuddy

StudyBuddy is an Android application designed to connect students for collaborative study sessions. Built using Java and Firebase, the app offers a platform where students can either create or join study sessions based on subjects, levels, locations, and dates.

🚀 Features

✍️ Student & Tutor Roles

🎓 Create and join study sessions

📅 Filter sessions by subject, level, or date (future feature)

📊 View available sessions in a RecyclerView

❌ Logout functionality

📑 Firebase Realtime Database integration

🔐 Firebase Authentication

📄 Technologies Used

Java

Android SDK (API 27+)

Firebase Realtime Database

Firebase Authentication

RecyclerView

ConstraintLayout

Gradle Kotlin DSL (build.gradle.kts)

📦 Installation & Setup

Clone the Repository:

git clone https://github.com/<your-username>/StudyBuddy.git

Open in Android Studio

Configure Firebase:

Create a Firebase project.

Add your Android app in Firebase Console.

Download google-services.json and place it in app/ directory.

Enable Firebase Authentication (Email/Password).

Set up Realtime Database rules.

Sync Gradle & Run

💡 Architecture Overview

StudentDashboardActivity.java: Displays all sessions to students.

StudySession.java: Data model for a study session.

StudentSessionAdapter.java: Binds session data to RecyclerView.

LoginActivity.java: Handles user login (to be implemented).

RegisterActivity.java: Handles user registration (to be implemented).

📆 Project Requirements (Academic)

At least 2 actors (Student & Tutor)

At least 3 activities (Login, Dashboard, Session Details)

RecyclerView for displaying sessions

Firebase Realtime Database or SQLite integration

Fully commented code

GitHub repository with README

📓 License

This project is part of an academic requirement and is licensed for educational use only.
