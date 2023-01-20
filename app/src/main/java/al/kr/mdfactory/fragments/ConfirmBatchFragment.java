package al.kr.mdfactory.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.activities.util.Vibrator;
import al.kr.mdfactory.fragments.util.MyTextWatcher;
import al.kr.mdfactory.viewModels.ConfirmBatchViewModel;
import al.kr.mdfactory.viewModels.WorkViewModel;

public class ConfirmBatchFragment extends Fragment {
    private static final String TAG = "%ConfirmBatchFragment%";
    private WorkViewModel workViewModel;
    private ConfirmBatchViewModel mViewModel;
    private Button confirmBTN;
    private Toolbar toolbar;
    private EditText itineraryET;
    private ImageView scanBTN;
    private View rootView;
    private ActivityResultLauncher<ScanOptions> scanLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workViewModel = new ViewModelProvider(requireActivity()).get(WorkViewModel.class);
        mViewModel = new ViewModelProvider(this).get(ConfirmBatchViewModel.class);
        registerScanLauncher();
    }

    private void registerScanLauncher() {
        Log.i(TAG, "start_scanning");
        ActivityResultCallback<ScanIntentResult> callback = this::scanResultReceived;
        scanLauncher = registerForActivityResult(new ScanContract(), callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_confirm_batch, container, false);
        initViews();
        observe();
        setupBackButton();
        initListeners();
        launchScan();
        return rootView;
    }

    public static ConfirmBatchFragment newInstance() {
        return new ConfirmBatchFragment();
    }

    private void initViews(){
        toolbar = requireActivity().findViewById(R.id.toolbar_lobby);
        confirmBTN = rootView.findViewById(R.id.BTN_confirm);
        scanBTN = rootView.findViewById(R.id.BTN_scan);
        itineraryET = rootView.findViewById(R.id.ET_itinerary);
    }
    public void observe() {
        mViewModel.getItinerary().observe(getViewLifecycleOwner(), s -> observeET(s, itineraryET));

        mViewModel.getToastMessage().observe(getViewLifecycleOwner(),
                ((ToastShower)requireActivity())::showToast);
    }
    private void observeET(String s, EditText editText) {
        if (!editText.getText().toString().equals(s)) {
            editText.setText(s);
        }
    }

    private void setupBackButton() {
        toolbar.setNavigationOnClickListener(v -> onNavigationClicked());
    }

    private void onNavigationClicked() {
        ((Vibrator)requireActivity()).vibrateShort();
        getParentFragmentManager().popBackStack();
    }

    private void initListeners() {
        itineraryET.addTextChangedListener(MyTextWatcher.of(mViewModel.getItinerary()));
        confirmBTN.setOnClickListener(v -> onConfirmClicked());
        scanBTN.setOnClickListener(v -> launchScan());
    }

    private void onConfirmClicked() {
        confirmBatch(mViewModel.getItinerary().getValue());
    }

    private void launchScan() {
        ScanOptions options = new ScanOptions()
                .setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
                .setPrompt("Сканируем...")
                .setBeepEnabled(false)
                .setOrientationLocked(false);
        scanLauncher.launch(options);
    }

    private void scanResultReceived(ScanIntentResult result) {
        Log.i(TAG, "scan_result_received");
        if(result.getContents() == null) {
            ((ToastShower)requireActivity()).showToast("Ошибка при сканировании");
        } else {
            Log.i(TAG, ""+result.getContents());
            mViewModel.getItinerary().postValue(result.getContents());
            confirmBatch(result.getContents());
        }
    }

    private void confirmBatch(String itinerary) {
        if (mViewModel.isItineraryValid(itinerary)) {
            workViewModel.getItinerary().setValue(itinerary);
            ((Vibrator)requireActivity()).vibrateShort();
            toActiveBatchFragment();
        }
    }

    private void toActiveBatchFragment() {
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_work, ActiveBatchFragment.newInstance())
                .commit();
    }
}