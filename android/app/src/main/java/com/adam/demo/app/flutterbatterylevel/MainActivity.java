package com.adam.demo.app.flutterbatterylevel;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {

  private static final String CHANNEL = "samples.flutter.io/battery";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i("Demo", "onCreate enter");
    GeneratedPluginRegistrant.registerWith(this);

    new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
            new MethodChannel.MethodCallHandler() {
                @Override
                public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                    Log.i("Demo", "onMethodCall enter");
                    if (methodCall.method.equals("getBatteryLevel")) {
                        int level = getBatteryLevel();
                        if (level != -1) {
                            result.success(level);
                        } else {
                            result.error("UNAVALIBEL", "Battery level is not avaliable...", null);
                        }
                    } else {
                        result.notImplemented();
                    }
                }
            }
    );
  }

  private int getBatteryLevel() {
      Log.i("Demo", "getBatteryLevel enter");
      int ret = -1;

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          BatteryManager Batteryservice = (BatteryManager) this.getSystemService(Context.BATTERY_SERVICE);
          // get battery level from battery service
          ret = Batteryservice.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
      } else {
          Intent intent = new ContextWrapper(getApplicationContext()).
                  registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
          ret = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)*100)/
                  intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
      }

      return ret;
  }
}
