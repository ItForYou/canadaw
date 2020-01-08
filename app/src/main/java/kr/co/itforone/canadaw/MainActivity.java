package kr.co.itforone.canadaw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.ViewTreeObserver;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.apache.http.util.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.webview)   WebView webView;
    @BindView(R.id.refreshlayout)   SwipeRefreshLayout refreshlayout;
    private long backPrssedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //버터나이프사용
        ButterKnife.bind(this);
        //스플레시 인텐트
        Intent splash = new Intent(this,SplashActivity.class);
        startActivity(splash);
        //웹뷰 세팅
        webView.addJavascriptInterface(new WebviewJavainterface(this),"Android");
        webView.setWebViewClient(new ClientManager(this));
        webView.setWebChromeClient(new ChoromeManager(this, this));
        WebSettings settings = webView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);//캐쉬 사용여부
        settings.setDomStorageEnabled(true);//HTML5에서 DOM 사용여부

        //자동로그인
        SharedPreferences sf = getSharedPreferences("lginfo",MODE_PRIVATE);
        String id = sf.getString("id","");


        //Toast.makeText(getApplicationContext(),id+pwd,Toast.LENGTH_LONG).show();

        //새로고침
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.clearCache(true);
                webView.reload();
                refreshlayout.setRefreshing(false);
            }
        });

        refreshlayout.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(webView.getScrollY() == 0){
                    refreshlayout.setEnabled(true);
                }
                else{
                    refreshlayout.setEnabled(false);
                }
            }
        });

        if(!id.equals("")){
            webView.loadUrl(getString(R.string.chklogin)+id);
        }
        else {
            webView.loadUrl(getString(R.string.index));
        }
    }

    //뒤로가기이벤트
    @Override
    public void onBackPressed(){
        WebBackForwardList historylist = webView.copyBackForwardList();
        String backUrl = historylist.getItemAtIndex(historylist.getCurrentIndex() - 1).getUrl();

        if(backUrl.contains("lotto.game.update.php")) {
            webView.clearHistory();
            webView.loadUrl(getString(R.string.index));
        }
        else if(webView.canGoBack()){
            webView.goBack();
        }else{
            long tempTime = System.currentTimeMillis();
            long intervalTime = tempTime - backPrssedTime;
            if (0 <= intervalTime && 2000 >= intervalTime){
                finish();
            }
            else
            {
                backPrssedTime = tempTime;
                Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누를시 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
