package org.shokai.ibeacon;

import android.app.Activity;
import android.content.Context;
import android.bluetooth.{BluetoothManager, BluetoothAdapter, BluetoothDevice};

class IBeacon(context:Context) extends EventEmitter{

  val VERSION = "0.2.0"

  import scala.collection.mutable.Map;
  import scala.collection.immutable.Range

  val beacons = Map.empty[String, Beacon]
  val bluetoothManager:BluetoothManager =
    context.getSystemService(Context.BLUETOOTH_SERVICE).asInstanceOf[BluetoothManager]
  val bluetoothAdapter:BluetoothAdapter = bluetoothManager.getAdapter()

  bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback(){
    override def onLeScan(device:BluetoothDevice, rssi:Int, scanRecord:Array[Byte]){
      val beacon:Beacon = new Beacon(rssi, scanRecord)
      if(beacon.error != null) return
      emit("beacon", beacon)
      if(!beacons.contains(beacon.name) ||
         beacon.createAt - beacons(beacon.name).createAt > 5000){
        emit("discover", beacon) // new beacon
      }
      beacons(beacon.name) = beacon
    }
  })

  def onBeacon(callback:(Beacon) => Unit){
    on("beacon", (beacon) => {
      callback(beacon.asInstanceOf[Beacon])
    })
  }

  def onDiscover(callback:(Beacon) => Unit){
    on("discover", (beacon) => {
      callback(beacon.asInstanceOf[Beacon])
    })
  }

  def onDiscover(uuid:String, major:String, minor:String, callback:(Beacon) => Unit){
    on("discover", (_beacon) => {
      val beacon = _beacon.asInstanceOf[Beacon]
      if((uuid == null || beacon.uuid.replace("-","").equals(uuid.replace("-","").toUpperCase())) &&
        (major == null || beacon.major.toUpperCase().equals(major.toUpperCase())) &&
        (minor == null || beacon.minor.toUpperCase().equals(minor.toUpperCase()))){
        callback(beacon)
      }
    })
  }

  def onRegion(uuid:String, major:String, minor:String, range:Range, callback:(Beacon) => Unit){
    val accuracy = 30
    val rssis = scala.collection.mutable.Queue.empty[Int]
    var last:Long = 0
    on("beacon", (_beacon) => {
      val beacon = _beacon.asInstanceOf[Beacon]
      if((uuid == null || beacon.uuid.replace("-","").equals(uuid.replace("-","").toUpperCase())) &&
         (major == null || beacon.major.toUpperCase().equals(major.toUpperCase())) &&
         (minor == null || beacon.minor.toUpperCase().equals(minor.toUpperCase())) ){
        rssis.enqueue(beacon.rssi)
        if(rssis.size > accuracy){
          rssis.dequeue()
          beacon.rssi = rssis.reduce((a,b) => a+b)/rssis.size // average
          if(range.min < beacon.rssi && beacon.rssi < range.max){
            if(System.currentTimeMillis() - last > 5000){
              callback(beacon)
            }
            last = System.currentTimeMillis()
          }
        }
      }
    })
  }

  def onFar(uuid:String, major:String, minor:String, callback:(Beacon) => Unit){
    onRegion(uuid, major, minor, Range(-90,-70), callback)
  }

  def onNear(uuid:String, major:String, minor:String, callback:(Beacon) => Unit){
    onRegion(uuid, major, minor, Range(-70,-20), callback)
  }

  def onImmediate(uuid:String, major:String, minor:String, callback:(Beacon) => Unit){
    onRegion(uuid, major, minor, Range(-20,0), callback)
  }

}

class Beacon(_rssi:Int, scanRecord:Array[Byte]){
  var rssi = _rssi
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


trait EventEmitter{

  val List = scala.collection.mutable.LinkedList
  var eid = 0

  class Event(_id:Int, _name:String, _callback:(Any) => Unit, _once:Boolean){
    var name = _name
    var callback = _callback
    val id = _id
    var once = _once
  }

  var __events = List.empty[Event]

  def on(name:String, callback:(Any) => Unit):Int = {
    eid += 1
    __events = __events :+ new Event(eid, name, callback, false)
    return eid
  }

  def once(name:String, callback:(Any) => Unit):Int = {
    eid += 1
    __events = __events :+ new Event(eid, name, callback, true)
    return eid
  }

  def emit(name:String, args:Any){
    var once_ids = List.empty[Int]
    __events.foreach( (e) => {
      if(e.name.equals(name)){
        e.callback(args)
        if(e.once) once_ids = once_ids :+ e.id
      }
    })
    __events = __events.filter((e) => {
      !once_ids.contains(e.id)
    })
  }

  def removeListener(id:Int){
    __events = __events.filter((e) => {
      e.id != id
    })
  }

}
