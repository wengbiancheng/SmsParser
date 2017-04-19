package com.example.qq.smsparser.controller.analysis;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.qq.smsparser.MyApplication;
import com.example.qq.smsparser.R;
import com.example.qq.smsparser.controller.BaseFragment;
import com.example.qq.smsparser.entity.OrderSaleMessage;
import com.example.qq.smsparser.model.db.DbutilOrder;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 销售数据的展示界面
 */
public class SaleListFragment extends BaseFragment implements LineChartOnValueSelectListener {

    /*=========== 控件相关 ==========*/
    private LineChartView lvc_salse;
    private PieChart pieChart;

    /*=========== 数据相关 ==========*/
    private LineChartData mLineData;
    private int numberOfLines = 2;
    private int maxNumberOfPoints = 12;
    private int numberOfPoints=-1;
    private float sales = 0, helper_sale_cost = 0, delivery_cost = 0, other_cost = 0,profit=0;
    private float[][] randomNumbersTab = new float[numberOfLines][maxNumberOfPoints + 1]; //将线上的点放在一个数组中
    private Map<Integer, List<OrderSaleMessage>> map = new HashMap<>();

    /*=========== 状态相关 ==========*/
    private boolean isHasAxes = true;                   //是否显示坐标轴
    private boolean isHasAxesNames = true;              //是否显示坐标轴名称
    private boolean isHasLines = true;                  //是否显示折线/曲线
    private boolean isHasPoints = true;                 //是否显示线上的节点
    private boolean isFilled = false;                   //是否填充线下方区域
    private boolean isHasPointsLabels = false;          //是否显示节点上的标签信息
    private boolean isCubic = false;                    //是否是立体的
    private boolean isPointsHasSelected = false;        //设置节点点击后效果(消失/显示标签)
    private boolean isPointsHaveDifferentColor;         //节点是否有不同的颜色

    /*=========== 其他相关 ==========*/
    private ValueShape pointsShape = ValueShape.CIRCLE; //点的形状(圆/方/菱形)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("Process", "HelperFragment:onCreateView");
        View view = inflater.inflate(R.layout.fragment_salelist, null);

        initView(view);
        initData();
        initListener();
        return view;
    }

    private void initView(View view) {
        lvc_salse = (LineChartView) view.findViewById(R.id.lvc_sales);
        lvc_salse.setViewportCalculationEnabled(false);
        pieChart= (PieChart) view.findViewById(R.id.lvc_pie);

        view.findViewById(R.id.line1).setBackgroundColor(ChartUtils.COLORS[0]);
        view.findViewById(R.id.line2).setBackgroundColor(ChartUtils.COLORS[1]);

        Calendar now = Calendar.getInstance();
        ((TextView) view.findViewById(R.id.tv_sales)).setText(now.get(Calendar.YEAR) + "年销售额");
        ((TextView) view.findViewById(R.id.tv_profit)).setText(now.get(Calendar.YEAR) + "年销售统计");

        numberOfPoints=now.get(Calendar.MONTH)+1;
    }

    private void initData() {
        setPointsValues();          //设置每条线的节点值
        setLinesDatas();            //设置每条线的一些属性
        resetViewport();            //计算并绘图

        setPieDatas();
    }

    private void initListener() {
        lvc_salse.setOnValueTouchListener(this);
    }

    /**
     * 初始化点的值
     */
    private void setPointsValues() {
        for (int i = 1; i <= numberOfPoints; i++) {
            randomNumbersTab[0][i] = 0;
            randomNumbersTab[1][i] = 0;
        }
        map = DbutilOrder.getInstance().getAllOrderSaleMessage(((MyApplication) Baseactivity.getApplication()).getSQLiteOpenHelper().getReadableDatabase());
        if (map != null) {
            for (int i = 1; i <= numberOfPoints; i++) {
                if (map.containsKey(i)) {
                    List<OrderSaleMessage> list = map.get(i);
                    for (int j = 0; j < list.size(); j++) {
                        randomNumbersTab[0][i] = randomNumbersTab[0][i] + list.get(j).getGood_price();
                        randomNumbersTab[1][i] = randomNumbersTab[1][i] + list.get(j).getProfit();
                    }
                } else {
                    randomNumbersTab[0][i] = 0;
                    randomNumbersTab[1][i] = 0;
                }
            }
        }
    }

    /**
     * 设置线的相关数据
     */
    private void setLinesDatas() {
        List<Line> lines = new ArrayList<>();
        //循环将每条线都设置成对应的属性
        for (int i = 0; i < numberOfLines; ++i) {
            //节点的值
            List<PointValue> values = new ArrayList<>();
            for (int j = 1; j <= numberOfPoints; ++j) { //从1月份开始共有12条线
                values.add(new PointValue(j, randomNumbersTab[i][j]));
            }

            /*========== 设置线的一些属性 ==========*/
            Line line = new Line(values);               //根据值来创建一条线
            line.setColor(ChartUtils.COLORS[i]);        //设置线的颜色
            line.setShape(pointsShape);                 //设置点的形状
            line.setHasLines(isHasLines);               //设置是否显示线
            line.setHasPoints(isHasPoints);             //设置是否显示节点
            line.setCubic(isCubic);                     //设置线是否立体或其他效果
            line.setFilled(isFilled);                   //设置是否填充线下方区域
            line.setHasLabels(true);                    //设置是否显示节点标签
            //设置节点点击的效果
            line.setHasLabelsOnlyForSelected(isPointsHasSelected);
            //如果节点与线有不同颜色 则设置不同颜色
            if (isPointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        mLineData = new LineChartData(lines);                      //将所有的线加入线数据类中
        mLineData.setBaseValue(Float.NEGATIVE_INFINITY);           //设置基准数(大概是数据范围)

        //如果显示坐标轴
        if (isHasAxes) {
            Axis axisX = new Axis();                    //X轴
            Axis axisY = new Axis().setHasLines(true);  //Y轴
            axisX.setTextColor(Color.GRAY);             //X轴灰色
            axisY.setTextColor(Color.GRAY);             //Y轴灰色
            //setLineColor()：此方法是设置图表的网格线颜色 并不是轴本身颜色
            //如果显示名称
            if (isHasAxesNames) {
                axisX.setName("月份");                //设置名称
                axisY.setName("金额/百");
            }
            mLineData.setAxisXBottom(axisX);            //设置X轴位置 下方
            mLineData.setAxisYLeft(axisY);              //设置Y轴位置 左边
        } else {
            mLineData.setAxisXBottom(null);
            mLineData.setAxisYLeft(null);
        }

        lvc_salse.setLineChartData(mLineData);    //设置图表控件
    }

    /**
     * 确定x轴和y轴的范围
     */
    private void resetViewport() {
        //创建一个图标视图 大小为控件的最大大小
        final Viewport v = new Viewport(lvc_salse.getMaximumViewport());
        v.bottom = 0;                          //y轴最小值
        v.top = 200;                            //y轴最大值
        v.left = 0;                             //x轴最小值
        v.right = maxNumberOfPoints + 1;           //y轴最大值
        lvc_salse.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        lvc_salse.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图
    }

    /**
     * 设置饼图的各种性质
     */
    private void setPieDatas() {
        /**各种数据初始化**/
        if(map == null){
            return;
        }
        for (int i = 1; i <= 12; i++) {
            if(!map.containsKey(i)){
                continue;
            }
            List<OrderSaleMessage> data = map.get(i);
            for (int j = 0; j < data.size(); j++) {
                sales = sales + data.get(j).getGood_price();
                helper_sale_cost = helper_sale_cost + data.get(j).getHelper_cost();
                delivery_cost = delivery_cost + data.get(j).getDelivery_price();
                other_cost = other_cost + data.get(j).getOther_cost();
                profit=profit+data.get(j).getProfit();
            }
        }
        Log.e("TestSale","帮工的工资消费是:"+helper_sale_cost+";快递费用是:"+delivery_cost
        +";其他花费是:"+other_cost+";纯利润是:"+profit);

        /**图的各种性质设置**/
        //是否显示圆盘中间文字，默认显示
        pieChart.setDrawCenterText(true);
        //设置圆盘中间文字
        pieChart.setCenterText("总销售额:"+sales+"元");
        //设置圆盘中间文字的大小
        pieChart.setCenterTextSize(20);
        pieChart.setUsePercentValues(true);  //显示成百分比
        pieChart.setDescription("");
        pieChart.setClickable(false);

        /**数据设置**/
        ArrayList<String> xValues=new ArrayList<>();
        xValues.add("帮工工资 "+ (float)(Math.round(helper_sale_cost*100))/100+"元");
        xValues.add("快递花费 "+ (float)(Math.round(delivery_cost*100))/100+"元");
        xValues.add("其他花费 "+ (float)(Math.round(other_cost*100))/100+"元");
        xValues.add("纯利润 "+ (float)(Math.round(profit*100))/100+"元");

        List<Entry> values=new ArrayList<>();
        values.add(new Entry(helper_sale_cost,0));
        values.add(new Entry(delivery_cost,1));
        values.add(new Entry(other_cost,2));
        values.add(new Entry(profit,3));

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(values,"");
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

        PieData pieData = new PieData(xValues,pieDataSet);
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
    public void onValueSelected(int line, int month, PointValue pointValue) {
        Intent intent = new Intent(Baseactivity, SaleMessageAty.class);
        List<OrderSaleMessage> list=map.get(month+1);
        float sale=0,helper_cost=0,send_cost=0,other_cost=0,profit=0;
        if(list!=null){
            for(int i=0;i<list.size();i++){
                sale=sale+list.get(i).getGood_price();
                helper_cost=helper_cost+list.get(i).getHelper_cost();
                send_cost=send_cost+list.get(i).getDelivery_price();
                other_cost=other_cost+list.get(i).getOther_cost();
                profit=profit+list.get(i).getProfit();
            }
        }
        intent.putExtra("month",month+1);
        intent.putExtra("sale",sale);
        intent.putExtra("helper_cost",helper_cost);
        intent.putExtra("send_cost",send_cost);
        intent.putExtra("other_cost",other_cost);
        intent.putExtra("profit",profit);
        startActivity(intent);
    }

    @Override
    public void onValueDeselected() {

    }
}
