package al.kr.mdfactory.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalTime;
import java.time.ZoneId;

import al.kr.mdfactory.connection.ConnectionManager;
import al.kr.mdfactory.viewModels.util.NotMainThread;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class StartShiftViewModel extends ViewModel implements ConnectionManager {
    private static final String TAG = "%StartShiftViewModel%";
    private final MutableLiveData<Boolean> onStartShiftRecorded = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isShiftStarted = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public void requestIsShiftStarted(String employeeLogin) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("employeeLogin", employeeLogin);
            jsonObject.put("zoneId", ZoneId.systemDefault());

            Request request = buildPost("time_recorder/is_shift_started", jsonObject.toString());
            runAsync(() -> sendRequest(request, this::setShiftStarted));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void setShiftStarted(int code, ResponseBody responseBody) {
        if (code != 200) {
            Log.i(TAG, "setShiftStarted:failure");
            toastMessage.postValue("Ошибка");
            return;
        }
        try {
            boolean isShiftStarted = responseBody.string().equals("true");
            Log.i(TAG, "isShiftStarted:"+isShiftStarted);
            this.isShiftStarted.postValue(isShiftStarted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void recordStartShift(String employeeLogin) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("employeeLogin", employeeLogin);
            jsonObject.put("zoneId", ZoneId.systemDefault());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            jsonObject.put("startTime", objectMapper.writeValueAsString(LocalTime.now()));

            Request request = buildPost("time_recorder/start_shift", jsonObject.toString());
            runAsync(() -> sendRequest(request, this::onStartShiftRecorded));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onStartShiftRecorded(int code, ResponseBody responseBody) {
        boolean isStartShiftRecorded = code == 200;
        Log.i(TAG, "startShiftRecorded:"+ isStartShiftRecorded);
        this.onStartShiftRecorded.postValue(isStartShiftRecorded);
    }

    @Override
    @NotMainThread
    public void onMessage(String message) {
        toastMessage.postValue(message);
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<Boolean> getOnStartShiftRecorded() {
        return onStartShiftRecorded;
    }

    public MutableLiveData<Boolean> getIsShiftStarted() {
        return isShiftStarted;
    }
}