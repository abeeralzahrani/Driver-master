package wassilni.pl.driver.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import objects.schedule;
import wassilni.pl.driver.R;

/**
 * Created by hp on 13/12/16.
 */

public class custmerAdapter extends BaseAdapter {

    Context context;
    List<schedule> scheduleList;

    public custmerAdapter(List<schedule> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    public custmerAdapter(FragmentActivity activity, List<schedule> schedullist) {
        this.scheduleList=schedullist;
        this.context=activity;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        LayoutInflater mInflater =(LayoutInflater)context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.worktime,null);
            holder = new ViewHolder();

            holder.time = (TextView)convertView
                    .findViewById(R.id.TimeTV);
            holder.sid=(TextView)convertView
                    .findViewById(R.id.S_ID);
            schedule schedule = scheduleList.get(position);
            holder.time.setText(schedule.getTime());
            holder.sid.setText(schedule.getS_ID());

        }


        return null;
    }
    private  class ViewHolder{
        TextView sid;
        TextView time;

    }
}
