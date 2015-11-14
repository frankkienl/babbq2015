package nl.frankkienl.babbq2015;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

/**
 * Created by FrankkieNL on 11/14/2015.
 */
public class WebActivity extends AppCompatActivity {

    private static final String EXTRA_CUSTOM_TABS_SESSION = "android.support.customtabs.extra.SESSION";
    public static final String EXAMPLE_URL = "http://jqtjs.com/preview/demos/main/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
    }

    public void startWebview() {
        WebView wv = (WebView) findViewById(R.id.web_webview);
        wv.setVisibility(View.VISIBLE);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        wv.loadUrl(EXAMPLE_URL);
    }

    public void initUI() {
        Button wvBtn = (Button) findViewById(R.id.web_webview_btn);
        wvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWebview();
            }
        });

        Button externalBtn = (Button) findViewById(R.id.web_external_btn);
        externalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXAMPLE_URL));
                if (Build.VERSION.SDK_INT >= 18) {
                    //when possible, try to use Chrome custom tab (better performance than webview)
                    Bundle extras = new Bundle();
                    extras.putBinder(EXTRA_CUSTOM_TABS_SESSION,
                            null); //null for no session.
                    intent.putExtras(extras);
                }
                startActivity(intent);
            }
        });
    }
}
