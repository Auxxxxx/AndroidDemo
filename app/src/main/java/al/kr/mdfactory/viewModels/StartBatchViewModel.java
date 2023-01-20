package al.kr.mdfactory.viewModels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;

import al.kr.mdfactory.connection.ConnectionManager;
import al.kr.mdfactory.viewModels.util.NotMainThread;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class StartBatchViewModel extends ViewModel implements ConnectionManager {
    private static final String TAG = "%StartBatchViewModel%";
    private final MutableLiveData<Duration> breakDuration = new MutableLiveData<>(Duration.ZERO);
    private final MutableLiveData<Boolean> onBreakRecorded = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public void recordBreak(String employeeLogin) {
        Log.i(TAG, "recording_break");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("employeeLogin", employeeLogin);
            jsonObject.put("zoneId", ZoneId.systemDefault());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            jsonObject.put("breakDuration", objectMapper.writeValueAsString(breakDuration.getValue()));

            Request request = buildPost("time_recorder/record_break", jsonObject.toString());
            runAsync(() -> sendRequest(request, this::onBreakRecorded));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onBreakRecorded(int code, ResponseBody responseBody) {
        Log.i(TAG, "break_recorded:"+code);
        boolean isBreakRecorded = code == 200;
        this.onBreakRecorded.postValue(isBreakRecorded);
    }

    @Override
    @NotMainThread
    public void onMessage(String message) {
        toastMessage.postValue(message);
    }

    public void saveBreak(Long millis) {
        Duration previousValue = breakDuration.getValue();
        assert previousValue != null;
        Duration newValue = previousValue.plusMillis(millis);
        breakDuration.setValue(newValue);
    }

    public MutableLiveData<Duration> getBreakDuration() {
        return breakDuration;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<Boolean> getOnBreakRecorded() {
        return onBreakRecorded;
    }
}
