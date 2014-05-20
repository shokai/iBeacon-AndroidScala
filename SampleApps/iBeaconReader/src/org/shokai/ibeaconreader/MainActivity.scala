package org.shokai.ibeaconreader;

import org.shokai.ibeacon.{IBeacon, Beacon};
import android.app.Activity;
import android.content.Context;
import android.os.{Bundle, Handler};
import android.util.Log;
import android.widget.{TextView, EditText, Button};
import android.view.View;

class MainActivity extends Activity{

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val handler:Handler = new Handler()
  lazy val textViewMsg:TextView = findViewById(R.id.textViewMsg).asInstanceOf[TextView]
  lazy val iBeacon:IBeacon = new IBeacon(this)

  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    print("app start")

    iBeacon.onBeacon((beacon:Beacon) =>
      print(s"UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
    )

  }

  var count:Int = 0
  def print(msg:String){
    Log.v(appName, msg)
    handler.post(new Runnable(){
      override def run(){
        textViewMsg.setText(s"${msg} ${"."*(count%3+1)}")
      }
    })
    count += 1
  }
}
