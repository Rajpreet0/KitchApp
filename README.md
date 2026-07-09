# KitchApp

A native Android app (Kotlin) that helps you track what's in your kitchen — fridge, freezer, and pantry — and get AI-generated recipe suggestions based on what you have (or a set of preferences). Built as part of a mobile application development course project (FRA-UAS).

## Features

- **Account system** — register, log in, forgot/reset password, delete account, session persisted locally for 24 hours
- **Kitchen inventory tracking** — separate lists for the **fridge**, **freezer**, and **pantry**, each letting you add, edit, and delete ingredients (with quantity/unit)
- **Grocery list** — a shopping list view for items to buy
- **Recipe suggestions** — set preferences (portions, category, time, complexity, cuisine/nationality, ingredients to include/exclude, special requirements) or use "Surprise me" to get an AI-generated recipe from the backend
- **Recipe display & saving** — view a suggested recipe's full details and save favorites for later
- **Excluded ingredients** — maintain a list of ingredients you never want suggested (e.g. allergies, dislikes)
- **Profile & settings** — update username/language, manage account

## Architecture

- **Platform:** Native Android, written in Kotlin, using classic `Activity`-based navigation (no Jetpack Compose)
- **Networking:** OkHttp + Gson, talking directly to a remote REST API (see below) — no Retrofit/Room
- **Local persistence:** Fridge/freezer/pantry/grocery lists and excluded ingredients are stored on-device using `SharedPreferences` with Gson-serialized JSON; login session state is also kept in `SharedPreferences` via `SessionManager`
- **Backend:** A separate hosted API at `https://kitch-app-server.vercel.app` handles authentication and AI recipe suggestions (this repository is the Android client only — the backend is a separate project)

### Backend API used by the app

| Endpoint | Purpose |
|---|---|
| `POST /users/login` | Log in with email + password |
| `POST /users/register` | Register a new account (username, email, password) |
| `POST /users/delete` | Delete a user account |
| `POST /users/findUser` | Look up a user for the "forgot password" flow |
| `POST /users/updatePassword` | Set a new password |
| `POST /recipes` | Get a recipe suggestion based on submitted preferences (or "surprise me") |

## Project Structure

```
app/src/main/java/.../kitchapp/
  activities/            One Activity per screen (login, register, home, fridge, freezer,
                          pantry, grocery, recipes, recipe preferences, recipe display,
                          suggestions, exclude ingredients, profile, password reset flow)
  fragments/             Shared dialog fragments (e.g. loading spinner)
  helpers/
    NetworkHelper.kt      OkHttp client wrapping all backend API calls
    SessionManager.kt     SharedPreferences-based login session storage
    ValidationUtil.kt     Form validation for auth and recipe-preference screens
app/src/main/res/
  layout/                XML layouts, one per Activity/list-row/dialog
  drawable/               Icons and backgrounds
  font/                   Custom fonts (light/regular/medium/bold)
  values/                 Strings, colors, themes
```

## Getting Started

### Prerequisites

- Android Studio (recent version)
- JDK 8+ (project targets Java 8 compatibility)
- An Android device or emulator running **API 28 (Android 9)** or later

### Build & run

1. Open the project folder in Android Studio and let Gradle sync (uses the Gradle wrapper, `compileSdk 34`).
2. Run the app on an emulator or physical device via the standard **Run** button, or from the command line:

```bash
./gradlew assembleDebug
```

The debug build installs and launches from `ActivityLogin`, the app's entry point.

3. The app talks to the hosted backend at `https://kitch-app-server.vercel.app` by default (configured in `NetworkHelper.kt`) — no additional setup is required to try it out, though recipe suggestions and auth depend on that service being available.

### Tests

- `app/src/test` — local JUnit unit tests
- `app/src/androidTest` — instrumented tests (Espresso), run on a device/emulator

```bash
./gradlew test
./gradlew connectedAndroidTest
```

## CI/CD

A GitHub Actions workflow (`.github/workflows/kitchappCICD.yml`) builds and validates the app on push/PR.


