package fandradetecinfo.com.meupeso.Views;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import fandradetecinfo.com.meupeso.MainActivity;

public class _BaseFragment extends Fragment {

    protected Context ctx;

    //protected ListView minhaLista;

    protected String TAG;

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity)getActivity()).getViewPager().setCurrentItem(MainActivity.posFragment);
    }
}
