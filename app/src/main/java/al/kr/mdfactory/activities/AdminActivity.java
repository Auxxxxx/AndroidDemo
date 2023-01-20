package al.kr.mdfactory.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.activities.util.Vibrator;
import al.kr.mdfactory.fragments.AdminFragment;

public class AdminActivity extends AppCompatActivity implements ToastShower, Vibrator {
    private Toast toast;
    private android.os.Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        initToast();
        initVibrator();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_admin, AdminFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void initToast() {
        toast = Toast.makeText(getApplicationContext(), "message not set", Toast.LENGTH_SHORT);
    }

    @Override
    public void showToast(String message) {
        toast.setText(message);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initVibrator() {
        vibrator = (android.os.Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void vibrateShort() {
        vibrator.vibrate(100);
    }

    @Override
    public void vibrateLong() {
        vibrator.vibrate(500);
    }
}