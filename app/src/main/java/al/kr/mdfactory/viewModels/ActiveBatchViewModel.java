package al.kr.mdfactory.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import al.kr.mdfactory.models.Batch;
import al.kr.mdfactory.models.Operation;
import al.kr.mdfactory.connection.ConnectionManager;
import al.kr.mdfactory.viewModels.util.NotMainThread;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class ActiveBatchViewModel extends ViewModel implements ConnectionManager {
    private static final String TAG = "%ActiveBatchViewModel%";
    private final MutableLiveData<List<Operation>> operations = new MutableLiveData<>();
    private final MutableLiveData<LocalTime> startTime = new MutableLiveData<>();
    private final MutableLiveData<LocalTime> finishTime = new MutableLiveData<>();
    private final MutableLiveData<Duration> batchDuration = new MutableLiveData<>(Duration.ZERO);
    private final MutableLiveData<Duration> breakDuration = new MutableLiveData<>(Duration.ZERO);
    private final MutableLiveData<Operation> selectedOperation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> onBatchAdded = new MutableLiveData<>(false);
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public void requestOperations(String employeeLogin, String itinerary) {
        try {
            Long specificationId = Long.parseLong(itinerary.substring(1, 4));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("employeeLogin", employeeLogin);
            jsonObject.put("specificationId", specificationId);
            Log.i(TAG, "sending_request_for_operation_options");

            Request request = buildPost("operation/performed_operation_options",
                    jsonObject.toString());
            runAsync(() -> sendRequest(request, this::onOperationsReceived));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onOperationsReceived(int code, ResponseBody responseBody) {
        if (code != 200) {
            Log.i(TAG, "setShiftStarted:failure");
            toastMessage.postValue("Ошибка");
            return;
        }
        try {
            operations.postValue(parseOperations(responseBody.string()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Operation> parseOperations(String json) {
        JSONArray responseJson;
        List<Operation> operations = null;
        try {
            responseJson = new JSONArray(json);
            Type type = new TypeToken<List<Operation>>(){}.getType();
            operations = new Gson().fromJson(responseJson.toString(), type);
            Log.i(TAG, "list_built_successfully");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "error_on_response");
        }
        return operations;
    }

    public void recordBatch(String employeeLogin) {
        if (checkSelectedOperationForNull()) return;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("employeeLogin", employeeLogin);
            jsonObject.put("zoneId", ZoneId.systemDefault());
            Batch batch = new Batch();
            batch.setStartTime(startTime.getValue());
            batch.setFinishTime(finishTime.getValue());
            batch.setDuration(batchDuration.getValue());
            //Log.i(TAG, ""+batchDuration.getValue().toMillis());
            batch.setBreakDuration(breakDuration.getValue());
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            jsonObject.put("batch", objectMapper.writeValueAsString(batch));

            Request request = buildPost("time_recorder/add_batch", jsonObject.toString());
            runAsync(() -> sendRequest(request, this::onBatchAdded));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @NotMainThread
    private void onBatchAdded(int code, ResponseBody responseBody) {
        boolean isBatchAdded = code == 200;
        this.onBatchAdded.postValue(isBatchAdded);
    }

    @NotMainThread
    public void onMessage(String message) {
        toastMessage.postValue(message);
    }

    public boolean checkSelectedOperationForNull() {
        if (selectedOperation.getValue() == null) {
            toastMessage.setValue("Выберите выполненную операцию");
            return true;
        }
        return false;
    }

    public void saveBreak(Long millis) {
        Duration previousValue = breakDuration.getValue();
        assert previousValue != null;
        Duration newValue = previousValue.plusMillis(millis);
        breakDuration.setValue(newValue);
    }

    public MutableLiveData<List<Operation>> getOperations() {
        return operations;
    }


    public MutableLiveData<LocalTime> getStartTime() {
        return startTime;
    }

    public MutableLiveData<LocalTime> getFinishTime() {
        return finishTime;
    }

    public MutableLiveData<Duration> getBatchDuration() {
        return batchDuration;
    }

    public MutableLiveData<Duration> getBreakDuration() {
        return breakDuration;
    }

    public MutableLiveData<Operation> getSelectedOperation() {
        return selectedOperation;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<Boolean> getOnBatchAdded() {
        return onBatchAdded;
    }
}