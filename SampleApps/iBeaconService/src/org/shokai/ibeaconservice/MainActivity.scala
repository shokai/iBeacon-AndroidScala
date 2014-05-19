package org.shokai.ibeaconservice;

import android.app.{Activity, PendingIntent};
import android.os.Bundle;
import android.content.{Context, Intent};
import android.widget.Button;
import android.view.View;
import android.util.Log;

class MainActivity extends Activity{

  lazy val appName:String = getResources().getString(R.string.app_name)
  lazy val btnStart:Button = findViewById(R.id.btnStart).asInstanceOf[Button]
  lazy val btnStop:Button = findViewById(R.id.btnStop).asInstanceOf[Button]
  lazy val serviceIntent:Intent = new Intent(this, classOf[IBeaconService])

  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    print("app start")

    val self = this

    btnStart.setOnClickListener( new View.OnClickListener(){
      override def onClick(v:View){
        self.startService(serviceIntent)
      }
    })

    btnStop.setOnClickListener( new View.OnClickListener(){
      override def onClick(v:View){
        self.stopService(serviceIntent)
      }
    })
  }

  def print(msg:String){
    Log.v(appName, msg)
  }

}
