package org.shokai.ibeaconservice;

import android.content.{Context, BroadcastReceiver, Intent}
import android.util.Log;

class BootReceiver extends BroadcastReceiver {

  override def onReceive(context:Context, intent:Intent){
    val intent:Intent = new Intent(context, classOf[IBeaconService])
    context.startService(intent)
  }

}
