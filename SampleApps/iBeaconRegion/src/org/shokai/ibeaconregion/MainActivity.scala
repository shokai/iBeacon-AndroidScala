package org.shokai.ibeaconregion;

import org.shokai.ibeacon.{IBeacon, Beacon};
import android.app.Activity;
import android.content.Context;
import android.os.{Bundle, Handler};
import android.util.Log;
import android.widget.TextView;
import android.view.View;

class MainActivity extends Activity{

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val handler:Handler = new Handler()
  lazy val textViewMsg:TextView = findViewById(R.id.textViewMsg).asInstanceOf[TextView]
  lazy val iBeacon:IBeacon = new IBeacon(this)

  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    print("app start")

    iBeacon.onImmediate(null, null, null, (beacon:Beacon) => {
      print(s"Immediate: UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
    })

    iBeacon.onNear(null, null, null, (beacon:Beacon) => {
      print(s"Near: UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
    })

    iBeacon.onFar(null, null, null, (beacon:Beacon) => {
      print(s"Far: UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
    })

  }

  def print(msg:String){
    Log.v(appName, msg)
    handler.post(new Runnable(){
      override def run(){
        textViewMsg.setText(msg)
      }
    })
  }

}
