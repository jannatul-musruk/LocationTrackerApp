 Location Tracker App

This Android application is developed as a technical task for the Android Intern position at ATI Limited.

 Features:

1. Background Location Service
- Fetches device location every 5 minutes
- Works even when app is closed or removed from recent apps
- Runs as a foreground service
- Shows location using Toast message
- Saves latitude, longitude and timestamp into Room database

2. API Integration
- Fetches data from a free public API (Chuck Norris API)
- Displays API response in the UI
- Handles network errors properly

3. Technology Used
- Java
- XML
- Room Database
- Foreground Service
- Fused Location Provider API

 📂 Project Structure
- MainActivity: UI and permission handling
- LocationService: Background location tracking
- Room Database: Local data persistence

# Main Files
- `MainActivity.java` → Handles UI, button clicks, permission requests, API calls, starts/stops location service
- `LocationService.java` → Background & foreground service, fetches location, shows Toast, saves data to database
- `LocationEntity.java` → Room database table structure (latitude, longitude, timestamp)
- `LocationDao.java` → Database operations (insert, read locations)
- `AppDatabase.java` → Room database configuration (singleton instance)

# Layouts (res/layout)
- `activity_main.xml` → Main screen UI (buttons + API result TextView)
- `item_location.xml` → RecyclerView row layout for saved locations

#Config & Gradle
- `AndroidManifest.xml` → Permissions, service declaration, app entry point
- `build.gradle` (app) → App dependencies (Room, Location, RecyclerView), SDK versions
- `build.gradle` (project) → Project-wide Gradle config
- `gradle/` → Gradle wrapper files


# How to Run
1. Clone the repository
2. Open in Android Studio
3. Allow location permission
4. Click "Start Location"
5. Location will be saved automatically

# Developer
Name: Jannatul Musruk
Flow:
Start Location
   ↓
Permission OK
   ↓
Foreground Service start
   ↓
Notification shows
   ↓
Every 5 min:
   LocationCallback
        ↓
   DB insert (Room)
        ↓
   Toast / Notification
Location Tracker App – Full Summary:
LocationTrackerApp/
├── app/
│   ├── manifests/
│   │   └── AndroidManifest.xml
│   ├── java/
│   │   └── com.example.locationtrackerapp/
│   │       ├── MainActivity.java
│   │       ├── LocationService.java
│   │       ├── AppDatabase.java
│   │       └── LocationEntity.java
│   └── res/
│       ├── layout/
│       │   └── activity_main.xml
│       └── values/
│           └── colors.xml, strings.xml, styles.xml
└── build.gradle (Project & Module)

