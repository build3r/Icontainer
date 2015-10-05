package com.builder.icontainer;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.builder.icontainer.models.Container;
import com.builder.icontainer.utils.ApplicationClass;
import com.builder.icontainer.utils.ContantsStatics;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContainerDetails extends AppCompatActivity
{
    final String tag = Container.class.getSimpleName();
    final String nutrition_endpoint = "getNutrition/";
    final String refresh_endpoint = "getContainer/";
    private SwipeRefreshLayout swipeContainer;
    Container container_data;
    String json_str;
    HorizontalBarChart mHorizontalBarChart;
    LineChart mLineChart;
    PieChart mPieChart;
    OkHttpClient client;
    float weight = 0f;
    SimpleDateFormat sdf;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_details);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        TextView container_name = (TextView) findViewById(R.id.container_detail_name);
        TextView container_refill_date = (TextView) findViewById(R.id.container_detail_refill);
        TextView container_expiry_date = (TextView) findViewById(R.id.container_detail_expiry);
        ImageView conatainer_image = (ImageView) findViewById(R.id.container_detail_image);

        client = new OkHttpClient();
        Intent mIntent = this.getIntent();

        String json_String = mIntent.getStringExtra("CONTAINER_DATA");
        container_data = deserialize(json_String);
        // Setup refresh listener which triggers new data loading
        try
        {
            run_ask_quest(ContantsStatics.BASEURL+nutrition_endpoint+container_data.getContainerId(),1);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)

                // once the network request has completed successfully.

                try
                {
                    run_ask_quest(ContantsStatics.BASEURL+refresh_endpoint+container_data.getContainerId(),2);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

        });

        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

        container_name.setText(container_data.getItem());
        mHorizontalBarChart = (HorizontalBarChart) findViewById(R.id.container_detail_chart);
        mLineChart = (LineChart) findViewById(R.id.container_detail_line_chart);
        mPieChart = (PieChart) findViewById(R.id.container_detail_pie_chart);
        sdf = new SimpleDateFormat("dd,MMM");
        conatainer_image.setImageResource(getImageResource(container_data.getItem()));
        int size = container_data.getDate().size();
        container_refill_date.setText("Refilled on : "+ sdf.format(new Date(container_data.getDate().get(size-1)*1000)));
        container_expiry_date.setText("Expires on : "+ sdf.format((new Date(container_data.getDate().get(size-1)*1000+15778476*1000
        ))));

        create_horizontal_chart();
        setmHorizontalBarChartData(container_data.getItemWeight().get(container_data.getItemWeight().size()-1));
        createLineGraph();
        setLineChartData();
        createPieChart();

        /*TextView mTextView = (TextView) findViewById(R.id.text_shabaz);
        mTextView.setText(json_String);
        mTextView.append(container_data.toString());*/
}

    private void createPieChart()
    {
        mPieChart.setUsePercentValues(true);
        mPieChart.setHoleColorTransparent(true);
        mPieChart.setHoleRadius(60f);

        mPieChart.setDescription("");

        mPieChart.setDrawCenterText(true);

        mPieChart.setDrawHoleEnabled(true);

        mPieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(true);
         String[] mParties = new String[] {
                "Protein", "Fat", "Carbs", "Vitamin", "Minerals"
        };
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        // mChart.setTouchEnabled(false);

        mPieChart.setCenterText("Nutrition Chart");



        mPieChart.animateXY(1500, 1500);
        // mChart.spin(2000, 0, 360);

        Legend lpie = mPieChart.getLegend();
        lpie.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        lpie.setXEntrySpace(7f);
        lpie.setYEntrySpace(5f);
        float mult = 10;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < 4 + 1; i++) {
            yVals1.add(new Entry((float) (Math.random() * mult) + mult / 5, i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < 5 + 1; i++)
            xVals.add(mParties[i % mParties.length]);

        PieDataSet dataSet = new PieDataSet(yVals1, "Nutri Data");
        dataSet.setSliceSpace(3f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    public void run_ask_quest(final String url,final int id)
            throws Exception {
        Log.d(tag, "URL");
        Request request = new Request.Builder()
                .url(url)
                .get().build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Request request, IOException e)
            {
                Log.d("deb exception", e.toString());
                runThread("", 2);
            }

            @Override
            public void onResponse(Response response) throws IOException
            {
                if (!response.isSuccessful())
                    throw new IOException("Unexpected code " + response);
                json_str = response.body().string();
                System.out.println(json_str);
                runThread(json_str, id);
            }
        });
    }

    private void runThread(final String json_str,final int id) {

        new Thread() {
            public void run() {
              runOnUiThread(new Runnable()
              {
                  @Override
                  public void run()
                  {
                      Log.d(tag,"DETAILS ID =  "+ id);
                      Log.d(tag,"DETAILS "+ json_str);
                      if(id==1)
                      {

                      }
                      else
                      {

                          container_data = deserialize(json_str);
                          setmHorizontalBarChartData(container_data.getItemWeight().get(container_data.getItemWeight().size()-1));
                          setLineChartData();
                          swipeContainer.setRefreshing(false);
                      }

                  }
              });
            }
        }.start();
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
    private Container deserialize(String json_string)
    {
        Container mContainer = null;
        try
        {
            mContainer = new Gson().fromJson(json_string, Container.class);
        }catch (Exception e)
        {
            json_string = "{\"containerId\": 1 ,\"item\": \"salt\",\"itemWeight\": [1,2] ,\"date\" : [1432536,2345678]}";
            mContainer = new Gson().fromJson(json_string, Container.class);

        }
        return mContainer;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_container_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void create_horizontal_chart()
    {

        mHorizontalBarChart.setDrawGridBackground(false);
        XAxis xl = mHorizontalBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setDrawLabels(false);
        xl.setSpaceBetweenLabels(1000);
        LimitLine maxLimitLine = new LimitLine(5.0f,"Max");
        LimitLine minLimitLine = new LimitLine(0.200f,"Min");

        YAxis yl = mHorizontalBarChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.addLimitLine(maxLimitLine);
        yl.addLimitLine(minLimitLine);
        yl.setAxisMaxValue(5.5f);
        yl.setDrawLabels(false);

        mHorizontalBarChart.setTouchEnabled(false);
        mHorizontalBarChart.setDrawBarShadow(true);
        mHorizontalBarChart.setBackground(new ColorDrawable(ApplicationClass.static_context.getResources().getColor(R.color.transparent)));
        mHorizontalBarChart.animateY(1500);

    }

    public void setmHorizontalBarChartData(float weight)
    {
        if(weight<=0f)
        {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            TextView mTextView = new TextView(this);
            mTextView.setGravity(TextView.TEXT_ALIGNMENT_GRAVITY);
            mTextView.setTextSize(20);
            mTextView.setText("\n"+container_data.getItem() + " is over!\n Do You want to order it now?");
            alert.setView(mTextView);
            alert.setNeutralButton("Order", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    startActivity(getPackageManager().getLaunchIntentForPackage("com.bigbasket.mobileapp"));

                }
            });
            alert.show();
        }

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("");
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();


        yVals1.add(new BarEntry(weight, 0));
        //yVals1.add(new BarEntry(5.0f,1));
        BarDataSet set1 = new BarDataSet(yVals1, "Quantity Left (KGs)");
        set1.setLabel("");
        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        mHorizontalBarChart.setDescription("");
        mHorizontalBarChart.setData(data);
        mHorizontalBarChart.invalidate();
        mHorizontalBarChart.notifyDataSetChanged();
    }

    private void createLineGraph() {
        mLineChart.setDrawGridBackground(false);
        // no description text
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("Jar Empty");

        // enable value highlighting
        mLineChart.setHighlightEnabled(true);
        // enable touch gestures
        mLineChart.setTouchEnabled(true);
        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setDrawGridBackground(false);
        //mLineChart.setBackgroundColor(getResources().getColor(R.color.white));
        // if disabled, scaling can be done on x- and y-axis separately
        mLineChart.setPinchZoom(true);
        // set an alternative background color
        // add data
        mLineChart.animateX(1000);
        // get the legend (only possible after setting data)
        Legend l = mLineChart.getLegend();
        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        XAxis xaxis = mLineChart.getXAxis();
        YAxis yaxis = mLineChart.getAxisLeft();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setAvoidFirstLastClipping(true);




    }
    void setLineChartData()
    {
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        List<Float> weight_deduc = container_data.getItemWeight();
        int i = 0;
        for (Long be : container_data.getDate())
        {
            xVals.add((String) android.text.format.DateFormat.format("dd/MMM", be * 1000));
            yVals.add(new Entry(weight_deduc.get(i), i));
            i++;
        }
        //Log.d(tag, "Finished  Adding Values");
        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Consumption");
        set1.setColor(getResources().getColor(R.color.primary_blue));
        //set1.setDrawFilled(true);
        set1.setDrawCircles(true);
        set1.setCircleColor(getResources().getColor(R.color.primary_blue));

        set1.setLineWidth(2f);
        // set1.setFillColor(Color.GREEN);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setDrawValues(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.setData(data);
        mLineChart.invalidate();
        mLineChart.notifyDataSetChanged();
    }

}
