package org.shokai.ibeaconservice;

import android.app.{Service};
import android.os.{Bundle, IBinder};
import android.content.Intent;
import android.util.Log;

class IBeaconService extends Service{

  lazy val appName:String = getResources().getString(R.string.app_name)

  override def onCreate(){
    trace("start service")
  }

  override def onStartCommand(intent:Intent, flags:Int, startId:Int):Int = {
    return Service.START_STICKY
  }

  override def onBind(intent:Intent):IBinder = {
    return null
  }

  override def onDestroy{
  }

  def trace(msg:String){
    Log.v(appName, msg)
  }

}
