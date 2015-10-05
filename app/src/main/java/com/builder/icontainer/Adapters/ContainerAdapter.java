package com.builder.icontainer.Adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.builder.icontainer.R;
import com.builder.icontainer.models.Container;
import com.builder.icontainer.utils.ApplicationClass;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shabaz on 29-Aug-15.
 */
public class ContainerAdapter extends BaseAdapter
{
    final String tag = ContainerAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private List<Container> contianer_list;
    HorizontalBarChart mChart;
    public ContainerAdapter(List<Container> contianer_list)
    {
        inflater = (LayoutInflater) ApplicationClass.static_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contianer_list = contianer_list;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount()
    {
        return contianer_list.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position)
    {
        return null;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.container_list_layout, null);
        Container container_list_model = contianer_list.get(position);
        Log.d(tag,"position 1 : "+container_list_model.toString());
        ImageView image = (ImageView) convertView.findViewById(R.id.container_image);
        TextView name = (TextView) convertView.findViewById(R.id.container_name);
        TextView refill_date = (TextView) convertView.findViewById(R.id.container_refill_date);
        name.setText(container_list_model.getItem());
        List<Long> dates = container_list_model.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd,MMM,yy");

        refill_date.setText("Refilled On : "+sdf.format(new Date(dates.get(dates.size()-1)*1000)));
        image.setImageResource(getImageResource(container_list_model.getItem()));
         mChart = (HorizontalBarChart) convertView.findViewById(R.id.container_list_chart);
        mChart.setDrawGridBackground(false);
        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setDrawLabels(false);
        xl.setSpaceBetweenLabels(1000);
        LimitLine maxLimitLine = new LimitLine(5.0f,"Max");
        LimitLine minLimitLine = new LimitLine(0.200f,"Min");

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.addLimitLine(maxLimitLine);
        yl.addLimitLine(minLimitLine);
        yl.setAxisMaxValue(5.5f);
        yl.setDrawLabels(false);
        addData(container_list_model.getItemWeight().get(container_list_model.getItemWeight().size()-1));
        mChart.setTouchEnabled(false);
        mChart.setDrawBarShadow(true);
        mChart.setBackground(new ColorDrawable(ApplicationClass.static_context.getResources().getColor(R.color.transparent)));
        mChart.animateY(1500);
        return convertView;
    }

    private int getImageResource(String item)
    {
        int ret = R.drawable.wheat;
        if (item.equals("Rice"))
            ret = R.drawable.rice;
        else if (item.equals("Salt"))
            ret = R.drawable.salt;
        else if (item.equals("Sugar"))
            ret = R.drawable.sugar;
        else if (item.equals("Urad Dal"))
            ret = R.drawable.uraddal;
        else if (item.equals("Dal"))
            ret = R.drawable.toordal;

            return ret;
    }

    void addData(float weight)
    {

      ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("");
        //xVals.add("");

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        yVals1.add(new BarEntry(weight, 0));
        //yVals1.add(new BarEntry(5.0f,1));
        BarDataSet set1 = new BarDataSet(yVals1, "Quantity Left (KGs)");
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(xVals, dataSets);

        data.setValueTextSize(10f);
        mChart.setDescription("");
        mChart.setData(data);
    }
}
