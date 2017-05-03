package wassilni.pl.driver.data;

import android.support.v4.app.Fragment;

import wassilni.pl.driver.ui.FragmentAbout;
import wassilni.pl.driver.ui.FragmentFifth;
import wassilni.pl.driver.ui.FragmentFour;
import wassilni.pl.driver.ui.FragmentOne;
import wassilni.pl.driver.ui.FragmentSixth;
import wassilni.pl.driver.ui.FragmentThree;
import wassilni.pl.driver.ui.FragmentTwo;


public enum Fragments {

    ONE(FragmentOne.class), TWO(FragmentTwo.class), THREE(FragmentThree.class), FOUR(FragmentFour.class),
    FiFth(FragmentFifth.class), Sixth(FragmentSixth.class),ABOUT(
            FragmentAbout.class);

    final Class<? extends Fragment> fragment;

    private Fragments(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }

    public String getFragment() {
        return fragment.getName();
    }
}
