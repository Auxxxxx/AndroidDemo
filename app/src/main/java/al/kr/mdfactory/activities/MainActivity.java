package al.kr.mdfactory.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.activities.util.Vibrator;
import al.kr.mdfactory.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity implements ToastShower, Vibrator {
    private Toast toast;
    private android.os.Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToast();
        initVibrator();

        if (savedInstanceState == null) {
            toLoginFragment();
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

    private void toLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_main, LoginFragment.newInstance())
                .commit();
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