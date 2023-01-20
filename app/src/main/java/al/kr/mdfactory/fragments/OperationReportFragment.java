package al.kr.mdfactory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import al.kr.mdfactory.R;

public class OperationReportFragment extends Fragment {
    private static final String TAG = "%OperationReportFragment%";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_operation_report, container, false);
    }

    public static OperationReportFragment newInstance() {
        return new OperationReportFragment();
    }

}