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
    print("start service")

    var thread:HandlerThread = new HandlerThread(appName, Process.THREAD_PRIORITY_BACKGROUND)
    thread.start()
    val looper = thread.getLooper()
    handler = new Handler(looper){
      override def handleMessage(msg:Message){

        iBeacon.onDiscover((beacon:Beacon) => {
          print(s"UUID=${beacon.uuid} Major=${beacon.major} Minor=${beacon.minor} RSSI=${beacon.rssi}")
        })

        iBeacon.onDiscover("805D6740-F575-492A-8668-45E553EB9DF2", null, null, (beacon:Beacon) => {
          print(s"discover UUID=${beacon.uuid}")
        })

        iBeacon.onRange("805D6740-F575-492A-8668-45E553EB9DF2", "0001", "0001", Range(-70,-50), (beacon:Beacon) => {
          print(s"range UUID=${beacon.uuid} rssi=${beacon.rssi}")
        })

      }
    }
  }

  override def onStartCommand(intent:Intent, flags:Int, startId:Int):Int = {
    var msg:Message = handler.obtainMessage()
    msg.arg1 = startId
    handler.sendMessage(msg)
    return Service.START_STICKY
  }

  override def onBind(intent:Intent):IBinder = {
    return null
  }

  override def onDestroy(){
    print("stop service")
  }


  def print(msg:String){
    Log.v(appName, msg)
    notifer.popup(msg)
  }

}
