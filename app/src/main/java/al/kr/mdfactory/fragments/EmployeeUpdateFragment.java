package al.kr.mdfactory.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.models.Employee;
import al.kr.mdfactory.models.OperationGroup;
import al.kr.mdfactory.fragments.util.MyTextWatcher;
import al.kr.mdfactory.viewModels.EmployeeSettingsViewModel;
import al.kr.mdfactory.viewModels.EmployeeUpdateViewModel;

public class EmployeeUpdateFragment extends Fragment {
    private static final String TAG = "%EmployeeUpdate%";
    private EmployeeSettingsViewModel employeeSettingsViewModel;
    private EmployeeUpdateViewModel mViewModel;
    private EditText loginET;
    private EditText passwordET;
    private EditText nameET;
    private Button saveEmployeeBTN;
    private RecyclerView operationGroupsRV;
    private View rootView;
    private OperationGroupListAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeSettingsViewModel = new ViewModelProvider(requireActivity()).get(EmployeeSettingsViewModel.class);
        mViewModel = new ViewModelProvider(this).get(EmployeeUpdateViewModel.class);

        setOnBackPressed();
    }

    private void setOnBackPressed() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_employee_update, container, false);
        initViews();
        initOperationGroupListRV(rootView.getContext());
        initFields();
        observe();
        mViewModel.requestOperationGroups();
        initListeners();
        return rootView;
    }

    public static EmployeeUpdateFragment newInstance() {
        return new EmployeeUpdateFragment();
    }

    private void initViews() {
        loginET = rootView.findViewById(R.id.ET_login);
        passwordET = rootView.findViewById(R.id.ET_password);
        nameET = rootView.findViewById(R.id.ET_name);
        operationGroupsRV = rootView.findViewById(R.id.RV_operation_groups);
        saveEmployeeBTN = rootView.findViewById(R.id.BTN_save);
    }

    private void initOperationGroupListRV(Context context) {
        adapter = new OperationGroupListAdapter(new ArrayList<>());
        operationGroupsRV.setAdapter(adapter);
        operationGroupsRV.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initFields() {
        Employee employeeToEdit = employeeSettingsViewModel.getEmployeeToEdit().getValue();
        if (employeeToEdit != null) {
            mViewModel.getLogin().setValue(employeeToEdit.getLogin());
            mViewModel.getName().setValue(employeeToEdit.getName());
            mViewModel.getPassword().setValue(employeeToEdit.getPassword());
            mViewModel.getSelectedOperationGroups().setValue(employeeToEdit.getOperationGroups());
            loginET.setEnabled(false);
        } else {
            mViewModel.getSelectedOperationGroups().setValue(new ArrayList<>());
        }
    }

    private void observe() {
        mViewModel.getOperationGroups().observe(getViewLifecycleOwner(),
                this::onReceivedOperationGroups);
        mViewModel.getToastMessage().observe(getViewLifecycleOwner(),
                ((ToastShower)requireActivity())::showToast);
        mViewModel.getLogin().observe(getViewLifecycleOwner(), s -> observeET(s, loginET));
        mViewModel.getPassword().observe(getViewLifecycleOwner(), s -> observeET(s, passwordET));
        mViewModel.getName().observe(getViewLifecycleOwner(), s -> observeET(s, nameET));
    }

    private void observeET(String s, EditText editText) {
        if (!editText.getText().toString().equals(s)) {
            editText.setText(s);
        }
    }

    private void initListeners() {
        loginET.addTextChangedListener(MyTextWatcher.of(mViewModel.getLogin()));
        passwordET.addTextChangedListener(MyTextWatcher.of(mViewModel.getPassword()));
        nameET.addTextChangedListener(MyTextWatcher.of(mViewModel.getName()));

        saveEmployeeBTN.setOnClickListener(v -> onSaveEmployeeClicked());
    }

    private void onSaveEmployeeClicked() {
        boolean adding = employeeSettingsViewModel.getEmployeeToEdit().getValue() == null;
        mViewModel.updateEmployee(adding);
    }

    private void onReceivedOperationGroups(List<OperationGroup> operationGroupsList) {
        adapter.setList(operationGroupsList);
    }

    class OperationGroupListAdapter extends RecyclerView.Adapter<OperationGroupListAdapter.ViewHolder> {
        private final List<OperationGroup> list;

        public OperationGroupListAdapter(List<OperationGroup> list) {
            this.list = list;
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setList(List<OperationGroup> newList) {
            list.clear();
            list.addAll(newList);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(final int position) {
            return R.layout.item_operation_group;
        }

        @NonNull
        @Override
        public OperationGroupListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final OperationGroupListAdapter.ViewHolder holder, int position) {
            OperationGroup operationGroup = list.get(position);
            holder.operationGroupCB.setText(operationGroup.getName());
            holder.operationGroupCB.setChecked(isSelected(operationGroup));
            holder.operationGroupCB.setOnClickListener(
                    v -> updateSelected(holder.operationGroupCB.isChecked(), operationGroup));
        }

        private boolean isSelected(OperationGroup operationGroup) {
            List<OperationGroup> selected = mViewModel.getSelectedOperationGroups().getValue();
            if (selected != null) {
                return selected.contains(operationGroup);
            } else {
                Log.i(TAG, "selected_null");
                return false;
            }
        }

        private void updateSelected(boolean isChecked, OperationGroup operationGroup) {
            List<OperationGroup> selected = mViewModel.getSelectedOperationGroups().getValue();
            if (selected != null) {
                if (isChecked) {
                    selected.add(operationGroup);
                } else {
                    selected.remove(operationGroup);
                }
            } else {
                Log.i(TAG, "selected_null");
            }
            mViewModel.getSelectedOperationGroups().setValue(selected);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final CheckBox operationGroupCB;

            ViewHolder(View view) {
                super(view);
                operationGroupCB = view.findViewById(R.id.CB_test_operation_group);
            }
        }
    }
}