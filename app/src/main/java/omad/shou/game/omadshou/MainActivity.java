package omad.shou.game.omadshou;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView wheel;
    ImageView greenCheck;
    Button btnSpin;
    Random random;
    int spinCount = 0; // nechta aylantirildi
    final int MAX_SPINS = 3;
    ArrayList<String> prizes = new ArrayList<>();
    ArrayList<String> results = new ArrayList<>();
    MediaPlayer mpBackground;
    MediaPlayer mpApplause; // Ovoz uchun MediaPlayer
    boolean isApplauseOn; // Chapak ovozi holati

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbarGift);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Orqaga tugma
        }

        toolbar.setNavigationOnClickListener(v -> finish());

        // SharedPreferences dan musiqa holatini o'qish
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        boolean isMusicOn = sharedPreferences.getBoolean("musicOn", false);
        isApplauseOn = sharedPreferences.getBoolean("applauseOn", true); // Chapak ovozi holatini oling

        // Fon musiqasi
        if (isMusicOn) {
            mpBackground = MediaPlayer.create(this, R.raw.background);
            mpBackground.setLooping(true); // Takrorlansin
            mpBackground.setVolume(0.5f, 0.5f);
            mpBackground.start();
        }

        wheel = findViewById(R.id.wheelImage);
        greenCheck = findViewById(R.id.greenCheck);
        btnSpin = findViewById(R.id.btnSpin);
        random = new Random();

        prizes.clear();
        for (int i = 0; i < 31; i++) {
            prizes.add("Damas");
        }
        for (int i = 0; i < 8; i++) {
            prizes.add("Damas + Maishiy texnika");
        }
        prizes.add("3 ta quti");

        btnSpin.setOnClickListener(v -> spinWheel());
    }

    private void spinWheel() {
        if (spinCount >= MAX_SPINS) {
            spinCount = 0;
        }
        // Yashil belgini yashirish
        greenCheck.setVisibility(ImageView.GONE);

        btnSpin.setEnabled(false);

        int degree = random.nextInt(360) + 720; // kamida 2 aylanish

        RotateAnimation rotate = new RotateAnimation(
                0,
                degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotate.setDuration(3000);
        rotate.setFillAfter(true);

        rotate.setAnimationListener(
                new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(
                    android.view.animation.Animation animation) {
            }

            @Override
            public void onAnimationEnd(
                    android.view.animation.Animation animation) {

                spinCount++;
                int index = random.nextInt(prizes.size());
                String prize = prizes.get(index);

                results.add(prize);

                // Yashil belgini ko‘rsatish
                greenCheck.setVisibility(ImageView.VISIBLE);

                showSingleResultDialog(prize);

                btnSpin.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(
                    android.view.animation.Animation animation) {
            }
         }
      );
      wheel.startAnimation(rotate);
    }
    private void showSingleResultDialog(String prize) {
        int remaining = MAX_SPINS - spinCount;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Natija")
                .setMessage(spinCount + "-aylantirish natijasi:\n\n" + prize)
                .setPositiveButton("Olaman", (d, w) -> {
                    if (spinCount >= MAX_SPINS) {
                        // 3 marta aylanish tugadi, GiftActivity ga o'tish
                        Intent intent = new Intent(MainActivity.this, GiftActivity.class);
                        intent.putExtra("prize", prize);
                        startActivity(intent);
                        finish(); // MainActivityni yopish
                        // Oxirgi spin, sovg‘a olganda olqish ovozi
                        if (isApplauseOn) {
                            MediaPlayer mpApplause = MediaPlayer.create(MainActivity.this, R.raw.applause);
                            mpApplause.start();
                            mpApplause.setOnCompletionListener(MediaPlayer::release);
                        }
                    } else {
                        Toast.makeText(this, "Siz sovg‘ani oldingiz: " + prize, Toast.LENGTH_SHORT).show();
                    }
                });

        if (remaining > 0) {
            builder.setNegativeButton("Olmayman", (d, w) -> {
                Toast.makeText(this, "Sizda yana " + remaining + " imkoniyat qoldi", Toast.LENGTH_SHORT).show();
            });
        } else {
            builder.setNegativeButton("Olmayman", (d, w) -> {
                Toast.makeText(this, "Imkoniyat tugadi!", Toast.LENGTH_SHORT).show();
            });
        }

        builder.setCancelable(false);
        builder.show();
    }

    // Menyu yaratish
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menyu elementini tekshirish
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            finish(); // Agar hozirgi activityni yopmoqchi bo'lsangiz
            return true; // O'zgarishlarni qabul qilish
        }
        return super.onOptionsItemSelected(item); // Boshqa elementlar uchun default xulq-atvor
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Fon musiqasini to‘xtatish
        if (mpBackground != null) {
            mpBackground.stop();
            mpBackground.release();
            mpBackground = null;
        }
        if (mpApplause != null) {
            mpApplause.release();
            mpApplause = null;
        }
    }
}
