package al.kr.mdfactory.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.NavigationController;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.activities.util.Vibrator;
import al.kr.mdfactory.fragments.LobbyFragment;
import al.kr.mdfactory.viewModels.LobbyViewModel;


public class LobbyActivity extends AppCompatActivity implements ToastShower,
        NavigationController, Vibrator {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Toast toast;
    private android.os.Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        setSupportActionBar(findViewById(R.id.toolbar_lobby));

        initViews();
        initToast();
        initVibrator();

        setEmployeeLogin(getIntent().getStringExtra("employeeLogin"));
        if (savedInstanceState == null){
            toLobbyFragment();
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


    private void initViews() {
        toolbar = findViewById(R.id.toolbar_lobby);
        drawerLayout = findViewById(R.id.drawer_lobby);
    }

    @Override
    public void useNavigationButton() {
        toolbar.setNavigationIcon(R.drawable.ic_navigation24);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }

    @Override
    public void useBackButton(View.OnClickListener l) {
        toolbar.setNavigationIcon(R.drawable.ic_back24);
        toolbar.setNavigationOnClickListener(l);
    }

    private void setEmployeeLogin(String employeeLogin) {
        LobbyViewModel lobbyViewModel = new ViewModelProvider(this).get(LobbyViewModel.class);
        lobbyViewModel.getEmployeeLogin().setValue(employeeLogin);
    }

    private void toLobbyFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_lobby, LobbyFragment.newInstance())
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void disableNavigationButton() {
        toolbar.setNavigationIcon(null);
        toolbar.setNavigationOnClickListener(null);
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