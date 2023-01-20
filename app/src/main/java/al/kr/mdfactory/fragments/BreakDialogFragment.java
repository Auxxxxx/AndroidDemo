package al.kr.mdfactory.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import al.kr.mdfactory.R;
import al.kr.mdfactory.viewModels.BreakDialogViewModel;

public class BreakDialogFragment extends DialogFragment {
    private BreakDialogListener breakDialogListener;
    private BreakDialogViewModel mViewModel;
    private Chronometer chronometer;
    private Button continueBTN;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE,  R.style.MyDialog);
        mViewModel = new ViewModelProvider(this).get(BreakDialogViewModel.class);
    }

    public static BreakDialogFragment newInstance(BreakDialogListener breakDialogListener) {
        BreakDialogFragment breakDialogFragment = new BreakDialogFragment();
        breakDialogFragment.breakDialogListener = breakDialogListener;
        return breakDialogFragment;
    }

    public interface BreakDialogListener {
        void onBreakDetach(Long elapsedMillis);
        void onBreakStop(Long elapsedMillis);
    }

    @NonNull
    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        rootView = getLayoutInflater().inflate(R.layout.fragment_break_dialog, null);
        initViews();
        initListeners();
        //setCancelable(false);
        builder.setView(rootView);
        chronometer.start();
        return builder.create();
    }

    private void initViews() {
        chronometer = rootView.findViewById(R.id.chronometer);
        continueBTN = rootView.findViewById(R.id.BTN_continue);
    }

    private void initListeners() {
        chronometer.setOnChronometerTickListener(this::onBreakChronometerTick);
        continueBTN.setOnClickListener(v -> dismiss());
    }

    private void onBreakChronometerTick(Chronometer c) {
        long elapsedMillis = SystemClock.elapsedRealtime() - c.getBase();
        mViewModel.getBreakTime().setValue(elapsedMillis);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        chronometer.stop();
        breakDialogListener.onBreakDetach(mViewModel.getBreakTime().getValue());
    }

    @Override
    public void onStop() {
        super.onStop();
        breakDialogListener.onBreakStop(mViewModel.getBreakTime().getValue());
    }
}
