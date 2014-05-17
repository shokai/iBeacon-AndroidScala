import android.Keys._

android.Plugin.androidBuild

name := "iBeaconService"

platformTarget in Android := "android-18"

run <<= run in Android

install <<= install in Android
