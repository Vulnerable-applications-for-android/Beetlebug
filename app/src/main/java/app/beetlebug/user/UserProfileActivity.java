package app.beetlebug.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.beetlebug.MainActivity;
import app.beetlebug.R;
import app.beetlebug.utils.CustomProgressBar;

public class UserProfileActivity extends AppCompatActivity {

    CustomProgressBar userProgress;
    ImageView m_btn_back;
    TextView flags_captured;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userProgress = (CustomProgressBar) findViewById(R.id.user_progress_bar);
        sharedPreferences = getSharedPreferences("flag_scores", Context.MODE_PRIVATE);

        m_btn_back = findViewById(R.id.back);

        if(Build.VERSION.SDK_INT>=21){
            Window window=this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }

        m_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        setupProgressBar();
        setUpFlagsCaptured();
    }

    public void setupProgressBar() {

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
        userProgress.setProgressWithAnimation(total_score, 2000);
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


}