package omad.shou.game.omadshou;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView name = findViewById(R.id.name);
        TextView text = findViewById(R.id.text);

        name.setText("OMAD SHOU");
        text.setText("OMAD SHOU ONLINE O'YINI");

        // 2 soniyadan keyin MainActivity ga o'tadi
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 2000);
    }
}