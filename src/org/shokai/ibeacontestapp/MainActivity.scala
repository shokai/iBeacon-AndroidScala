package org.shokai.ibeacontestapp;

import android.app.Activity;
import android.content.Context;
import android.os.{Bundle, Handler};
import android.util.Log;
import android.widget.{TextView, EditText, Button};
import android.view.View;


class MainActivity extends Activity{

  lazy val handler:Handler = new Handler()
  lazy val textViewMsg:TextView = findViewById(R.id.textViewMsg).asInstanceOf[TextView]


  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    trace("app start")

    val iBeacon:IBeacon = new IBeacon(this)

    iBeacon.onDetect((beacon) =>
      trace(s"rssi=${beacon.rssi} uuid=${beacon.uuid} major=${beacon.major} minor=${beacon.minor}")
    )

  }

  def trace(msg:String){
    Log.v("iBeaconTestApp", msg)
    handler.post(new Runnable(){
      override def run(){
        textViewMsg.setText(msg)
      }
    })
  }
}
