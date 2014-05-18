package org.shokai.ibeaconservice;

import android.app.{Notification, NotificationManager, PendingIntent};
import android.content.{Context, Intent};

class Notifer(appName:String, context:Context){

  def popup(msg:String):Notifer = {
    val manager:NotificationManager =
      context.getSystemService(Context.NOTIFICATION_SERVICE).asInstanceOf[NotificationManager]
    val notif:Notification =
      new Notification(android.R.drawable.btn_default, msg, System.currentTimeMillis())
    val intent:Intent = new Intent(context, context.getClass())
    val contentIntent:PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

    notif.setLatestEventInfo(context.getApplicationContext(), appName, msg, contentIntent)
    manager.notify(R.string.app_name, notif)

    return this
  }
  
}
