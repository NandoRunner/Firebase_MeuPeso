package fandradetecinfo.com.meupeso;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fernando on 30/08/2017.
 */

public class _BaseAdapter extends BaseAdapter {

    protected ArrayList<TextView> lstTV;
    protected Activity activity;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    protected void findAllTextView(ViewGroup viewGroup) {

        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup)
                findAllTextView((ViewGroup) view);
            else if (view instanceof TextView) {
                TextView tv = (TextView) view;
                lstTV.add(tv);
            }
        }

    }

    protected void tratarCores(View vw, int position)
    {
        lstTV = new ArrayList<>();

        findAllTextView((ViewGroup)vw);

        int color = position % 2 != 0 ? R.color.colorWhite : R.color.colorBlack;

        for( TextView aux : lstTV )
        {
            aux.setTextColor(activity.getResources().getColor(color));
        }

        vw.setBackgroundColor(activity.getResources().getColor((position % 2 != 0 ?
                R.color.colorPrimary : R.color.actionbar_fg_color)));
    }
}
