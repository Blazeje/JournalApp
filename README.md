# Journal App
A small Kotlin/Jetpack Compose app that allows users to record and view short video clips.

## Tech Stack

- **Language:** Kotlin 2.3.10
- **UI:** Jetpack Compose + Fragments
- **Dependency Injection:** Koin
- **Architecture:** MVI (Model-View-Intent)
- **Database:** SQLDelight
- **Media:** CameraX (Recording), Media3 / ExoPlayer (Playback)
- **Coroutines:** Kotlinx.coroutines
- **Build Tools:** AGP 9.0.0, JDK 21
- **Minimum SDK:** 27, Target SDK: 36

## **What’s Done ✅**

- **Modular Architecture:** Clean separation into `app`, `ui`, `video`, `domain`, `data`, and `shared` modules.
- **Video Recording:** Integrated CameraX with real-time preview and video capture.
- **Video Playback:** Seamless playback using Media3 / ExoPlayer in the journal feed.
- **Local Persistence:** SQLDelight used for storing video metadata.
- **Permission Management:** Robust handling of Camera and Audio permissions.
- **Sharing:** Social sharing of recorded videos via Android's `ACTION_SEND`.
- **DI:** Fully powered by Koin for dependency injection.
- **MVI Pattern:** Reactive UI using State, Event, and Effect streams.

---

## **Next Steps 🚀**

1. Expand **unit tests** coverage for ViewModels and Repositories.

---

## **How to Run**

1. Clone the project.
2. Open in Android Studio (Ladybug or newer).
3. Build and run the `:app` module.
4. Grant permissions to start recording your journal entries!

---

## **Project Structure**

- `:app` - Entry point and Fragment container.
- `:ui` - Journal feed screen and UI components.
- `:video` - Camera recording and playback implementation.
- `:domain` - Use cases and repository interfaces.
- `:data` - SQLDelight database and repository implementations.
- `:shared` - Common models and utilities.
