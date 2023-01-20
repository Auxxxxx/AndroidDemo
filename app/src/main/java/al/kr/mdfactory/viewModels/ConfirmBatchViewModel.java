package al.kr.mdfactory.viewModels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ConfirmBatchViewModel extends ViewModel {
    private static final String TAG = "%ConfirmBatchViewModel%";
    private final MutableLiveData<String> itinerary = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();

    public boolean isItineraryValid(String itinerary) {
        Log.i(TAG, "itinerary" + itinerary);
        if (itinerary == null) {
            toastMessage.setValue("Ошибка");
            return false;
        }
        try {
            Double.parseDouble(itinerary);
            if (itinerary.length() == 20) {
                return true;
            } else {
                String message = "Требуемая длина кода: 20\n" +
                        "Длина введённого кода: " + itinerary.length();
                toastMessage.setValue(message);
                return false;
            }
        } catch (NumberFormatException e) {
            toastMessage.setValue("Номер путевого листа может содержать только цифры 0-9");
            return false;
        }
    }

    public MutableLiveData<String> getItinerary() {
        return itinerary;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }
}