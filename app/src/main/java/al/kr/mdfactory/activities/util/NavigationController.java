package al.kr.mdfactory.activities.util;

import android.view.View;

public interface NavigationController {
    void disableNavigationButton();
    void useNavigationButton();
    void useBackButton(View.OnClickListener l);
}
