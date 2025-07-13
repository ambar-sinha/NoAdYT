package com.example.noaddyt;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;


public class MainActivity extends AppCompatActivity {
LinearLayout search, confirm, retry;
WebView webView;
EditText url;
FirebaseFirestore db = FirebaseFirestore.getInstance();
TextView txt;
public static final String ver = "1.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.search);
        confirm = findViewById(R.id.confirm);
        webView = findViewById(R.id.webView);
        url = findViewById(R.id.url);
        retry = findViewById(R.id.retry);
        txt = findViewById(R.id.txt);
        ProgressDialog pb= new ProgressDialog(this);
        pb.setTitle("Please Wait");
        pb.setMessage("Checking for Updates");
        pb.setCancelable(false);
        pb.show();
        webView.setVisibility(GONE);
        confirm.setVisibility(GONE);
        txt.setVisibility(GONE);
        retry.setVisibility(GONE);
        db.collection("AppData").document("Updates").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if(!snapshot.getString("Version").equals(ver))
                {
                    startActivity(new Intent(MainActivity.this, Update.class));
        finish();

                }
                else {
                    pb.dismiss();
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(url.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Please Enter Url", Toast.LENGTH_SHORT).show();
                }
                else {
                    WebSettings webSettings = webView.getSettings();
                    webSettings.setJavaScriptEnabled(true);

                    String inputUrl = url.getText().toString().trim();
                    if (!inputUrl.startsWith("http://") && !inputUrl.startsWith("https://")) {
                        inputUrl = "https://" + inputUrl;
                    }
                    webView.loadUrl(inputUrl);

                    retry.setVisibility(VISIBLE);
                    webView.setVisibility(VISIBLE);
                    confirm.setVisibility(VISIBLE);
                    txt.setVisibility(VISIBLE);
                }
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(MainActivity.this, "Failed to load URL", Toast.LENGTH_SHORT).show();
                webView.setVisibility(GONE);
                confirm.setVisibility(GONE);
                txt.setVisibility(GONE);
                retry.setVisibility(GONE);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VideoActivity.class).putExtra("Url", url.getText().toString()));
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(GONE);
                confirm.setVisibility(GONE);
                txt.setVisibility(GONE);
                retry.setVisibility(GONE);
            }
        });

    }
}
