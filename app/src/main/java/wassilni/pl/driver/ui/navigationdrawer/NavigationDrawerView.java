package wassilni.pl.driver.ui.navigationdrawer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import wassilni.pl.driver.R;
import wassilni.pl.driver.data.model.NavigationDrawerItem;
import wassilni.pl.driver.ui.misc.BetterViewAnimator;


public class NavigationDrawerView extends BetterViewAnimator {

    @InjectView(R.id.leftDrawerListView)
    ListView leftDrawerListView;

    private final NavigationDrawerAdapter adapter;


    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new NavigationDrawerAdapter(context);
    }

    public void replaceWith(List<NavigationDrawerItem> items) {
        adapter.replaceWith(items);
        setDisplayedChildId(R.id.leftDrawerListView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        leftDrawerListView.setAdapter(adapter);
    }

    public NavigationDrawerAdapter getAdapter() {
        return adapter;
    }
}
