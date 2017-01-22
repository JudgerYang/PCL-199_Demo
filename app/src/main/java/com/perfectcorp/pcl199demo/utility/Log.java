package com.perfectcorp.pcl199demo.utility;

import android.text.TextUtils;

public class Log {
    public static void bear(Object... args) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[3];
        String className = ste.getClassName();
        String tag = "Bear-" + className.substring(className.lastIndexOf('.') + 1);

        String prefix = "(" + ste.getFileName() + ":" + ste.getLineNumber() + ")[" + ste.getMethodName() + "] ";

        android.util.Log.d(tag, prefix + TextUtils.join("; ", args));
    }
}
