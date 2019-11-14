package kr.co.itforone.canadaw;

import android.app.Activity;
import android.webkit.JavascriptInterface;

class WebviewJavainterface {
    Activity activity;
    MainActivity mainActivity;


    WebviewJavainterface (MainActivity mainActivity){
        this.mainActivity=mainActivity;
    }
    WebviewJavainterface(Activity activity){
        this.activity=activity;
    }

    @JavascriptInterface
    public void share(String id, String table) {
    }
}
