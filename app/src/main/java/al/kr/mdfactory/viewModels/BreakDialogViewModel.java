package al.kr.mdfactory.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BreakDialogViewModel extends ViewModel {
    private final MutableLiveData<Long> breakTime = new MutableLiveData<>();

    public MutableLiveData<Long> getBreakTime() {
        return breakTime;
    }
}
