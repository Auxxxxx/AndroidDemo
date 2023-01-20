package al.kr.mdfactory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import al.kr.mdfactory.R;

public class WorkFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        toStartShiftFragment();
        return inflater.inflate(R.layout.fragment_work, container, false);
    }

    public static WorkFragment newInstance() {
        return new WorkFragment();
    }

    private void toStartShiftFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_work, StartShiftFragment.newInstance())
                .commit();
    }
}