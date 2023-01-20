package al.kr.mdfactory.fragments.util;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.lifecycle.MutableLiveData;

public class MyTextWatcher implements TextWatcher {
    private final MutableLiveData<String> data;

    private MyTextWatcher(MutableLiveData<String> data) {
        this.data = data;
    }

    public static MyTextWatcher of(MutableLiveData<String> data) {
        return new MyTextWatcher(data);
    }

    public void afterTextChanged(Editable e) {
        if (String.valueOf(data.getValue()).equals(e.toString())) return;
        data.setValue(e.toString());
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
}
