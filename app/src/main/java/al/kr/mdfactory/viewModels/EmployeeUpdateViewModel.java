package al.kr.mdfactory.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Predicate;

import al.kr.mdfactory.models.Employee;
import al.kr.mdfactory.models.OperationGroup;
import al.kr.mdfactory.connection.ConnectionManager;
import al.kr.mdfactory.viewModels.util.NotMainThread;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class EmployeeUpdateViewModel extends ViewModel implements ConnectionManager {
    private static final String TAG = "%EmployeeUpdateView%";
    private final MutableLiveData<List<OperationGroup>> operationGroups = new MutableLiveData<>();
    private final MutableLiveData<String> login = new MutableLiveData<>();
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<List<OperationGroup>> selectedOperationGroups = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private boolean addingNewEmployee = false;

    public void requestOperationGroups() {
        try {
            Request request = buildGet("operation_group/getAll");
            runAsync(() -> sendRequest(request, this::onOperationGroupsReceived));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onOperationGroupsReceived(int code, ResponseBody responseBody) {
        if (code != 200) {
            Log.i(TAG, "setShiftStarted:failure");
            toastMessage.postValue("Ошибка");
            return;
        }
        try {
            String json = responseBody.string();
            Type type = new TypeToken<List<OperationGroup>>() {}.getType();
            List<OperationGroup> responseOperationGroupList = new Gson().fromJson(json, type);
            operationGroups.postValue(responseOperationGroupList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateEmployee(boolean adding) {
        if (isEmployeeDataNull()) return;
        this.addingNewEmployee = adding;
        try {
            assert login.getValue() != null : "employee_login_is_null";
            Employee employee = new Employee(login.getValue());
            employee.setName(name.getValue());
            employee.setPassword(password.getValue());
            employee.setOperationGroups(selectedOperationGroups.getValue());
            String json = new Gson().toJson(employee);
            Request request = buildPost("employee/update", json);

            runAsync(() -> sendRequest(request, this::onEmployeeUpdated));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onEmployeeUpdated(int code, ResponseBody responseBody) {
        if (code != 200) {
            Log.i(TAG, "setShiftStarted:failure");
            toastMessage.postValue("Ошибка");
            return;
        }
        Log.i(TAG, "worker_updated");
        if (addingNewEmployee) {
            toastMessage.postValue("Работник добавлен");
        } else {
            toastMessage.postValue("Данные изменены");
        }
    }

    public boolean isEmployeeDataNull() {
        Predicate<String> p = (value) -> value == null || value.isEmpty();

        if (p.test(login.getValue())) {
            toastMessage.setValue("Введите логин");
            return true;
        }
        if (p.test(password.getValue())) {
            toastMessage.setValue("Введите пароль");
            return true;
        }
        if (p.test(name.getValue())) {
            toastMessage.setValue("Введите ФИО");
            return true;
        }
        if (selectedOperationGroups.getValue() == null || selectedOperationGroups.getValue().isEmpty()) {
            toastMessage.setValue("Выберите хотя бы одну группу операций");
            return true;
        }
        return false;
    }

    @NotMainThread
    public void onMessage(String message) {
        toastMessage.postValue(message);
    }

    public MutableLiveData<List<OperationGroup>> getOperationGroups() {
        return operationGroups;
    }

    public MutableLiveData<String> getLogin() {
        return login;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<List<OperationGroup>> getSelectedOperationGroups() {
        return selectedOperationGroups;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }
}