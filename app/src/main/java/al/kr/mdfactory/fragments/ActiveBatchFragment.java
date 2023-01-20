package al.kr.mdfactory.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.NavigationController;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.activities.util.Vibrator;
import al.kr.mdfactory.models.Operation;
import al.kr.mdfactory.viewModels.ActiveBatchViewModel;
import al.kr.mdfactory.viewModels.LobbyViewModel;
import al.kr.mdfactory.viewModels.WorkViewModel;

public class ActiveBatchFragment extends Fragment implements BreakDialogFragment.BreakDialogListener {
    private static final int ITEM_POSTPONE = R.id.item_postpone;
    private static final int ITEM_START_BREAK = R.id.item_start_break;
    private static final String TAG = "%ActiveBatchFragment%";
    private LobbyViewModel lobbyViewModel;
    private WorkViewModel workViewModel;
    private ActiveBatchViewModel mViewModel;
    private Button finishBatchBTN;
    private Chronometer chronometer;
    private RadioGroup operationsRG;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lobbyViewModel = new ViewModelProvider(requireActivity()).get(LobbyViewModel.class);
        workViewModel = new ViewModelProvider(requireActivity()).get(WorkViewModel.class);
        mViewModel = new ViewModelProvider(this).get(ActiveBatchViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_active_batch, container, false);
        initViews();
        ((NavigationController)requireActivity()).disableNavigationButton();
        observe();
        mViewModel.requestOperations(lobbyViewModel.getEmployeeLogin().getValue(),
                workViewModel.getItinerary().getValue());
        initListeners();
        startChronometer();
        mViewModel.getStartTime().setValue(LocalTime.now());
        return rootView;
    }

    public static ActiveBatchFragment newInstance() {
        return new ActiveBatchFragment();
    }

    private void initViews(){
        chronometer = rootView.findViewById(R.id.chronometer);
        operationsRG = rootView.findViewById(R.id.RG_operations);
        finishBatchBTN = rootView.findViewById(R.id.BTN_finish);

        TextView itineraryTV = rootView.findViewById(R.id.TV_itinerary);
        itineraryTV.setText(workViewModel.getItinerary().getValue());
    }

    private void observe() {
        mViewModel.getOperations().observe(getViewLifecycleOwner(),
                this::operationsReceived);
        mViewModel.getToastMessage().observe(getViewLifecycleOwner(),
                ((ToastShower)requireActivity())::showToast);
        mViewModel.getOnBatchAdded().observe(getViewLifecycleOwner(),
                this::onBatchAdded);
    }

    private void operationsReceived(@NonNull List<Operation> operations) {
        LayoutInflater inflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Operation operation : operations) {
            RadioButton radioButton = (RadioButton) inflater.inflate(
                    R.layout.item_batch_operation,operationsRG,false);
            radioButton.setId(Math.toIntExact(operation.getId()));
            radioButton.setText(operation.getName());
            radioButton.setOnClickListener(v -> mViewModel.getSelectedOperation().setValue(operation));
            operationsRG.addView(radioButton);
        }
    }

    private void onBatchAdded(Boolean isBatchAdded) {
        if (isBatchAdded) {
            toStartBatchFragment();
        }
    }

    private void initListeners() {
        finishBatchBTN.setOnClickListener(v -> onFinishClicked());
    }

    private void onFinishClicked() {
        ((Vibrator)requireActivity()).vibrateShort();
        mViewModel.getFinishTime().setValue(LocalTime.now());
        mViewModel.recordBatch(lobbyViewModel.getEmployeeLogin().getValue());
    }

    private void startChronometer() {
        chronometer.start();
        chronometer.setOnChronometerTickListener(this::onBatchChronometerTick);
    }

    private void onBatchChronometerTick(Chronometer c) {
        long elapsedMillis = SystemClock.elapsedRealtime() - c.getBase();
        //Log.i(TAG, ""+elapsedMillis);
        mViewModel.getBatchDuration().setValue(Duration.ofMillis(elapsedMillis));
        if (5 * 1000 < elapsedMillis && elapsedMillis < 10 * 1000) {
            finishBatchBTN.setVisibility(View.VISIBLE);
            finishBatchBTN.setEnabled(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_basic, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case ITEM_POSTPONE:
                //TODO: implement case behavior
                break;
            case ITEM_START_BREAK:
                startBreak();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startBreak() {
        chronometer.stop();
        BreakDialogFragment.newInstance(this).show(getParentFragmentManager(), "dialog");
    }

    @Override
    public void onBreakDetach(Long elapsedMillis) {
        long chronometerBase = (long)Math.ceil(chronometer.getBase()/ 1000.0) * 1000;
        chronometer.setBase(chronometerBase + elapsedMillis);
        chronometer.start();
    }

    @Override
    public void onBreakStop(Long elapsedMillis) {
        mViewModel.saveBreak(elapsedMillis);
    }

    private void toStartBatchFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_work, StartBatchFragment.newInstance())
                .commit();
    }
}





