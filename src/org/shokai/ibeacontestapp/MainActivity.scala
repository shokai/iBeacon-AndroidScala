package org.shokai.ibeacontestapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.{TextView, EditText, Button};
import android.view.View;
import android.view.View.OnClickListener;

class MainActivity extends Activity{

  lazy val textViewMsg:TextView = findViewById(R.id.textViewMsg).asInstanceOf[TextView]

  override def onCreate(savedInstanceState:Bundle){
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    trace("app start")
    textViewMsg.setText("はい")
  }

  def trace(msg:String){
    Log.v("iBeaconTestApp", msg)
  }
}
