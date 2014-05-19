package org.shokai.ibeaconservice;

import org.shokai.ibeacon.{IBeacon, Beacon};
import android.app.{Service, IntentService};
import android.os.{Bundle, IBinder, Looper, Handler, HandlerThread, Process, Message};
import android.content.Intent;
import android.util.Log;

class IBeaconService extends Service{

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val iBeacon:IBeacon = new IBeacon(this)
  lazy val notifer:Notifer = new Notifer(appName, this)

  var handler:Handler = null

  override def onCreate{
    trace("onCreate")

    var thread:HandlerThread = new HandlerThread(appName, Process.THREAD_PRIORITY_BACKGROUND)
    thread.start()
    val looper = thread.getLooper()
    handler = new Handler(looper){
      override def handleMessage(msg:Message){
        Log.v("iBeaconService", "handleMessage")

        iBeacon.onDetect((beacon:Beacon) =>
          trace(s"UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
        )

      }
    }
  }

  override def onStartCommand(intent:Intent, flags:Int, startId:Int):Int = {
    trace("startCommand")
    var msg:Message = handler.obtainMessage()
    msg.arg1 = startId
    handler.sendMessage(msg)
    return Service.START_STICKY
  }

  override def onBind(intent:Intent):IBinder = {
    return null
  }

  def trace(msg:String){
    notifer.popup(msg)
    Log.v(appName, msg)
  }

}
