# 🌄 PAPER ROCK Wallpapers

A high-performance modern Android wallpaper application featuring curated HD rock art and minimalist wallpapers. This project includes a robust, fully automated CI/CD pipeline, secure dependency management via Dependabot, and play-store ready builds.

---

## 🚀 Features

- **Pristine HD Wallpapers**: High-resolution curated wallpapers optimized for modern phone displays.
- **Fast Image Loading & Caching**: Efficient loading and caching mechanism to minimize network requests.
- **Minimalist Aesthetic**: Material Design 3 (M3) compliant interface with modern typography and animations.
- **Automated CI/CD**: Seamless APK and AAB build pipelines with automatic releasing.
- **Robust Security**: Automated dependency tracking and security patches configured via Dependabot.

---

## 📦 Download APK

### 🔹 Stable Release
For the latest official release, head to the Releases page:
👉 **[GitHub Releases](https://github.com/SayanthRock/Rock-wallpapers-HD-/releases)**

Under the latest release, download:
- `app-release.apk`

---

### 🔹 Development Builds (CI Artifacts)
An experimental build is compiled automatically on every push to the `main` branch.

To download the latest development build:
1. Navigate to the **[GitHub Actions](https://github.com/SayanthRock/Rock-wallpapers-HD-/actions)** tab.
2. Select the most recent workflow run.
3. Scroll down to the **Artifacts** section and download:
   - `app-debug-apk` or `app-release-aab`

---

## ⚙️ CI/CD Pipeline Configuration

The repository uses GitHub Actions to automate compiles, tests, and deployments:

- **Build Validation**: Automatically runs on all Pull Requests and Pushes targeting the `main` branch.
- **Artifact Publishing**: Compiles debug and signed release artifacts automatically.
- **Automated Releases**: Generates a GitHub Release draft and attaches the compiled binaries whenever a version tag (e.g., `v1.0.0`) is pushed.

### How to trigger a new release:
```bash
# Tag the current commit with a semver version
git tag v1.0.0

# Push the tag to trigger the automatic release workflow
git push origin v1.0.0
```

---

## 🔐 Automated Dependency Auditing

We use **Dependabot** to continuously audit dependencies for security vulnerabilities and keep the build stable:
- **Weekly Checks**: Configured to run checkups on Gradle and npm ecosystems.
- **Strict Exclusions**: Core system packages (like KSP and Kotlin compiler plug-ins) are pinned or restricted to non-breaking version boundaries to prevent automated builds from breaking suddenly.
- **Grouped Security Updates**: Security fixes are grouped together to reduce PR spam.

The configuration file is located at `.github/dependabot.yml`.

---

## 🛠 Local Development Setup

To compile and inspect the application locally:

### Prerequisites:
- JDK 17
- Android Studio Koala+ or CLI tools

### Build Commands:
```bash
# Clone the repository
git clone https://github.com/SayanthRock/Rock-wallpapers-HD-.git
cd Rock-wallpapers-HD-

# Grant executable permission to the gradle wrapper
chmod +x ./gradlew

# Compile a Debug build
./gradlew assembleDebug
```

The output file will be written to `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🧪 Technology Stack

- **Platform**: Native Android (Kotlin & Jetpack Compose)
- **Design System**: Material Design 3 (M3)
- **CI/CD Orchestration**: GitHub Actions
- **Dependency Automation**: Dependabot
- **Java Platform**: OpenJDK 17
- **Build System**: Gradle 8.4 Wrapper with Kotlin DSL

---

## 📜 License

This project is licensed under the Open Source License - see the LICENSE file for details.
