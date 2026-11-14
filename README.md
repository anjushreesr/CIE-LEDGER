# 📋 CIE Ledger – Student Marks Management App

> A modern Android app that helps teachers record student internal marks, calculate CIE automatically, and check exam eligibility instantly.

![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/Language-Java-ED8B00?logo=openjdk&logoColor=white)
![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?logo=firebase&logoColor=black)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-6C3DD3)
![Min SDK](https://img.shields.io/badge/Min%20SDK-26-blue)

---

## 📖 About the Project

Managing internal marks on paper or in spreadsheets is slow and error-prone. **CIE Ledger** replaces that with a clean mobile app where a teacher can create a class, enter marks for every student, and see the final CIE and eligibility status update live.

---

## ✨ Features

- 🔐 **Authentication** – Email/password sign up and login with Firebase Auth, auto-login, and password reset
- 🏠 **Dashboard** – Total classes count and a list of all your classes
- 📚 **Class Management** – Create, edit, and delete classes (course, semester, section, type, number of students)
- 🧪 **Theory / Integrated** – Choose the subject type; Integrated subjects get an extra Lab column
- 📊 **Marks Entry** – Spreadsheet-style table with a sticky header (USN, Name, I1, I2, LA1, LA2, Lab, Attendance)
- 🧮 **Auto CIE Calculation** – Totals update live as you type
- ✅ **Eligibility Status** – Shows `ELIGIBLE` / `NOT ELIGIBLE` for each student
- 🛡️ **Input Validation** – Marks above the allowed maximum are highlighted and capped
- 👤 **Profile** – View account details, edit name, reset password, logout
- ⚙️ **Settings** – Dark mode toggle, CIE rules, app version
- 💾 **Offline First** – All data is stored locally using Room Database

---

## 🧮 CIE Calculation Rules

### Theory subject

| Component | Max Marks | Reduced To |
|-----------|-----------|------------|
| Internal 1 / Internal 2 (best of two) | 50 | 30 |
| LA 1 / LA 2 (best of two) | 10 | 20 |
| **Theory CIE** | | **50** |

```
Theory CIE = (Best(I1, I2) / 50 × 30) + (Best(LA1, LA2) / 10 × 20)
```

### Integrated subject (Theory + Lab)

| Lab Component | Max Marks |
|---------------|-----------|
| Observation | 10 |
| Record | 10 |
| Lab Internal | 30 |
| **Lab CIE** | **50** |

```
Final CIE = (Theory CIE + Lab CIE) / 2        → out of 50
```

### Eligibility

A student is **ELIGIBLE** only when both conditions are true:

- Attendance ≥ **85%**
- CIE ≥ **20 / 50**

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java |
| IDE | Android Studio |
| Architecture | MVVM (ViewModel + LiveData + Repository) |
| Local Database | Room (SQLite) |
| Authentication | Firebase Authentication |
| Cloud Database | Firebase Firestore *(optional sync)* |
| UI | XML layouts, Material Design 3, ViewBinding |
| Lists | RecyclerView |

---

## 🏗️ Project Structure

```
com.example.cieledger
├── model
│   ├── ClassEntity.java
│   └── StudentMarks.java
├── database
│   ├── AppDatabase.java
│   ├── ClassDao.java
│   └── StudentMarksDao.java
├── repository
│   ├── ClassRepository.java
│   └── MarksRepository.java
├── viewmodel
│   ├── ClassViewModel.java
│   └── MarksViewModel.java
└── ui
    ├── SplashActivity.java
    ├── LoginActivity.java
    ├── RegisterActivity.java
    ├── MainActivity.java
    ├── AddClassActivity.java
    ├── MarksEntryActivity.java
    ├── fragments
    │   ├── HomeFragment.java
    │   ├── ProfileFragment.java
    │   └── SettingsFragment.java
    └── adapters
        ├── ClassAdapter.java
        └── MarksAdapter.java
```

---

## 🔄 App Flow

```
Launch → Login / Register → Dashboard → Create Class → Enter Marks → CIE + Eligibility
```

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest stable)
- JDK 17
- An Android device or emulator (API 26+)
- A Google account for Firebase

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/<your-username>/CIE-Ledger.git
   ```

2. **Open in Android Studio**
   `File → Open →` select the cloned folder, then wait for Gradle sync.

3. **Set up Firebase**
   - Go to the [Firebase Console](https://console.firebase.google.com) and create a project
   - Add an Android app with package name `com.example.cieledger`
   - Download `google-services.json` and place it inside the `app/` folder
   - Go to **Authentication → Sign-in method** and enable **Email/Password**
   - *(Optional)* Create a **Firestore Database** if you want cloud sync

4. **Run the app**
   Click ▶ **Run** and sign up with any email.

> ⚠️ `google-services.json` contains your project keys. Do not commit it to a public repository — add it to `.gitignore`.

---

## 🖼️ Screenshots

| Login | Register | Dashboard |
|:-----:|:--------:|:---------:|
| ![Login](screenshots/login.png) | ![Register](screenshots/register.png) | ![Dashboard](screenshots/dashboard.png) |

| Create Class | Marks Entry | Profile |
|:------------:|:-----------:|:-------:|
| ![Class](screenshots/add_class.png) | ![Marks](screenshots/marks_entry.png) | ![Profile](screenshots/profile.png) |

*(Add your own screenshots to a `screenshots/` folder and keep the file names above.)*

---

## 🔮 Future Enhancements

- ☁️ Full cloud sync with Firestore across devices
- 👩‍🏫 Teacher / department admin dashboard
- 📄 Export reports as PDF and Excel
- 🔔 Notifications for eligibility and deadlines
- 📈 Class performance analytics
- 🌐 Multi-language support

---

## 👥 Team

| Name |
|------|
| Anjushree SR |
| Anusha M Kolkar |
| Amrin Taj |

---

## 📄 License

This project was developed for academic purposes. Add a license of your choice (for example MIT) before publishing.

---

<p align="center">Made with ❤️ for educators</p>
