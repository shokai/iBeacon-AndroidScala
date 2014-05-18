package org.shokai.ibeaconservice;

import org.shokai.ibeacon.{IBeacon, Beacon};
import android.app.{Service, IntentService};
import android.os.{Bundle, IBinder, Looper, Handler, HandlerThread, Process, Message};
import android.content.Intent;
import android.util.Log;

class IBeaconService extends IntentService("IBeaconService"){

  lazy val appName:String = getResources().getString(R.string.app_name)

  override def onHandleIntent(intent:Intent){
    trace("service start")
  }

  override def onStartCommand(intent:Intent, flags:Int, startId:Int):Int = {
    trace("startCommand")
    return super.onStartCommand(intent, flags, startId)
  }

  def trace(msg:String){
    // new Notifer(appName, this).popup(msg)
    Log.v(appName, msg)
  }

}
