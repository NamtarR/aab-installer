# AAB Installer

As you probably know, some time ago Google introduced a new and preferred way of building Android apps - an [Android App Bundle](https://developer.android.com/guide/app-bundle). While its benefits are understandable, and it integrates seamlessly with the Play Store, there is no way to install it directly to a device. In order to do that, one would have to first convert an AAB to an APKS file, which is a set of APKs for each and every configuration that the app supports, and then retrieve those APKs that match the device configuration to install on.
AAB Installer simplifies this process using Google's [bundletool](https://developer.android.com/studio/command-line/bundletool) and [ADB](https://developer.android.com/studio/command-line/adb) with a GUI, and makes it easier for non-developers to install multiple bundles if necessary.

Furthermore, the idea seemed like the perfect opportunity to dive into [Compose](https://www.jetbrains.com/ru-ru/lp/compose-mpp/), so this project also serves as a small playground.

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/NamtarR/aab-installer/blob/master/LICENSE.md)

#### What you can do with this app

- Build an Android App Bundle and install it on a device 
- Manage signing configs for multiple projects
- Check each command result in the console

#### Usage instructions

- Download and install Java 11+, if needed
- Download AAB Installer
- Download and install [ADB](https://developer.android.com/studio/command-line/adb)
- Download and copy [bundletool](https://developer.android.com/studio/command-line/bundletool) to wherever you like
- Run AAB Installer and in the Settings tab set ADB and bundletool paths

  <img src="https://raw.githubusercontent.com/NamtarR/aab-installer/main/images/Settings.png" width="600" />
  

- In the signing tab add a signing config for your app. You will have to have it in order to build an installable APK. 
It now supports environment variables in config fields. In order to use them surround variable name with double curly braces.
For example, `{{AWESOME_KEYSTORE_PASSWORD}}`.
  <img src="https://raw.githubusercontent.com/NamtarR/aab-installer/main/images/Signing.png" width="600" />
  

- In the Build tab select an AAB to install, select your device, signing config, and you are good to go!
  <img src="https://raw.githubusercontent.com/NamtarR/aab-installer/main/images/Install.png" width="600" />

#### What's next
- Linux and MacOS versions _(They are probably fine, but I just have to check that)_
- ~~Ability to use keystore and key passwords from environment variables~~
- ~~Universal APK mode~~
- Automatic ADB and Bundletool paths discovery
- Building using device specs

#### License
AAB Installer is distributed under the MIT license. See [LICENSE](https://github.com/NamtarR/aab-installer/blob/master/LICENSE.md) for details.
