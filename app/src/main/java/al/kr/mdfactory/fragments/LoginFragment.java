package al.kr.mdfactory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.AdminActivity;
import al.kr.mdfactory.activities.LobbyActivity;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.activities.util.Vibrator;
import al.kr.mdfactory.fragments.util.MyTextWatcher;
import al.kr.mdfactory.viewModels.LoginViewModel;

public class LoginFragment extends Fragment {
    public static final String TAG = "%LoginFragment%";
    private LoginViewModel mViewModel;
    private EditText loginET;
    private EditText passwordET;
    private Button startBTN;
    private Button adminBTN;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initViews();
        observe();
        initListeners();
        return rootView;
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private void initViews() {
        loginET = rootView.findViewById(R.id.ET_login);
        passwordET = rootView.findViewById(R.id.ET_password);
        startBTN = rootView.findViewById(R.id.BTN_main_start);
        adminBTN = rootView.findViewById(R.id.BTN_as_admin);
    }

    public void observe() {
        mViewModel.getToastMessage().observe(getViewLifecycleOwner(),
                ((ToastShower)requireActivity())::showToast);
        mViewModel.getSuccessfulLogin().observe(getViewLifecycleOwner(),
                this::onSuccessfulLogin);
        mViewModel.getLogin().observe(getViewLifecycleOwner(), s -> observeET(s, loginET));
        mViewModel.getPassword().observe(getViewLifecycleOwner(), s -> observeET(s, passwordET));
    }

    private void observeET(String s, EditText editText) {
        if (!editText.getText().toString().equals(s)) {
            editText.setText(s);
        }
    }

    private void initListeners(){
        loginET.addTextChangedListener(MyTextWatcher.of(mViewModel.getLogin()));
        passwordET.addTextChangedListener(MyTextWatcher.of(mViewModel.getPassword()));

        startBTN.setOnClickListener(v -> onStartButtonClicked());
        adminBTN.setOnClickListener(v -> onAdminButtonClicked());
    }

    private void onStartButtonClicked() {
        ((Vibrator)requireActivity()).vibrateShort();
        disableViewForTimeout(startBTN, 1);
        mViewModel.performLogIn();
    }

    private static void disableViewForTimeout(View v, long timeoutSeconds) {
        v.setEnabled(false);
        new Handler().postDelayed(() -> v.setEnabled(true),timeoutSeconds * 1000);
    }

    private void onAdminButtonClicked() {
        ((Vibrator)requireActivity()).vibrateShort();
        Intent intent = new Intent(getActivity(), AdminActivity.class);
        startActivity(intent);
    }

    private void onSuccessfulLogin(String employeeLogin) {
        Intent intent = new Intent(requireActivity(), LobbyActivity.class);
        intent.putExtra("employeeLogin", employeeLogin);
        startActivity(intent);
    }
}