package org.shokai.ibeacontestapp;

import android.app.Activity;
import android.content.Context;
import android.bluetooth.{BluetoothManager, BluetoothAdapter, BluetoothDevice};


class IBeacon(context:Activity){

  val bluetoothManager:BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE).asInstanceOf[BluetoothManager]
  val bluetoothAdapter:BluetoothAdapter = bluetoothManager.getAdapter()

  var onDetectCallback:((Beacon) => Unit) = null

  bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback(){
    override def onLeScan(device:BluetoothDevice, rssi:Int, scanRecord:Array[Byte]){
      val beacon:Beacon = new Beacon(rssi, scanRecord)
      if (onDetectCallback != null && beacon.error == null){
        onDetectCallback(beacon)
      }
    }
  })

  def onDetect(callback:(Beacon) => Unit){
    this.onDetectCallback = callback
  }

}
