iBeacon for Android Scala
=========================

- https://github.com/shokai/iBeacon-AndroidScala


Dependencies
------------
- android-18 SDK (for Android4.3)
- scala & sbt
- [scala android-sdk-plugin](https://github.com/pfn/android-sdk-plugin)


Install Dependencies
--------------------

    % brew update
    % brew install scala sbt


Usage
-----

copy `org/shokai/ibeacon/IBeacon.scala` into your app.


```scala
class MainActivity extends Activity{

  override def onCreate(savedInstanceState:Bundle){
    val iBeacon:IBeacon = new IBeacon(this)

    iBeacon.onDetect((beacon) =>
      trace(s"UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
    )
  }

}
```


SampleApps
----------

    % cd SampleApps/iBeaconReader


### Build

    % android update project --path `pwd`
    % sbt
    > android:package-debug


### Install

    % adb devices
    % adb install -r IBeaconTestApp-debug.apk


### Develop

    % sbt "~ android:run"
