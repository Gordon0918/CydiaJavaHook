package com.example.cydiajavahook;

import android.util.Log;

import com.saurik.substrate.MS;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by xlzh on 2017/4/19.
 */

public class Main {
    static void initialize(){
        Log.d("JAVAHOOK", "initialize");
        MS.hookClassLoad("java.net.InetSocketAddress", new MS.ClassLoadHook() {
            public void classLoaded(Class<?> _class) {
                Log.d("JAVAHOOK", "classLoaded");
                Constructor method = null;
                try {
                    method = _class.getConstructor(String.class, Integer.TYPE);
                } catch (NoSuchMethodException e) {
                    Log.d("JAVAHOOK", "NULL");
                    e.printStackTrace();
                }
                if (method != null) {
                    MS.hookMethod(_class, method, new MS.MethodAlteration() {
                        public Object invoked(Object _this, Object... args) throws Throwable {
                            String host = (String) args[0];
                            int port = (Integer) args[1];
                            Log.d("JAVAHOOK", "host: " + host + " port: " + port);
                            if (port == 6667)
                                port = 7001;
                            return invoke(_this, host, port);
                        }
                    });
                }
            }
        });
    }
}

