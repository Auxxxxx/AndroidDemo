package al.kr.mdfactory.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;

import al.kr.mdfactory.R;
import al.kr.mdfactory.fragments.util.ViewPagerAdapter;

public class AdminFragment extends Fragment {
    private final static int ITEM_EMPLOYEES = R.id.item_employees_list;
    private final static int ITEM_OPERATION_REPORT = R.id.item_operations_report;
    private final static int ITEM_TIME_REPORT = R.id.item_time_report;
    private ViewPager2 viewPager2;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_admin, container, false);
        initViews();
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        initToolbar();
        initViewPager();
        initListeners();
        return rootView;
    }

    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    private void initViews() {
        toolbar = requireActivity().findViewById(R.id.toolbar_admin);
        drawerLayout = requireActivity().findViewById(R.id.drawer_admin);
        navigationView = requireActivity().findViewById(R.id.NV_admin);
        viewPager2 = rootView.findViewById(R.id.viewpager2);
    }
    private void initToolbar() {
        toolbar.setTitle(null);
        toolbar.setNavigationIcon(R.drawable.ic_navigation24);
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
    }
    private void initViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(EmployeeSettingsFragment.newInstance());
        adapter.addFragment(OperationReportFragment.newInstance());
        adapter.addFragment(TimeReportFragment.newInstance());
        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(false);
        viewPager2.setCurrentItem(0, false);
    }

    private void initListeners() {
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    private boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ITEM_EMPLOYEES:
                viewPager2.setCurrentItem(0, true);
                break;
            case ITEM_OPERATION_REPORT:
                viewPager2.setCurrentItem(1, true);
                break;
            case ITEM_TIME_REPORT:
                viewPager2.setCurrentItem(2, true);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}