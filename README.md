# GUREGRAM cloud-build package

This is a Telegram-style Android UI prototype, not connected to Telegram servers. Chats are demo data. Do not enter Telegram login credentials.

## Build the APK using only your phone (cloud build)
1. Sign in or create a GitHub account in your phone browser: https://github.com/
2. Create a **new repository** named `GUREGRAM` and set it to **Public** (this makes the Actions flow easiest to access).
3. On the new repository page, choose **Add file → Upload files**.
4. Upload the contents of this ZIP (not the ZIP itself). Ensure `.github/workflows/android.yml` is included. If the phone upload interface hides dot-folders, use GitHub's web file creation to add `.github/workflows/android.yml` using the copy in this ZIP.
5. Commit the files to the `main` branch.
6. Open the repository's **Actions** tab. Select **Build GUREGRAM APK**. If needed, press **Run workflow**.
7. Wait for the green checkmark, open the completed workflow run, scroll to **Artifacts**, and download `GUREGRAM-debug-APK`.
8. Extract the artifact ZIP. Tap `app-debug.apk` to install. Android may ask you to allow installs from that browser/files app.

## If Actions does not appear
Open repository **Settings → Actions → General** and allow actions, then return to the Actions tab.

## Project status
This project currently builds a visual UI prototype with sample chats, search, demo folder tabs, and demo settings. It does not log into Telegram or send real messages. A real Telegram-based client requires integration with the official Telegram Android source and compliance with its license.
