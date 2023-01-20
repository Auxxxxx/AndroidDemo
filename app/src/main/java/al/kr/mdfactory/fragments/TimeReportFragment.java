package al.kr.mdfactory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import al.kr.mdfactory.R;

public class TimeReportFragment extends Fragment {
    private static final String TAG = "%TimeReportFragment%";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_time_report, container, false);
    }

    public static TimeReportFragment newInstance() {
        return new TimeReportFragment();
    }

}