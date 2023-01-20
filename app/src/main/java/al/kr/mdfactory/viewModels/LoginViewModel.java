package al.kr.mdfactory.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Optional;

import al.kr.mdfactory.activities.constant.LoginResult;
import al.kr.mdfactory.connection.ConnectionManager;
import al.kr.mdfactory.viewModels.util.NotMainThread;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class LoginViewModel extends ViewModel implements ConnectionManager {
    private static final String TAG = "%LoginViewModel%";
    private final MutableLiveData<String> login = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<String> successfulLogin = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public void performLogIn() {
        if (checkLoginDataForNull()) return;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("login", login.getValue());
            jsonObject.put("password", password.getValue());
            jsonObject.put("zoneId", ZoneId.systemDefault());
            Log.i(TAG, login.getValue()+ ":" + password.getValue());
            Request request = buildPost("employee/attempt_log_in",
                    jsonObject.toString());

           runAsync(() -> sendRequest(request, this::onLoginPerformed));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onLoginPerformed(int code, ResponseBody responseBody) {
        if (code != 200) {
            toastMessage.postValue("Ошибка. Обратитесь к руководителю");
            return;
        }
        try {
            JSONObject responseJson = new JSONObject(responseBody.string());
            int loginResultCode = responseJson.getInt("loginResultCode");
            Optional<LoginResult> result = LoginResult.of(loginResultCode);
            if (!result.isPresent()) {
                toastMessage.postValue("Ошибка. Обратитесь к руководителю");
                return;
            }

            switch (result.get()) {
                case SHIFT_FINISHED:
                    toastMessage.postValue("Сегодняшняя смена завершена");
                    break;
                case NO_EMPLOYEE:
                    toastMessage.postValue("Неверный логин");
                    break;
                case WRONG_PASSWORD:
                    toastMessage.postValue("Неверный пароль");
                    break;
                default:
                    successfulLogin.postValue(responseJson.getString("employeeLogin"));
                    toastMessage.postValue("Добро пожаловать");
                    break;
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

    }

    public boolean checkLoginDataForNull() {
        if (login.getValue() == null || login.getValue().isEmpty()) {
            toastMessage.setValue("Введите логин");
            return true;
        }
        if (password.getValue() == null || password.getValue().isEmpty()) {
            toastMessage.setValue("Введите пароль");
            return true;
        }
        return false;
    }

    @NotMainThread
    public void onMessage(String message) {
        toastMessage.postValue(message);
    }

    public MutableLiveData<String> getLogin() {
        return login;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getSuccessfulLogin() {
        return successfulLogin;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }
}