package org.shokai.ibeacontestapp;

import scala.collection.immutable.StringOps;

class Beacon(scanRecord:Array[Byte]){
  var major:String = null
  var minor:String = null
  var uuid:String = null
  var error:String = "not init"

  if(scanRecord.length < 30){
    error = "length < 30"
  }
  else if(scanRecord(5) != 0x4c.toByte ||
    scanRecord(6) != 0x00.toByte ||
    scanRecord(7) != 0x02.toByte ||
    scanRecord(8) != 0x15.toByte){
    error = "bad format"
  }
  else{
    uuid  =  9.to(24).map(i => "%02x".format(scanRecord(i)) ).mkString("-")
    major = 25.to(26).map(i => "%02x".format(scanRecord(i)) ).mkString("-")
    minor = 27.to(28).map(i => "%02x".format(scanRecord(i)) ).mkString("-")
    error = null
  }
}
