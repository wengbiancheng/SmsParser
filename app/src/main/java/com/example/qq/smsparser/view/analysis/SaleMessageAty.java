package com.example.qq.smsparser.view.analysis;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.qq.smsparser.R;

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
 * Created by qq on 2017/3/11.
 */
public class SaleMessageAty extends Activity implements View.OnClickListener{

    private GridView gridView;
    private TextView textView;
    private LineChartView mLineChartView;

    private List<Map<String,String>> datas=new ArrayList<>();

    private LinearLayout titleLeft;
    private TextView titleMiddle;
    private Button titleRight;

    /*=========== 控件相关 ==========*/
    private LineChartView lvc_salse;
    private LineChartView lvc_profit;

    /*=========== 数据相关 ==========*/
    private LineChartData mLineData;
    private int numberOfLines=1;
    private int maxNumberOfLines=4;
    private int numberOfPoints=30;

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
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints]; //将线上的点放在一个数组中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_message);

        initView();
        initTestData();
        initData();
        initListener();
    }

    private void initView(){
        gridView= (GridView) findViewById(R.id.gv_aty_sale_message);
        textView= (TextView) findViewById(R.id.tv_aty_sale_message);
        mLineChartView= (LineChartView) findViewById(R.id.lvc_aty_sale_message);

        titleLeft= (LinearLayout) findViewById(R.id.title_leftImageBtn);
        titleMiddle= (TextView) findViewById(R.id.title_middleTextView);
        titleRight= (Button) findViewById(R.id.title_rightBtn);
        titleLeft.setVisibility(View.VISIBLE);

        mLineChartView= (LineChartView) findViewById(R.id.lvc_aty_sale_message);
    }

    private void initTestData(){
        datas.add(getHashMap("销售总额"));
        datas.add(getHashMap("770"));
        datas.add(getHashMap("帮工费用"));
        datas.add(getHashMap("200"));
        datas.add(getHashMap("快递费用"));
        datas.add(getHashMap("100"));
        datas.add(getHashMap("包装费用"));
        datas.add(getHashMap("50"));
        datas.add(getHashMap("其他费用"));
        datas.add(getHashMap("20"));
        datas.add(getHashMap("纯利润"));
        datas.add(getHashMap("400"));
    }
    private HashMap<String,String> getHashMap(String content){
        HashMap<String,String> cell=new HashMap<>();
        cell.put("cell",content);
        return cell;
    }

    private void initData(){
        titleMiddle.setText("1月的销售数据");
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,datas,R.layout.item_gv,new String[]{"cell"},new int[]{R.id.tv_item});
        gridView.setAdapter(simpleAdapter);

        setPointsValues();          //设置每条线的节点值
        setLinesDatas();            //设置每条线的一些属性
        resetViewport();            //计算并绘图
    }

    private void initListener(){
        titleLeft.setOnClickListener(this);
    }

    /**
     * 利用随机数设置每条线对应节点的值
     */
    private void setPointsValues() {
        for (int i = 0; i < maxNumberOfLines; ++i) {
            for (int j = 0; j < numberOfPoints; ++j) {
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
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
            for (int j = 0; j < numberOfPoints; ++j) {
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
            line.setHasLabels(isHasPointsLabels);       //设置是否显示节点标签
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
        /* 其他的一些属性方法 可自行查看效果
         * mLineData.setValueLabelBackgroundAuto(true);            //设置数据背景是否跟随节点颜色
         * mLineData.setValueLabelBackgroundColor(Color.BLUE);     //设置数据背景颜色
         * mLineData.setValueLabelBackgroundEnabled(true);         //设置是否有数据背景
         * mLineData.setValueLabelsTextColor(Color.RED);           //设置数据文字颜色
         * mLineData.setValueLabelTextSize(15);                    //设置数据文字大小
         * mLineData.setValueLabelTypeface(Typeface.MONOSPACE);    //设置数据文字样式
        */

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
                axisY.setName("金额");
            }
            mLineData.setAxisXBottom(axisX);            //设置X轴位置 下方
            mLineData.setAxisYLeft(axisY);              //设置Y轴位置 左边
        } else {
            mLineData.setAxisXBottom(null);
            mLineData.setAxisYLeft(null);
        }

        mLineChartView.setLineChartData(mLineData);    //设置图表控件
    }

    /**
     * 重点方法，计算绘制图表
     */
    private void resetViewport() {
        //创建一个图标视图 大小为控件的最大大小
        final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.left = 0;                             //坐标原点在左下
        v.bottom = 0;
        v.top = 100;                            //最高点为100
        v.right = numberOfPoints - 1;           //右边为点 坐标从0开始 点号从1 需要 -1
        mLineChartView.setMaximumViewport(v);   //给最大的视图设置 相当于原图
        mLineChartView.setCurrentViewport(v);   //给当前的视图设置 相当于当前展示的图

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_leftImageBtn:
                onBackPressed();
                break;
        }
    }
}
