package pro.sketchware;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import java.util.Locale;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

import com.besome.sketch.tools.CollectErrorActivity;

import pro.sketchware.utility.AnalyticsHelper;
import pro.sketchware.utility.theme.ThemeManager;

public class SketchApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        Configuration config = new Configuration(base.getResources().getConfiguration());
        config.setLocale(new Locale("fa"));
        config.setLayoutDirection(new Locale("fa"));
        super.attachBaseContext(base.createConfigurationContext(config));
    }

    private static Context mApplicationContext;

    public static Context getContext() {
        return mApplicationContext;
    }

    @Override
    public void onCreate() {
        mApplicationContext = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
                Intent intent = new Intent(getApplicationContext(), CollectErrorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("error", Log.getStackTraceString(throwable));
                startActivity(intent);
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        });
        super.onCreate();
        ThemeManager.applyTheme(this, ThemeManager.getCurrentTheme(this));
        AnalyticsHelper.logDeviceAbi(this);
    }
}
