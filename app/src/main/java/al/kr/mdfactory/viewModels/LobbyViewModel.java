package al.kr.mdfactory.viewModels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;

import al.kr.mdfactory.connection.ConnectionManager;
import al.kr.mdfactory.viewModels.util.NotMainThread;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class LobbyViewModel extends ViewModel implements ConnectionManager {
    private static final String TAG = "%LobbyViewModel%";
    private final MutableLiveData<String> employeeLogin = new MutableLiveData<>();
    private final MutableLiveData<Boolean> onShiftFinished = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public void recordFinishShift() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("employeeLogin", employeeLogin.getValue());
            jsonObject.put("zoneId", ZoneId.systemDefault());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            jsonObject.put("finishTime", objectMapper.writeValueAsString(LocalTime.now()));

            Request request = buildPost("time_recorder/finish_shift", jsonObject.toString());
            runAsync(() -> sendRequest(request, this::onShiftFinished));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onShiftFinished(int code, ResponseBody responseBody) {
        boolean isShiftFinished = code == 200;
        if (!isShiftFinished) toastMessage.postValue("Смена не была начата");
        Log.i(TAG, "isShiftFinished:"+isShiftFinished);
        this.onShiftFinished.postValue(isShiftFinished);
    }

    @Override
    @NotMainThread
    public void onMessage(String message) {
        toastMessage.postValue(message);
    }

    public MutableLiveData<String> getEmployeeLogin() {
        return employeeLogin;
    }

    public MutableLiveData<Boolean> getOnShiftFinished() {
        return onShiftFinished;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }
}
