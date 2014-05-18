package org.shokai.ibeaconservice;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;

class MainActivity extends Activity{

  lazy val appName:String = getResources().getString(R.string.app_name)

  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Log.v(appName, "app start")

    val intent:Intent = new Intent(this, org.shokai.ibeaconservice.IBeaconService.getClass())
    startService(intent)
  }

}
