package org.shokai.ibeacon;

import android.app.Activity;
import android.content.Context;
import android.bluetooth.{BluetoothManager, BluetoothAdapter, BluetoothDevice};

class IBeacon(context:Context){

  import scala.collection.mutable.Map;
  val beacons = Map.empty[String, Beacon]
  val bluetoothManager:BluetoothManager =
    context.getSystemService(Context.BLUETOOTH_SERVICE).asInstanceOf[BluetoothManager]
  val bluetoothAdapter:BluetoothAdapter = bluetoothManager.getAdapter()

  var onDetectCallback:((Beacon) => Unit) = null
  var onBeaconCallback:((Beacon) => Unit) = null

  bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback(){
    override def onLeScan(device:BluetoothDevice, rssi:Int, scanRecord:Array[Byte]){
      val beacon:Beacon = new Beacon(rssi, scanRecord)
      if(beacon.error != null) return
      if (onBeaconCallback != null){
        onBeaconCallback(beacon)
      }
      if (onDetectCallback != null){
        if(!beacons.contains(beacon.name) ||
           beacon.createAt - beacons(beacon.name).createAt > 5000){
          onDetectCallback(beacon) // detect new beacon
        }
      }
      beacons(beacon.name) = beacon
    }
  })

  // dispatch when beacon appear
  def onDetect(callback:(Beacon) => Unit){
    this.onDetectCallback = callback
  }

  // dispatch every beacon packet
  def onBeacon(callback:(Beacon) => Unit){
    this.onBeaconCallback = callback
  }

}

class Beacon(_rssi:Int, scanRecord:Array[Byte]){
  val rssi = _rssi
  var major:String = null
  var minor:String = null
  var uuid:String = null
  var error:String = "not init"
  val createAt:Long = System.currentTimeMillis()

  if(scanRecord.length < 30){
    error = "not iBeacn (length < 30)"
  }
  else if(scanRecord(5) != 0x4c.toByte ||
          scanRecord(6) != 0x00.toByte ||
          scanRecord(7) != 0x02.toByte ||
          scanRecord(8) != 0x15.toByte){
    error = "not iBeacon (format error)"
  }
  else{
    uuid  = Array(9.to(12), 13.to(14), 15.to(16), 17.to(18), 19.to(24)).map(
      range =>
        range.map(i => "%02x".format(scanRecord(i))).mkString.toUpperCase
    ).mkString("-")
    major = 25.to(26).map(i => "%02x".format(scanRecord(i)).toUpperCase ).mkString
    minor = 27.to(28).map(i => "%02x".format(scanRecord(i)).toUpperCase ).mkString
    error = null
  }

  override def toString:String = {
    s"UUID=${uuid} Major=${major} Minor=${minor} RSSI=${rssi}"
  }

  def name:String = {
    return s"${uuid}.${major}.${minor}"
  }

}
