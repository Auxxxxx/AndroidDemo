package al.kr.mdfactory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import al.kr.mdfactory.R;

public class EmployeeSettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        toEmployeeListFragment();
        return inflater.inflate(R.layout.fragment_employee_settings, container, false);
    }

    public static EmployeeSettingsFragment newInstance() {
        return new EmployeeSettingsFragment();
    }

    private void toEmployeeListFragment() {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.frame_employee_settings, EmployeeListFragment.newInstance())
                .addToBackStack("EmployeeListFragment")
                .commit();
    }

}