package al.kr.mdfactory.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.NavigationController;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.activities.util.Vibrator;
import al.kr.mdfactory.viewModels.LobbyViewModel;
import al.kr.mdfactory.viewModels.StartBatchViewModel;


public class StartBatchFragment extends Fragment implements BreakDialogFragment.BreakDialogListener {
    private static final int ITEM_START_BREAK = R.id.item_start_break;
    public static final String TAG = "%StartBatchFragment%";
    private LobbyViewModel lobbyViewModel;
    private StartBatchViewModel mViewModel;
    private ImageButton startBatchBTN;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lobbyViewModel = new ViewModelProvider(requireActivity()).get(LobbyViewModel.class);
        mViewModel = new ViewModelProvider(this).get(StartBatchViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_start_batch, container, false);
        initViews();
        observe();
        ((NavigationController)requireActivity()).useBackButton(this::onBackButtonClicked);
        initListeners();
        return rootView;
    }

    public static StartBatchFragment newInstance() {
        return new StartBatchFragment();
    }

    private void initViews(){
        startBatchBTN = rootView.findViewById(R.id.BTN_start_batch);
    }

    private void observe() {
        mViewModel.getToastMessage().observe(getViewLifecycleOwner(),
                ((ToastShower)requireActivity())::showToast);
        mViewModel.getOnBreakRecorded().observe(getViewLifecycleOwner(),
                this::onBreakRecorded);
    }

    private void onBreakRecorded(Boolean isBreakRecorded) {
        if (isBreakRecorded) {
            ((ToastShower)requireActivity()).showToast("Перерыв сохранён");
        }
    }

    private void initListeners() {
        startBatchBTN.setOnClickListener(v -> onStartBatchClicked());
    }

    private void onBackButtonClicked(View v) {
        ((Vibrator)requireActivity()).vibrateShort();
        toStartShiftFragment();
    }

    private void onStartBatchClicked() {
        Log.i(TAG, "start_batch");
        ((Vibrator)requireActivity()).vibrateShort();
        toConfirmBatchFragment();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_break, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case ITEM_START_BREAK:
                startBreak();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startBreak() {
        BreakDialogFragment.newInstance(this).show(getParentFragmentManager(), "dialog");
    }

    @Override
    public void onBreakDetach(Long elapsedMillis) {
    }

    @Override
    public void onBreakStop(Long elapsedMillis) {
        mViewModel.saveBreak(elapsedMillis);
        mViewModel.recordBreak(lobbyViewModel.getEmployeeLogin().getValue());
    }


    private void toStartShiftFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_work, StartShiftFragment.newInstance())
                .commit();
    }

    private void toConfirmBatchFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_work, ConfirmBatchFragment.newInstance())
                .addToBackStack("ConfirmBatch")
                .commit();
    }
}












