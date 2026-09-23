package omad.shou.game.omadshou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GiftActivity extends AppCompatActivity {
    MediaPlayer mpApplause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        // Toolbarni sozlash
        Toolbar toolbar = findViewById(R.id.toolbarGift);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Orqaga tugma
        }
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(GiftActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish(); // GiftActivityni yopish
        });

        TextView tvGift = findViewById(R.id.tvGift);
        ImageView ivGift = findViewById(R.id.ivGift);

        Intent intent = getIntent();
        String prize = intent.getStringExtra("prize");

        // Matn ko'rsatish
        tvGift.setText("Sizning sovg‘angiz: " + prize + "\n\nMuborak! bo'lsin");

        // Sovg'aga qarab rasm yoki GIF ko'rsatish
        if ("Damas".equals(prize)) {
            ivGift.setImageResource(R.drawable.damas); // res/drawable/damas.png
        } else if ("3 ta quti".equals(prize)) {
            ivGift.setImageResource(R.drawable.quti); // res/drawable/quti.png
        } else {
            ivGift.setImageResource(R.drawable.gift); // default rasm yoki GIF
        }
        // SharedPreferences dan chapak ovozi holatini o'qish
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        boolean isApplauseOn = sharedPreferences.getBoolean("applauseOn", true); // Chapak ovozi holati

        if (isApplauseOn) {
            mpApplause = MediaPlayer.create(this, R.raw.applause);
            mpApplause.start();
            mpApplause.setOnCompletionListener(MediaPlayer::release);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Agar ovoz ijro etilayotgan bo'lsa, uni to'xtatish
        if (mpApplause != null) {
            mpApplause.release();
            mpApplause = null;
        }
    }
}