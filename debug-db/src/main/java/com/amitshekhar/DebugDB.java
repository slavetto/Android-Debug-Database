/*
 *
 *  *    Copyright (C) 2016 Amit Shekhar
 *  *    Copyright (C) 2011 Android Open Source Project
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package com.amitshekhar;

import android.content.Context;
import android.util.Log;

import com.amitshekhar.server.ClientServer;
import com.amitshekhar.utils.NetworkUtils;

import java.io.File;
import java.util.HashMap;

/**
 * Created by amitshekhar on 15/11/16.
 */

public class DebugDB {

    private static final String TAG = DebugDB.class.getSimpleName();
    private static final int DEFAULT_PORT = 8080;
    private static ClientServer clientServer;
    private static String addressLog = "not available";

    private DebugDB() {
        // This class in not publicly instantiable
    }

    public static void initialize(Context context) {
        //Calculating the address
        int portNumber = retrievePortNumber(context);

        clientServer = new ClientServer(context, portNumber);
        clientServer.start();
        addressLog = NetworkUtils.getAddressLog(context, portNumber);


        //Checking whenever we have to output the address log to debug
        boolean dumpDebug = Boolean.valueOf(context.getString(R.string.DUMP_DB_DEBUG_ADDRESS));
        if (dumpDebug) {
            Log.d(TAG, addressLog);
        }
    }

    private static int retrievePortNumber(Context context) {
        int portNumber;
        try {
            portNumber = Integer.valueOf(context.getString(R.string.PORT_NUMBER));
        } catch (NumberFormatException ex) {
            Log.e(TAG, "PORT_NUMBER should be integer", ex);
            portNumber = DEFAULT_PORT;
            Log.i(TAG, "Using Default port : " + DEFAULT_PORT);
        }
        return portNumber;
    }

    public static String getAddressLog() {
        return addressLog;
    }

    public static void shutDown() {
        if (clientServer != null) {
            clientServer.stop();
            clientServer = null;
        }
    }

    public static void setCustomDatabaseFiles(HashMap<String, File> customDatabaseFiles){
        if(clientServer!=null){
            clientServer.setCustomDatabaseFiles(customDatabaseFiles);
        }
    }
    
    public static boolean isServerRunning() {
        return clientServer != null && clientServer.isRunning();
    }

}
