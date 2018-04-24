// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package org.apache.cordova;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.cordova.api.CordovaInterface;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.LOG;
import org.apache.cordova.api.PluginManager;
import org.json.JSONException;
import org.json.JSONObject;

// Referenced classes of package org.apache.cordova:
//            CordovaWebView, CordovaWebViewClient, CordovaChromeClient, IceCreamCordovaWebViewClient, 
//            Config, LinearLayoutSoftKeyboardDetect, NativeToJsMessageQueue, AuthenticationToken

public class CordovaActivity extends Activity
    implements CordovaInterface
{

    private static int ACTIVITY_EXITING = 2;
    private static int ACTIVITY_RUNNING = 1;
    private static int ACTIVITY_STARTING = 0;
    public static String TAG = "CordovaActivity";
    private Object LOG_TAG;
    protected CordovaPlugin activityResultCallback;
    protected boolean activityResultKeepRunning;
    private int activityState;
    protected CordovaWebView appView;
    private int backgroundColor;
    protected boolean cancelLoadUrl;
    private String initCallbackClass;
    protected boolean keepRunning;
    private Intent lastIntent;
    private int lastRequestCode;
    private Object lastResponseCode;
    protected int loadUrlTimeoutValue;
    private Object responseCode;
    protected LinearLayout root;
    protected ProgressDialog spinnerDialog;
    protected Dialog splashDialog;
    protected int splashscreen;
    protected int splashscreenTime;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    protected CordovaWebViewClient webViewClient;

    public CordovaActivity()
    {
        cancelLoadUrl = false;
        spinnerDialog = null;
        activityState = 0;
        activityResultCallback = null;
        backgroundColor = 0xff000000;
        splashscreen = 0;
        splashscreenTime = 3000;
        loadUrlTimeoutValue = 20000;
        keepRunning = true;
    }

    public void addService(String s, String s1)
    {
        if (appView != null && appView.pluginManager != null)
        {
            appView.pluginManager.addService(s, s1);
        }
    }

    public boolean backHistory()
    {
        if (appView != null)
        {
            return appView.backHistory();
        } else
        {
            return false;
        }
    }

    public void cancelLoadUrl()
    {
        cancelLoadUrl = true;
    }

    public void clearAuthenticationTokens()
    {
        if (appView != null && appView.viewClient != null)
        {
            appView.viewClient.clearAuthenticationTokens();
        }
    }

    public void clearCache()
    {
        if (appView == null)
        {
            init();
        }
        appView.clearCache(true);
    }

    public void clearHistory()
    {
        appView.clearHistory();
    }

    public void displayError(final String title, final String message, final String button, final boolean exit)
    {
        runOnUiThread(new Runnable() {

            final CordovaActivity this$0;
            final String val$button;
            final boolean val$exit;
            final CordovaActivity val$me;
            final String val$message;
            final String val$title;

            public void run()
            {
                try
                {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(me);
                    builder.setMessage(message);
                    builder.setTitle(title);
                    builder.setCancelable(false);
                    builder.setPositiveButton(button, new android.content.DialogInterface.OnClickListener() {

                        final _cls4 this$1;

                        public void onClick(DialogInterface dialoginterface, int i)
                        {
                            dialoginterface.dismiss();
                            if (exit)
                            {
                                me.endActivity();
                            }
                        }

            
            {
                this$1 = _cls4.this;
                super();
            }
                    });
                    builder.create();
                    builder.show();
                    return;
                }
                catch (Exception exception)
                {
                    finish();
                }
            }

            
            {
                this$0 = CordovaActivity.this;
                me = cordovaactivity1;
                message = s;
                title = s1;
                button = s2;
                exit = flag;
                super();
            }
        });
    }

    public void endActivity()
    {
        activityState = ACTIVITY_EXITING;
        super.finish();
    }

    public Activity getActivity()
    {
        return this;
    }

    public AuthenticationToken getAuthenticationToken(String s, String s1)
    {
        if (appView != null && appView.viewClient != null)
        {
            return appView.viewClient.getAuthenticationToken(s, s1);
        } else
        {
            return null;
        }
    }

    public boolean getBooleanProperty(String s, boolean flag)
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            Boolean boolean1;
            try
            {
                boolean1 = (Boolean)bundle.get(s);
            }
            catch (ClassCastException classcastexception)
            {
                if ("true".equals(bundle.get(s).toString()))
                {
                    boolean1 = Boolean.valueOf(true);
                } else
                {
                    boolean1 = Boolean.valueOf(false);
                }
            }
            if (boolean1 != null)
            {
                return boolean1.booleanValue();
            }
        }
        return flag;
    }

    public Context getContext()
    {
        LOG.d(TAG, "This will be deprecated December 2012");
        return this;
    }

    public double getDoubleProperty(String s, double d)
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            Double double1;
            try
            {
                double1 = (Double)bundle.get(s);
            }
            catch (ClassCastException classcastexception)
            {
                double1 = Double.valueOf(Double.parseDouble(bundle.get(s).toString()));
            }
            if (double1 != null)
            {
                return double1.doubleValue();
            }
        }
        return d;
    }

    public int getIntegerProperty(String s, int i)
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            Integer integer;
            try
            {
                integer = (Integer)bundle.get(s);
            }
            catch (ClassCastException classcastexception)
            {
                integer = Integer.valueOf(Integer.parseInt(bundle.get(s).toString()));
            }
            if (integer != null)
            {
                return integer.intValue();
            }
        }
        return i;
    }

    public String getStringProperty(String s, String s1)
    {
        Bundle bundle = getIntent().getExtras();
        String s2;
        if (bundle != null)
        {
            if ((s2 = bundle.getString(s)) != null)
            {
                return s2;
            }
        }
        return s1;
    }

    public ExecutorService getThreadPool()
    {
        return threadPool;
    }

    public void init()
    {
        CordovaWebView cordovawebview = new CordovaWebView(this);
        Object obj;
        if (android.os.Build.VERSION.SDK_INT < 11)
        {
            obj = new CordovaWebViewClient(this, cordovawebview);
        } else
        {
            obj = new IceCreamCordovaWebViewClient(this, cordovawebview);
        }
        init(cordovawebview, ((CordovaWebViewClient) (obj)), new CordovaChromeClient(this, cordovawebview));
    }

    public void init(CordovaWebView cordovawebview, CordovaWebViewClient cordovawebviewclient, CordovaChromeClient cordovachromeclient)
    {
        LOG.d(TAG, "CordovaActivity.init()");
        appView = cordovawebview;
        appView.setId(100);
        appView.setWebViewClient(cordovawebviewclient);
        appView.setWebChromeClient(cordovachromeclient);
        cordovawebviewclient.setWebView(appView);
        cordovachromeclient.setWebView(appView);
        appView.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1, 1.0F));
        if (getBooleanProperty("disallowOverscroll", false) && android.os.Build.VERSION.SDK_INT >= 9)
        {
            appView.setOverScrollMode(2);
        }
        appView.setVisibility(4);
        root.addView(appView);
        setContentView(root);
        cancelLoadUrl = false;
    }

    public boolean isUrlWhiteListed(String s)
    {
        return Config.isUrlWhiteListed(s);
    }

    void loadSpinner()
    {
        String s;
        if (appView == null || !appView.canGoBack())
        {
            s = getStringProperty("loadingDialog", null);
        } else
        {
            s = getStringProperty("loadingPageDialog", null);
        }
        if (s != null)
        {
            String s1 = "";
            String s2 = "Loading Application...";
            if (s.length() > 0)
            {
                int i = s.indexOf(',');
                if (i > 0)
                {
                    s1 = s.substring(0, i);
                    s2 = s.substring(i + 1);
                } else
                {
                    s1 = "";
                    s2 = s;
                }
            }
            spinnerStart(s1, s2);
        }
    }

    public void loadUrl(String s)
    {
        if (appView == null)
        {
            init();
        }
        backgroundColor = getIntegerProperty("backgroundColor", 0xff000000);
        root.setBackgroundColor(backgroundColor);
        keepRunning = getBooleanProperty("keepRunning", true);
        loadSpinner();
        appView.loadUrl(s);
    }

    public void loadUrl(String s, int i)
    {
        if (appView == null)
        {
            init();
        }
        splashscreenTime = i;
        splashscreen = getIntegerProperty("splashscreen", 0);
        showSplashScreen(splashscreenTime);
        appView.loadUrl(s, i);
    }

    protected void onActivityResult(int i, int j, Intent intent)
    {
        ValueCallback valuecallback;
        LOG.d(TAG, "Incoming Result");
        super.onActivityResult(i, j, intent);
        Log.d(TAG, (new StringBuilder()).append("Request code = ").append(i).toString());
        valuecallback = appView.getWebChromeClient().getValueCallback();
        if (i != 5173) goto _L2; else goto _L1
_L1:
        Log.d(TAG, "did we get here?");
        if (valuecallback != null) goto _L4; else goto _L3
_L3:
        return;
_L4:
        CordovaPlugin cordovaplugin;
        android.net.Uri uri;
        if (intent == null || j != -1)
        {
            uri = null;
        } else
        {
            uri = intent.getData();
        }
        Log.d(TAG, (new StringBuilder()).append("result = ").append(uri).toString());
        valuecallback.onReceiveValue(uri);
_L2:
        cordovaplugin = activityResultCallback;
        if (cordovaplugin == null && initCallbackClass != null)
        {
            activityResultCallback = appView.pluginManager.getPlugin(initCallbackClass);
            cordovaplugin = activityResultCallback;
        }
        if (cordovaplugin != null)
        {
            LOG.d(TAG, "We have a callback to send this result to");
            cordovaplugin.onActivityResult(i, j, intent);
            return;
        }
        if (true) goto _L3; else goto _L5
_L5:
    }

    public void onConfigurationChanged(Configuration configuration)
    {
        super.onConfigurationChanged(configuration);
    }

    public void onCreate(Bundle bundle)
    {
        Config.init(this);
        LOG.d(TAG, "CordovaActivity.onCreate()");
        super.onCreate(bundle);
        if (bundle != null)
        {
            initCallbackClass = bundle.getString("callbackClass");
        }
        if (!getBooleanProperty("showTitle", false))
        {
            getWindow().requestFeature(1);
        }
        Display display;
        if (getBooleanProperty("setFullscreen", false))
        {
            getWindow().setFlags(1024, 1024);
        } else
        {
            getWindow().setFlags(2048, 2048);
        }
        display = getWindowManager().getDefaultDisplay();
        root = new LinearLayoutSoftKeyboardDetect(this, display.getWidth(), display.getHeight());
        root.setOrientation(1);
        root.setBackgroundColor(backgroundColor);
        root.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1, 0.0F));
        setVolumeControlStream(3);
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        postMessage("onCreateOptionsMenu", menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onDestroy()
    {
        LOG.d(TAG, "CordovaActivity.onDestroy()");
        super.onDestroy();
        removeSplashScreen();
        if (appView != null)
        {
            appView.handleDestroy();
            return;
        } else
        {
            activityState = ACTIVITY_EXITING;
            return;
        }
    }

    public boolean onKeyDown(int i, KeyEvent keyevent)
    {
        if (appView.getFocusedChild() != null && (i == 4 || i == 82))
        {
            return appView.onKeyDown(i, keyevent);
        } else
        {
            return super.onKeyDown(i, keyevent);
        }
    }

    public boolean onKeyUp(int i, KeyEvent keyevent)
    {
        android.view.View view = appView.getFocusedChild();
        if ((appView.isCustomViewShowing() || view != null) && (i == 4 || i == 82))
        {
            return appView.onKeyUp(i, keyevent);
        } else
        {
            return super.onKeyUp(i, keyevent);
        }
    }

    public Object onMessage(String s, Object obj)
    {
        LOG.d(TAG, (new StringBuilder()).append("onMessage(").append(s).append(",").append(obj).append(")").toString());
        if (!"splashscreen".equals(s)) goto _L2; else goto _L1
_L1:
        if (!"hide".equals(obj.toString())) goto _L4; else goto _L3
_L3:
        removeSplashScreen();
_L6:
        return null;
_L4:
        if (splashDialog == null || !splashDialog.isShowing())
        {
            splashscreen = getIntegerProperty("splashscreen", 0);
            showSplashScreen(splashscreenTime);
        }
        continue; /* Loop/switch isn't completed */
_L2:
        if ("spinner".equals(s))
        {
            if ("stop".equals(obj.toString()))
            {
                spinnerStop();
                appView.setVisibility(0);
            }
        } else
        if ("onReceivedError".equals(s))
        {
            JSONObject jsonobject = (JSONObject)obj;
            try
            {
                onReceivedError(jsonobject.getInt("errorCode"), jsonobject.getString("description"), jsonobject.getString("url"));
            }
            catch (JSONException jsonexception)
            {
                jsonexception.printStackTrace();
            }
        } else
        if ("exit".equals(s))
        {
            endActivity();
        }
        if (true) goto _L6; else goto _L5
_L5:
    }

    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        if (appView != null)
        {
            appView.onNewIntent(intent);
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuitem)
    {
        postMessage("onOptionsItemSelected", menuitem);
        return true;
    }

    protected void onPause()
    {
        super.onPause();
        LOG.d(TAG, "Paused the application!");
        while (activityState == ACTIVITY_EXITING || appView == null) 
        {
            return;
        }
        appView.handlePause(keepRunning);
        removeSplashScreen();
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        postMessage("onPrepareOptionsMenu", menu);
        return true;
    }

    public void onReceivedError(int i, final String description, final String failingUrl)
    {
        final String errorUrl = getStringProperty("errorUrl", null);
        if (errorUrl != null && (errorUrl.startsWith("file://") || Config.isUrlWhiteListed(errorUrl)) && !failingUrl.equals(errorUrl))
        {
            runOnUiThread(new Runnable() {

                final CordovaActivity this$0;
                final String val$errorUrl;
                final CordovaActivity val$me;

                public void run()
                {
                    me.spinnerStop();
                    me.appView.showWebPage(errorUrl, false, true, null);
                }

            
            {
                this$0 = CordovaActivity.this;
                me = cordovaactivity1;
                errorUrl = s;
                super();
            }
            });
            return;
        }
        final boolean exit;
        if (i != -2)
        {
            exit = true;
        } else
        {
            exit = false;
        }
        runOnUiThread(new Runnable() {

            final CordovaActivity this$0;
            final String val$description;
            final boolean val$exit;
            final String val$failingUrl;
            final CordovaActivity val$me;

            public void run()
            {
                if (exit)
                {
                    me.appView.setVisibility(8);
                    me.displayError("Application Error", (new StringBuilder()).append(description).append(" (").append(failingUrl).append(")").toString(), "OK", exit);
                }
            }

            
            {
                this$0 = CordovaActivity.this;
                exit = flag;
                me = cordovaactivity1;
                description = s;
                failingUrl = s1;
                super();
            }
        });
    }

    protected void onResume()
    {
        super.onResume();
        Config.init(this);
        LOG.d(TAG, "Resuming the App");
        String s = getStringProperty("errorUrl", null);
        LOG.d(TAG, (new StringBuilder()).append("CB-3064: The errorUrl is ").append(s).toString());
        if (activityState == ACTIVITY_STARTING)
        {
            activityState = ACTIVITY_RUNNING;
        } else
        if (appView != null)
        {
            appView.handleResume(keepRunning, activityResultKeepRunning);
            if ((!keepRunning || activityResultKeepRunning) && activityResultKeepRunning)
            {
                keepRunning = activityResultKeepRunning;
                activityResultKeepRunning = false;
                return;
            }
        }
    }

    protected void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        if (activityResultCallback != null)
        {
            bundle.putString("callbackClass", activityResultCallback.getClass().getName());
        }
    }

    public void postMessage(String s, Object obj)
    {
        if (appView != null)
        {
            appView.postMessage(s, obj);
        }
    }

    public AuthenticationToken removeAuthenticationToken(String s, String s1)
    {
        if (appView != null && appView.viewClient != null)
        {
            return appView.viewClient.removeAuthenticationToken(s, s1);
        } else
        {
            return null;
        }
    }

    public void removeSplashScreen()
    {
        if (splashDialog != null && splashDialog.isShowing())
        {
            splashDialog.dismiss();
            splashDialog = null;
        }
    }

    public void sendJavascript(String s)
    {
        if (appView != null)
        {
            appView.jsMessageQueue.addJavaScript(s);
        }
    }

    public void setActivityResultCallback(CordovaPlugin cordovaplugin)
    {
        activityResultCallback = cordovaplugin;
    }

    public void setAuthenticationToken(AuthenticationToken authenticationtoken, String s, String s1)
    {
        if (appView != null && appView.viewClient != null)
        {
            appView.viewClient.setAuthenticationToken(authenticationtoken, s, s1);
        }
    }

    public void setBooleanProperty(String s, boolean flag)
    {
        Log.d(TAG, "Setting boolean properties in CordovaActivity will be deprecated in 3.0 on July 2013, please use config.xml");
        getIntent().putExtra(s, flag);
    }

    public void setDoubleProperty(String s, double d)
    {
        Log.d(TAG, "Setting double properties in CordovaActivity will be deprecated in 3.0 on July 2013, please use config.xml");
        getIntent().putExtra(s, d);
    }

    public void setIntegerProperty(String s, int i)
    {
        Log.d(TAG, "Setting integer properties in CordovaActivity will be deprecated in 3.0 on July 2013, please use config.xml");
        getIntent().putExtra(s, i);
    }

    public void setStringProperty(String s, String s1)
    {
        Log.d(TAG, "Setting string properties in CordovaActivity will be deprecated in 3.0 on July 2013, please use config.xml");
        getIntent().putExtra(s, s1);
    }

    protected void showSplashScreen(final int time)
    {
        runOnUiThread(new Runnable() {

            final CordovaActivity this$0;
            final CordovaActivity val$that;
            final int val$time;

            public void run()
            {
                Display display = getWindowManager().getDefaultDisplay();
                LinearLayout linearlayout = new LinearLayout(that.getActivity());
                linearlayout.setMinimumHeight(display.getHeight());
                linearlayout.setMinimumWidth(display.getWidth());
                linearlayout.setOrientation(1);
                linearlayout.setBackgroundColor(that.getIntegerProperty("backgroundColor", 0xff000000));
                linearlayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(-1, -1, 0.0F));
                linearlayout.setBackgroundResource(that.splashscreen);
                splashDialog = new Dialog(that, 0x1030010);
                if ((0x400 & getWindow().getAttributes().flags) == 1024)
                {
                    splashDialog.getWindow().setFlags(1024, 1024);
                }
                splashDialog.setContentView(linearlayout);
                splashDialog.setCancelable(false);
                splashDialog.show();
                (new Handler()).postDelayed(new Runnable() {

                    final _cls5 this$1;

                    public void run()
                    {
                        removeSplashScreen();
                    }

            
            {
                this$1 = _cls5.this;
                super();
            }
                }, time);
            }

            
            {
                this$0 = CordovaActivity.this;
                that = cordovaactivity1;
                time = i;
                super();
            }
        });
    }

    public void showWebPage(String s, boolean flag, boolean flag1, HashMap hashmap)
    {
        if (appView != null)
        {
            appView.showWebPage(s, flag, flag1, hashmap);
        }
    }

    public void spinnerStart(String s, String s1)
    {
        if (spinnerDialog != null)
        {
            spinnerDialog.dismiss();
            spinnerDialog = null;
        }
        spinnerDialog = ProgressDialog.show(this, s, s1, true, true, new android.content.DialogInterface.OnCancelListener() {

            final CordovaActivity this$0;
            final CordovaActivity val$me;

            public void onCancel(DialogInterface dialoginterface)
            {
                me.spinnerDialog = null;
            }

            
            {
                this$0 = CordovaActivity.this;
                me = cordovaactivity1;
                super();
            }
        });
    }

    public void spinnerStop()
    {
        if (spinnerDialog != null && spinnerDialog.isShowing())
        {
            spinnerDialog.dismiss();
            spinnerDialog = null;
        }
    }

    public void startActivityForResult(CordovaPlugin cordovaplugin, Intent intent, int i)
    {
        activityResultCallback = cordovaplugin;
        activityResultKeepRunning = keepRunning;
        if (cordovaplugin != null)
        {
            keepRunning = false;
        }
        super.startActivityForResult(intent, i);
    }

}
