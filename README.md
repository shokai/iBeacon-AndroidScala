iBeacon for Android Scala
=========================

- https://github.com/shokai/iBeacon-AndroidScala


Dependencies
------------
- "android-18" SDK (for Android 4.3)
- Bluetooth LE
- scala & sbt
- [scala android-sdk-plugin](https://github.com/pfn/android-sdk-plugin)


Install Dependencies
--------------------

    % brew update
    % brew install scala sbt


Usage
-----

copy `IBeacon.scala` into your app.


```scala
import org.shokai.ibeacon.{IBeacon, Beacon};
```

```scala
class MainActivity extends Activity{

  lazy val iBeacon:IBeacon = new IBeacon(this)

  override def onCreate(savedInstanceState:Bundle){

    // capture all beacon packet
    iBeacon.onBeacon((beacon:Beacon) =>
      Log.v("iBeacon", s"UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
    )


    // when beacon appear
    iBeacon.onDetect((beacon:Beacon) =>
      Log.v("iBeacon", s"detect ${beacon}")
    )

  }

}
```

add `user-permission` into `AndroidManifest.xml`
```xml
<manifest>
  <application> ~~ your app ~~ </application>
  <uses-permission android:name="android.permission.BLUETOOTH" />
  <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
  <uses-feature android:name="android.hardware.bluetooth_le" android:required="true" />
</manifest>
```

SampleApps
----------

![sample app](http://shokai.org/archive/file/59e5175520370a366113eb39781639df.png)

![service app](http://shokai.org/archive/file/3a8bce26f1cc9bbd8ecc57d41d820509.png)


    % cd SampleApps/iBeaconReader

or

    % cd SampleApps/iBeaconService


### Build

    % android update project --path `pwd`
    % sbt
    > android:package-debug


### Install

    % adb devices
    % adb install -r iBeaconReader-debug.apk


### Develop

    % sbt "~ android:run"
