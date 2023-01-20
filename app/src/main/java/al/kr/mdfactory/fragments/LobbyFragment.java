package al.kr.mdfactory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;

import al.kr.mdfactory.R;
import al.kr.mdfactory.activities.MainActivity;
import al.kr.mdfactory.activities.util.ToastShower;
import al.kr.mdfactory.fragments.util.ViewPagerAdapter;
import al.kr.mdfactory.viewModels.LobbyViewModel;

public class LobbyFragment extends Fragment {
    private final static int ITEM_WORK = R.id.item_work;
    private final static int ITEM_HISTORY = R.id.item_history;
    private final static int ITEM_FINISH = R.id.item_finish;
    private LobbyViewModel mViewModel;
    private ViewPager2 viewPager2;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(LobbyViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lobby, container, false);
        initViews();
        observe();
        initNavigation();
        initToolBar();
        initViewPager();
        return rootView;
    }

    public static LobbyFragment newInstance() {
        return new LobbyFragment();
    }

    private void initViews() {
        toolbar = requireActivity().findViewById(R.id.toolbar_lobby);
        drawerLayout = requireActivity().findViewById(R.id.drawer_lobby);
        navigationView = requireActivity().findViewById(R.id.NV_lobby);
        viewPager2 = rootView.findViewById(R.id.viewpager2);
    }

    private void observe() {
        mViewModel.getToastMessage().observe(getViewLifecycleOwner(),
                ((ToastShower)requireActivity())::showToast);
        mViewModel.getOnShiftFinished().observe(getViewLifecycleOwner(),
                this::onShiftFinished);
    }

    private void onShiftFinished(Boolean isShiftFinished) {
        if (isShiftFinished) {
            toMainActivity();
        } else {

        }
    }

    private void initNavigation() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case ITEM_WORK:
                    viewPager2.setCurrentItem(0, true);
                    break;
                case ITEM_HISTORY:
                    viewPager2.setCurrentItem(1, true);
                    break;
                case ITEM_FINISH:
                    navigationView.post(() -> navigationView.setCheckedItem(ITEM_WORK));
                    viewPager2.setCurrentItem(0, false);
                    mViewModel.recordFinishShift();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void initToolBar() {
        toolbar.setTitle(null);
    }

    private void initViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(WorkFragment.newInstance());
        adapter.addFragment(HistoryFragment.newInstance());
        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setCurrentItem(0, false);
    }

    private void toMainActivity() {
        requireActivity().finish();
    }
}

