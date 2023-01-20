package al.kr.mdfactory.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkViewModel extends ViewModel {
    private final MutableLiveData<String> itinerary = new MutableLiveData<>();

    public MutableLiveData<String> getItinerary() {
        return itinerary;
    }
}
