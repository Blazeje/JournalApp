# Mini Video Journal App
A small Kotlin/Jetpack Compose app that allows users to record and view short video clips.

## Tech Stack

- **Language:** Kotlin 2.3
- **UI:** Jetpack Compose
- **Dependency Injection:** Hilt
- **Testing:** JUnit, MockK
- **Architecture:** MVI (Model-View-Intent)
- **Networking:** Retrofit + Moshi
- **Coroutines:** Kotlinx.coroutines
- **Code Quality:** Ktlint (formatting & linting)
- **Media3 / ExoPlayer** (planned)
- **SQLDelight** (planned)
- **Build Tools:** AGP 8.9.1, JDK 21
- **Minimum SDK:** 27, Target SDK: 36

## **What’s Done ✅**

- App builds and runs successfully
- `MainActivity` launches `FirstFragment` from `ui` module
- `FeedViewModel` implemented with Hilt
- `FeedScreen` Compose UI:
  - `LazyColumn` showing placeholder videos
  - Button to add a placeholder video
- Module dependencies wired correctly:
  - `app → ui, domain, data, video`
  - `ui → domain`
  - `data → domain` (repository implementation injected via Hilt)

---

## **Next Steps 🚀**

1. Implement actual **video recording** using device camera
2. Handle **permissions** (Camera & Storage)
3. Replace placeholder videos with real recorded clips
4. Implement **video playback** using Media3 / ExoPlayer
5. Add **video thumbnails, sharing, Material theming**
6. Add **unit tests** for ViewModels and UseCases
7. (Optional) CI/CD using GitHub Actions

---

## **How to Run**

1. Clone the project
2. Open in Android Studio
3. Build and run the `:app` module
4. The first screen shows a feed of videos (currently placeholder videos)

---

## **DI / Hilt Setup**

- `VideoRepositoryImpl` in `data` module
- Repository interface in `domain` module
- Hilt provides implementations to `ui` via constructor injection
- `FeedViewModel` uses `GetVideosUseCase` & `RecordVideoUseCase` from `domain`
