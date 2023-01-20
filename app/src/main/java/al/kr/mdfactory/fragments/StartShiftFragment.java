package al.kr.mdfactory.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.NavigationController;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.activities.util.Vibrator;
import al.kr.mdfactory.viewModels.LobbyViewModel;
import al.kr.mdfactory.viewModels.StartShiftViewModel;

public class StartShiftFragment extends Fragment {
    public static final String TAG = "%StartShiftFragment%";
    private LobbyViewModel lobbyViewModel;
    private StartShiftViewModel mViewModel;
    private ImageButton startShiftIB;
    private TextView startShiftTV;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lobbyViewModel = new ViewModelProvider(requireActivity()).get(LobbyViewModel.class);
        mViewModel = new ViewModelProvider(this).get(StartShiftViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_start_shift, container, false);
        initViews();
        observe();
        ((NavigationController)requireActivity()).useNavigationButton();
        mViewModel.requestIsShiftStarted(lobbyViewModel.getEmployeeLogin().getValue());
        return rootView;
    }

    public static StartShiftFragment newInstance() {
        return new StartShiftFragment();
    }

    private void initViews(){
        startShiftIB = rootView.findViewById(R.id.IB_start_shift);
        startShiftTV = rootView.findViewById(R.id.TV_start_shift);
    }

    private void observe() {
        mViewModel.getToastMessage().observe(getViewLifecycleOwner(),
                ((ToastShower) requireActivity())::showToast);
        mViewModel.getOnStartShiftRecorded().observe(getViewLifecycleOwner(),
                this::onStartShiftRecorded);
        mViewModel.getIsShiftStarted().observe(getViewLifecycleOwner(),
                this::checkIfShiftStarted);
    }

    private void checkIfShiftStarted(Boolean isShiftStarted) {
        Log.i(TAG, "startShiftChecked");
        if (isShiftStarted) {
            startShiftTV.setText(R.string.continue_shift);
            startShiftIB.setOnClickListener(this::continueShift);
        } else {
            startShiftTV.setText(R.string.start_shift);
            startShiftIB.setOnClickListener(this::startShift);
        }
    }

    private void onStartShiftRecorded(Boolean isStartShiftRecorded) {
        if (isStartShiftRecorded) toStartBatchFragment();
    }

    private void continueShift(View v) {
        toStartBatchFragment();
    }

    private void startShift(View v) {
        ((Vibrator)requireActivity()).vibrateShort();
        mViewModel.recordStartShift(lobbyViewModel.getEmployeeLogin().getValue());
    }

    private void toStartBatchFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_work, StartBatchFragment.newInstance())
                .addToBackStack("StartBatch")
                .commit();
    }
}