package org.shokai.ibeaconservice;

import org.shokai.ibeacon.{IBeacon, Beacon};
import android.app.{Service, IntentService};
import android.os.{Bundle, IBinder, Looper, Handler, HandlerThread, Process, Message};
import android.content.Intent;
import android.util.Log;

class IBeaconService extends IntentService("IBeaconService"){

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val iBeacon:IBeacon = new IBeacon(this)
  lazy val notifer:Notifer = new Notifer(appName, this)

  override def onHandleIntent(intent:Intent){
    trace("service start")

    iBeacon.onDetect((beacon:Beacon) =>
      trace(s"UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
    )

  }

  override def onStartCommand(intent:Intent, flags:Int, startId:Int):Int = {
    trace("startCommand")
    return super.onStartCommand(intent, flags, startId)
  }

  def trace(msg:String){
    notifer.popup(msg)
    Log.v(appName, msg)
  }

}
