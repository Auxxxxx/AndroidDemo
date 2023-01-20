package al.kr.mdfactory.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.models.Employee;
import al.kr.mdfactory.viewModels.EmployeeListViewModel;
import al.kr.mdfactory.viewModels.EmployeeSettingsViewModel;

public class EmployeeListFragment extends Fragment {
    private static final String TAG = "%EmployeeListFragment%";
    private EmployeeSettingsViewModel employeeSettingsViewModel;
    private EmployeeListViewModel mViewModel;
    private RecyclerView employeeListRV;
    private Button addEmployeeBTN;
    private View rootView;
    private EmployeeListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeSettingsViewModel = new ViewModelProvider(requireActivity()).get(EmployeeSettingsViewModel.class);
        mViewModel = new ViewModelProvider(this).get(EmployeeListViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_employee_list, container, false);
        initViews();
        initEmployeeListRV(rootView.getContext());
        observe();
        mViewModel.requestEmployeeList();
        initListeners();
        return rootView;
    }

    public static EmployeeListFragment newInstance() {
        return new EmployeeListFragment();
    }

    private void initViews() {
        employeeListRV = rootView.findViewById(R.id.RV_employee_list);
        addEmployeeBTN = rootView.findViewById(R.id.BTN_employee_add);
    }

    private void initEmployeeListRV(Context context) {
        adapter = new EmployeeListAdapter(new ArrayList<>());
        employeeListRV.setAdapter(adapter);
        employeeListRV.setLayoutManager(new LinearLayoutManager(context));
    }

    private void observe() {
        mViewModel.getEmployeeList().observe(getViewLifecycleOwner(),
                this::onReceivedEmployeeList);
        mViewModel.getToastMessage().observe(getViewLifecycleOwner(),
                ((ToastShower)requireActivity())::showToast);
    }

    private void initListeners() {
        addEmployeeBTN.setOnClickListener(v -> toEmployeeUpdateFragment(null));
    }

    private void toEmployeeUpdateFragment(@Nullable Employee employeeToEdit) {
        employeeSettingsViewModel.getEmployeeToEdit().setValue(employeeToEdit);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.frame_employee_settings, EmployeeUpdateFragment.newInstance())
                .addToBackStack("EmployeeUpdateFragment")
                .commit();
    }

    private void onReceivedEmployeeList(List<Employee> employeeList) {
        assert employeeList != null : TAG + "employee_list_null";
        Collections.sort(employeeList);
        adapter.setList(employeeList);
    }

    class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> {
        private final List<Employee> list;

        public EmployeeListAdapter(List<Employee> list) {
            this.list = list;
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setList(List<Employee> newList) {
            list.clear();
            list.addAll(newList);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(final int position) {
            return R.layout.item_card_employee;
        }

        @NonNull
        @Override
        public EmployeeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final EmployeeListAdapter.ViewHolder holder, int position) {
            Employee employee = list.get(position);
            holder.employeeName.setText(employee.getName());
            holder.editBTN.setOnClickListener(v -> toEmployeeUpdateFragment(employee));
            holder.deleteBTN.setOnClickListener(v -> mViewModel.deleteEmployee(employee));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView employeeName;
            private final ImageButton editBTN;
            private final ImageButton deleteBTN;

            ViewHolder(View view) {
                super(view);
                employeeName = view.findViewById(R.id.TV_name);
                editBTN = view.findViewById(R.id.BTN_edit);
                deleteBTN = view.findViewById(R.id.BTN_delete);
            }
        }
    }
}