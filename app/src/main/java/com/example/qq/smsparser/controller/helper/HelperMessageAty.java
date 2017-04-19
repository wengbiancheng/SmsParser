package com.example.qq.smsparser.controller.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.MainActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 帮工工作数据的展示界面
 */
public class HelperMessageAty extends Activity implements View.OnClickListener {

    private GridView gridView;
    private PieChart pieChart;

    private List<Map<String, String>> datas = new ArrayList<>();

    private LinearLayout titleLeft;
    private TextView titleMiddle;
    private Button titleRight;

    private float sales, helper_sale_cost, delivery_cost, other_cost, profit;
    private int number=0;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_message);

        sales = getIntent().getFloatExtra("sale", 0);
        helper_sale_cost = getIntent().getFloatExtra("helper_cost", 0);
        delivery_cost = getIntent().getFloatExtra("send_cost", 0);
        other_cost = getIntent().getFloatExtra("other_cost", 0);
        profit = getIntent().getFloatExtra("profit", 0);
        number=getIntent().getIntExtra("number",0);
        name=getIntent().getStringExtra("helperName");

        initView();
        initData();
        initListener();
        initPieData();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gv_aty_helper_message);

        titleLeft = (LinearLayout) findViewById(R.id.title_leftImageBtn);
        titleMiddle = (TextView) findViewById(R.id.title_middleTextView);
        titleRight = (Button) findViewById(R.id.title_rightBtn);
        titleLeft.setVisibility(View.VISIBLE);


        pieChart = (PieChart) findViewById(R.id.helper_month);
    }

    private HashMap<String, String> getHashMap(String content) {
        HashMap<String, String> cell = new HashMap<>();
        cell.put("cell", content);
        return cell;
    }

    private void initData() {
        datas.add(getHashMap("销售总额"));
        datas.add(getHashMap((float) (Math.round(sales * 100)) / 100 + "元"));
        datas.add(getHashMap("发货订单量"));
        datas.add(getHashMap(number+"单"));
        datas.add(getHashMap("帮工费用"));
        datas.add(getHashMap((float) (Math.round(helper_sale_cost * 100)) / 100 + "元"));
        datas.add(getHashMap("快递费用"));
        datas.add(getHashMap((float) (Math.round(delivery_cost * 100)) / 100 + "元"));
        datas.add(getHashMap("包装费用"));
        datas.add(getHashMap((float) (Math.round(other_cost * 100)) / 100 + "元"));
        datas.add(getHashMap("纯利润"));
        datas.add(getHashMap((float) (Math.round(profit * 100)) / 100 + "元"));

        titleMiddle.setText(name+"的工作数据统计");
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, datas, R.layout.item_gv, new String[]{"cell"}, new int[]{R.id.tv_item});
        gridView.setAdapter(simpleAdapter);

    }

    private void initListener() {
        titleLeft.setOnClickListener(this);
    }

    private void initPieData() {
        /**图的各种性质设置**/
        //是否显示圆盘中间文字，默认显示
        pieChart.setDrawCenterText(true);
        //设置圆盘中间文字
        pieChart.setCenterText("总销售额:" + sales + "元");
        //设置圆盘中间文字的大小
        pieChart.setCenterTextSize(16);
        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.setDescription("");
        pieChart.setClickable(false);

        /**数据设置**/
        ArrayList<String> xValues = new ArrayList<>();
        xValues.add("帮工工资 " + (float) (Math.round(helper_sale_cost * 100)) / 100 + "元");
        xValues.add("快递花费 " + (float) (Math.round(delivery_cost * 100)) / 100 + "元");
        xValues.add("其他花费 " + (float) (Math.round(other_cost * 100)) / 100 + "元");
        xValues.add("纯利润 " + (float) (Math.round(profit * 100)) / 100 + "元");

        List<Entry> values = new ArrayList<>();
        values.add(new Entry(helper_sale_cost, 0));
        values.add(new Entry(delivery_cost, 1));
        values.add(new Entry(other_cost, 2));
        values.add(new Entry(profit, 3));

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(values, "");
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        ArrayList<Integer> colors = new ArrayList<Integer>();
        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));
        pieDataSet.setColors(colors);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);
        // 设置成PercentFormatter将追加%号
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(10);
        pieChart.setData(pieData);

        /**比例图设置**/
        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
        mLegend.setXEntrySpace(5f);
        mLegend.setYEntrySpace(5f);
        mLegend.setTextSize(8);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftImageBtn:
                Intent intent=new Intent(this, MainActivity.class);
                intent.putExtra("Fragment",3);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
