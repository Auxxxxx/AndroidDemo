package al.kr.mdfactory.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import al.kr.mdfactory.models.Employee;
import al.kr.mdfactory.connection.ConnectionManager;
import al.kr.mdfactory.viewModels.util.NotMainThread;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class EmployeeListViewModel extends ViewModel implements ConnectionManager {
    private static final String TAG = "%LoginViewModel%";
    private final MutableLiveData<List<Employee>> employeeList = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public void requestEmployeeList() {
        try {
            Request request = buildGet("employee/getAll");
            runAsync(() -> sendRequest(request, this::onEmployeeListReceived));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onEmployeeListReceived(int code, ResponseBody responseBody) {
        if (code != 200) {
            Log.i(TAG, "setShiftStarted:failure");
            toastMessage.postValue("Ошибка");
            return;
        }
        try {
            String json = responseBody.string();
            Type type = new TypeToken<List<Employee>>() {}.getType();
            List<Employee> responseEmployeeList = new Gson().fromJson(json, type);
            employeeList.postValue(responseEmployeeList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(Employee employeeToDelete) {
        try {
            Request request = buildDelete("employee/" + employeeToDelete.getLogin());
            runAsync(() -> sendRequest(request, this::onEmployeeDeleted));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onEmployeeDeleted(int code, ResponseBody responseBody) {
        if (code == 200) {
            requestEmployeeList();
        }
    }

    @NotMainThread
    public void onMessage(String message) {
        toastMessage.postValue(message);
    }

    public MutableLiveData<List<Employee>> getEmployeeList() {
        return employeeList;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }
}