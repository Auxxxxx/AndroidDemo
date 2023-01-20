package al.kr.mdfactory.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import al.kr.mdfactory.models.Employee;

public class EmployeeSettingsViewModel extends ViewModel {
    private static final String TAG = "%EmployeeSettingsViewModel%";
    private final MutableLiveData<Employee> employeeToEdit = new MutableLiveData<>();

    public MutableLiveData<Employee> getEmployeeToEdit() {
        return employeeToEdit;
    }
}