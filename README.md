iBeacon Test App for Android
============================
- https://github.com/shokai/iBeaconTestApp-Android


Dependencies
------------
- android-18 SDK (for Android4.3)
- sbt
- [scala android-sdk-plugin](https://github.com/pfn/android-sdk-plugin)


Install Dependencies
--------------------

    % brew update
    % brew install scala sbt


Build
-----

    % android update project --path `pwd`
    % sbt
    > android:package-debug


Install
-------

    % adb devices
    % adb install -r IBeaconTestApp-debug.apk


Develop
-------

    % sbt "~ android:run"
