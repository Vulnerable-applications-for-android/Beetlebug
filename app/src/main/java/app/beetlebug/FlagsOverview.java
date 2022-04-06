package app.beetlebug;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import app.beetlebug.ctf.BinaryPatchActivity;
import app.beetlebug.ctf.RootDetectorActivity;
import app.beetlebug.ctf.VulnerableActivityIntent;
import app.beetlebug.fragments.AndroidComponentsFragment;
import app.beetlebug.fragments.BiometricFragment;
import app.beetlebug.fragments.DatabasesFragment;
import app.beetlebug.fragments.InsecureStorageFragment;
import app.beetlebug.fragments.DeviceFragment;
import app.beetlebug.fragments.SecretsFragment;
import app.beetlebug.fragments.SensitiveDataFragment;
import app.beetlebug.fragments.WebViewFragment;
import app.beetlebug.user.UserProfileActivity;
import app.beetlebug.utils.CustomProgressBar;

public class FlagsOverview extends AppCompatActivity {
    ImageView mBackButton;
    ScrollView mScrollView;
    RelativeLayout mToolbar;
    Button mfinish;
    TextView flags_captured;


    CustomProgressBar hardcodedSecretsProgressBar;
    CustomProgressBar webViewsProgressBar;
    CustomProgressBar androidComponentsProgressBar;
    CustomProgressBar insecureStoreProgressBar;
    CustomProgressBar sensitiveInfoProgressBar;
    CustomProgressBar databasesProgressBar;
    CustomProgressBar patchDetectionProgressBar;
    CustomProgressBar bioProgressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flags_overview);
        mBackButton = findViewById(R.id.back);
        mScrollView = findViewById(R.id.scrollview_flags);
        mToolbar = findViewById(R.id.toolbar);

        hardcodedSecretsProgressBar = findViewById(R.id.hardcoded_secrets_bar);
        webViewsProgressBar = findViewById(R.id.progress_bar_webview);
        insecureStoreProgressBar = findViewById(R.id.progress_bar_local_storage);
        androidComponentsProgressBar = findViewById(R.id.progress_bar_components);
        sensitiveInfoProgressBar = findViewById(R.id.progress_bar_sensitive_info);
        patchDetectionProgressBar = findViewById(R.id.progress_bar_binary);
        databasesProgressBar = findViewById(R.id.progress_bar_database);
        webViewsProgressBar = findViewById(R.id.progress_bar_webview);
        bioProgressBar = findViewById(R.id.progress_bar_bio);

        mfinish = findViewById(R.id.finish_button);

        sharedPreferences = getSharedPreferences("flag_scores", Context.MODE_PRIVATE);

        if(Build.VERSION.SDK_INT>=21){
            Window window=this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home_intent = new Intent(FlagsOverview.this, MainActivity.class);
                startActivity(home_intent);
            }
        });

        setUpFlagsCaptured();

        setupProgressBarInsecureStorage();
        setupProgressBarHardCodedSecrets();
        setUpProgressBarAndroidComponents();
        setUpProgressBarInfoDiscl();
        setUpProgressBarDatabases();
        setUpProgressBarWebViews();
        setUpProgressBarPatch();
        setUpProgressBarAuth();
    }

    private void setUpProgressBarAuth() {
        int auth_score = sharedPreferences.getInt("ctf_score_auth", 0);
        String total_string = Integer.toString(auth_score);
        if (total_string.equals("5")) {
            bioProgressBar.setProgressWithAnimation(100, 2000);
        }
    }
    private void setUpProgressBarAndroidComponents() {
        int service_score = sharedPreferences.getInt("ctf_score_service", 0);
        int content_score = sharedPreferences.getInt("ctf_score_content_provider", 0);
        int intent_redirect_score = sharedPreferences.getInt("ctf_score_intent_redirect", 0);

        int total_score = service_score + content_score + intent_redirect_score;
        String total_string = Integer.toString(total_score);

        if(total_string.equals("5")) {
            androidComponentsProgressBar.setProgressWithAnimation(33, 2000);
        } else if (total_string.equals("10")){
            androidComponentsProgressBar.setProgressWithAnimation(66, 2000);
        } else if (total_string.equals("15"))
            androidComponentsProgressBar.setProgressWithAnimation(100, 2000);
    }


    private void setUpProgressBarWebViews() {
        int xss_score = sharedPreferences.getInt("ctf_score_xss", 0);
        int webview_score = sharedPreferences.getInt("ctf_score_webview", 0);
        int total_score = xss_score + webview_score;
        String total_string = Integer.toString(total_score);

        if(total_string.equals("5")) {
            webViewsProgressBar.setProgressWithAnimation(50, 2000);
        } else if (total_string.equals("10")) {
            webViewsProgressBar.setProgressWithAnimation(100, 2000);
        }
    }


    private void setUpProgressBarInfoDiscl() {
        int log_score = sharedPreferences.getInt("ctf_score_log", 0);
        int clip_score = sharedPreferences.getInt("ctf_score_clip", 0);
        int total_score = log_score + clip_score;
        String total_string = Integer.toString(total_score);
        if(total_string.equals("5")) {
            sensitiveInfoProgressBar.setProgressWithAnimation(50, 2000);
        } else if (total_string.equals("10")) {
            sensitiveInfoProgressBar.setProgressWithAnimation(100, 2000);
        }
    }

    private void setUpProgressBarDatabases() {
        int firebase_score = sharedPreferences.getInt("ctf_score_firebase", 0);
        int sqli_score = sharedPreferences.getInt("ctf_score_sqli", 0);

        int total_score = firebase_score + sqli_score;
        String total_string = Integer.toString(total_score);

        if(total_string.equals("5")) {
            databasesProgressBar.setProgressWithAnimation(50, 2000);
        } else if (total_string.equals("10")) {
            databasesProgressBar.setProgressWithAnimation(100, 2000);
        }
    }

    private void setUpProgressBarPatch() {
        int root_score = sharedPreferences.getInt("ctf_score_patch", 0);
        String score = Integer.toString(root_score);
        if(score.equals("5"))
        patchDetectionProgressBar.setProgressWithAnimation(100, 2000);
    }



    public void inSecureStorage (View v) {
        mScrollView.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        Fragment fragment = new InsecureStorageFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    public void biometricAuth (View v) {
        mScrollView.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        Fragment fragment = new BiometricFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    public void  webViewFlag (View v) {
        mScrollView.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        Fragment fragment = new WebViewFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    public void  inSecureActivity (View v) {
        Intent i = new Intent(FlagsOverview.this, VulnerableActivityIntent.class);
        startActivity(i);
    }


    public void AndroidComponents(View view) {
        mScrollView.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        Fragment fragment = new AndroidComponentsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    public void databaseFlag(View view) {
        mScrollView.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        Fragment fragment = new DatabasesFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }


    // Setup Progress Bar
    private void setupProgressBarHardCodedSecrets() {
        int secret_string_score = sharedPreferences.getInt("ctf_score_secret_string", 0);
        int secret_source_score = sharedPreferences.getInt("ctf_score_secret_source", 0);

        int total_score = secret_source_score + secret_string_score;
//        Toast.makeText(FlagsOverview.this, "Total Score: " + total_score, Toast.LENGTH_LONG).show();
        String total_string = Integer.toString(total_score);
        if(total_string.equals("5")) {
            hardcodedSecretsProgressBar.setProgressWithAnimation(50, 2000);
        } else if (total_string.equals("10")){
            hardcodedSecretsProgressBar.setProgressWithAnimation(100, 2000);
        }
    }

    private void setupProgressBarInsecureStorage() {
        int external_str_score = sharedPreferences.getInt("ctf_score_external", 0);
        int shared_pref_score = sharedPreferences.getInt("ctf_score_shared_pref", 0);
        int sqlite_score = sharedPreferences.getInt("ctf_score_sqlite", 0);

        int total_score = external_str_score + shared_pref_score + sqlite_score;
        String total_string = Integer.toString(total_score);

        if(total_string.equals("5")) {
            insecureStoreProgressBar.setProgressWithAnimation(33, 2000);
        } else if (total_string.equals("10")){
            insecureStoreProgressBar.setProgressWithAnimation(66, 2000);
        } else if (total_string.equals("15"))
        insecureStoreProgressBar.setProgressWithAnimation(100, 2000);
    }




    public void inSecureLoggingFlag(View view) {
        mScrollView.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        Fragment fragment = new SensitiveDataFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    public void patchBinary (View view) {
        Intent i = new Intent(FlagsOverview.this, BinaryPatchActivity.class);
        startActivity(i);
    }
    public void submitFlags(View view) {
        final Dialog finish_dialog = new Dialog(FlagsOverview.this);
        finish_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // retrieve ctf score from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("flag_scores", Context.MODE_PRIVATE);
        int secret_source_score = sharedPreferences.getInt("ctf_score_secret_source", 0);
        int secret_string_score = sharedPreferences.getInt("ctf_score_secret_string", 0);
        int shared_pref_score = sharedPreferences.getInt("ctf_score_shared_pref", 0);
        int sqlite_score = sharedPreferences.getInt("ctf_score_sqlite", 0);
        int external_str_score = sharedPreferences.getInt("ctf_score_external", 0);
        int xss_score = sharedPreferences.getInt("ctf_score_xss", 0);
        int webview_score = sharedPreferences.getInt("ctf_score_webview", 0);
        int intent_redirect_score = sharedPreferences.getInt("ctf_score_intent_redirect", 0);
        int service_score = sharedPreferences.getInt("ctf_score_service", 0);
        int content_score = sharedPreferences.getInt("ctf_score_content_provider", 0);
        int auth_score = sharedPreferences.getInt("ctf_score_auth", 0);
        int clip_score = sharedPreferences.getInt("ctf_score_clip", 0);
        int log_score = sharedPreferences.getInt("ctf_score_log", 0);
        int firebase_score = sharedPreferences.getInt("ctf_score_firebase", 0);
        int sqli_score = sharedPreferences.getInt("ctf_score_sqli", 0);
        int patch_score = sharedPreferences.getInt("ctf_score_patch", 0);



        int total_score = sqlite_score + shared_pref_score + secret_source_score + secret_string_score + external_str_score + firebase_score
                + sqli_score + intent_redirect_score + service_score + log_score + xss_score + content_score + patch_score
                + clip_score + auth_score + webview_score;

        if (total_score < 50) {
            finish_dialog.setContentView(R.layout.try_again_sheet);
            finish_dialog.show();
            finish_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            finish_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            finish_dialog.getWindow().setGravity(Gravity.BOTTOM);
        } else if (total_score <= 45) {
            finish_dialog.setContentView(R.layout.bottom_sheet_continue);
            finish_dialog.show();
            finish_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            finish_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            finish_dialog.getWindow().setGravity(Gravity.BOTTOM);
        } else if (total_score >= 60) {
            finish_dialog.setContentView(R.layout.bottom_sheet);
            finish_dialog.show();
            finish_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            finish_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            finish_dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
    }


    public void shareResult(View view) {
        String url = "https://github.com/hafiz-ng/beetlebug";

        // define MIME type
        String mimeType = "text/plain";

        String author_handle = "@hafiz__ng";

        // create a share widget, with options on how to share the text
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setText("Get Started today " + url + " Developer - " + author_handle)
                .startChooser();
    }

    public void embeddedSecrets(View view) {
        mScrollView.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        Fragment fragment = new SecretsFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment).commit();
    }

    public void setUpFlagsCaptured() {
        // retrieve ctf score from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("flag_scores", Context.MODE_PRIVATE);
        int sqlite_score = sharedPreferences.getInt("ctf_score_sqlite", 0);
        int shared_pref_score = sharedPreferences.getInt("ctf_score_shared_pref", 0);
        int secret_source_score = sharedPreferences.getInt("ctf_score_secret_source", 0);
        int secret_string_score = sharedPreferences.getInt("ctf_score_secret_string", 0);
        int external_str_score = sharedPreferences.getInt("ctf_score_external", 0);
        int firebase_score = sharedPreferences.getInt("ctf_score_firebase", 0);
        int sqli_score = sharedPreferences.getInt("ctf_score_sqli", 0);
        int intent_redirect_score = sharedPreferences.getInt("ctf_score_intent_redirect", 0);
        int service_score = sharedPreferences.getInt("ctf_score_service", 0);
        int log_score = sharedPreferences.getInt("ctf_score_log", 0);
        int xss_score = sharedPreferences.getInt("ctf_score_xss", 0);
        int content_score = sharedPreferences.getInt("ctf_score_content_provider", 0);
        int patch_score = sharedPreferences.getInt("ctf_score_patch", 0);
        int clip_score = sharedPreferences.getInt("ctf_score_clip", 0);
        int auth_score = sharedPreferences.getInt("ctf_score_auth", 0);

        int total_score = sqlite_score + shared_pref_score + secret_source_score + secret_string_score + external_str_score + firebase_score
                + sqli_score + intent_redirect_score + service_score + log_score + xss_score + content_score + patch_score
                + clip_score + auth_score;

        String str_score = Integer.toString(total_score);

        flags_captured = findViewById(R.id.flag_score);

        switch(str_score) {
            case "5" :
                flags_captured.setText("1");
                break;
            case "10" :
                flags_captured.setText("2");
                break;
            case "15" :
                flags_captured.setText("3");
                break;
            case "20" :
                flags_captured.setText("4");
                break;
            case "25" :
                flags_captured.setText("5");
                break;
            case "30" :
                flags_captured.setText("6");
                break;
            case "35" :
                flags_captured.setText("7");
                break;
            case "40" :
                flags_captured.setText("8");
                break;
            case "45" :
                flags_captured.setText("9");
                break;
            case "50" :
                flags_captured.setText("10");
                break;
            case "55" :
                flags_captured.setText("11");
                break;
            case "60" :
                flags_captured.setText("12");
                break;
            case "65" :
                flags_captured.setText("13");
                break;
            case "70" :
                flags_captured.setText("14");
                break;
            case "85" :
                flags_captured.setText("15");
                break;
            case "100" :
                flags_captured.setText("16");
                break;

            default :
                flags_captured.setText("0");// Optional
        }

    }

    public void continueFlag(View view) {
//        final Dialog finish_dialog = new Dialog(FlagsOverview.this);
//        finish_dialog.hide();
        Intent continue_ctf = new Intent(FlagsOverview.this, FlagsOverview.class);
        startActivity(continue_ctf);
    }
}