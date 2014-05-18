package org.shokai.ibeaconservice;

import android.app.{Activity, Notification, NotificationManager, PendingIntent};
import android.os.Bundle;
import android.content.{Context, Intent};
import android.widget.Button;
import android.view.View;
import android.util.Log;

class MainActivity extends Activity{

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val btnStart:Button = findViewById(R.id.btnStart).asInstanceOf[Button]

  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Log.v(appName, "app start")

    new Notifer(appName, this).popup("app start")

    val self = this

    btnStart.setOnClickListener( new View.OnClickListener(){
      override def onClick(v:View){
        Log.v(appName, "click")
        val intent:Intent = new Intent(self, classOf[IBeaconService])
        self.startService(intent)
      }
    })

    Log.v(appName, "start intent")
  }

}
