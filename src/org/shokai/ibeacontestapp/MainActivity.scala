package org.shokai.ibeacontestapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.{TextView, EditText, Button};
import android.view.View;
import android.bluetooth.{BluetoothManager, BluetoothAdapter, BluetoothDevice};


class MainActivity extends Activity{

  lazy val textViewMsg:TextView = findViewById(R.id.textViewMsg).asInstanceOf[TextView]
  lazy val bluetoothManager:BluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE).asInstanceOf[BluetoothManager]
  lazy val bluetoothAdapter:BluetoothAdapter = bluetoothManager.getAdapter()


  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    trace("app start")
    bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback(){
      override def onLeScan(device:BluetoothDevice, rssi:Int, scanRecord:Array[Byte]){
        val beacon:Beacon = new Beacon(scanRecord)
        if (beacon.error != null){
          trace(s"rssi: $rssi, beacon: error=${beacon.error}")
        }
        else{
          trace(s"rssi: $rssi, beacon: uuid=${beacon.uuid} major=${beacon.major} minor=${beacon.minor}")
        }
      }
    })
  }

  def trace(msg:String){
    Log.v("iBeaconTestApp", msg)
  }
}
