package com.example.qq.smsparser.model.analysis;

import android.content.Context;

/**
 * 数据分析模块，主要的功能是从数据库中提取出订货数据和帮工的发货数据，然后解析成相应的形式返回给view中的
 * Activity进行展示
 */
public class SaleManager {

    private SaleManager saleManager=null;
    private Context context;
    private SaleManager(Context context){
        this.context=context;
    }

    public SaleManager getSaleManagerInstance(Context context){
        if(saleManager==null){
            saleManager=new SaleManager(context);
            return saleManager;
        }
        return saleManager;
    }

    //TODO 应该写一个方法进行数据库的操作,private

    //TODO 应该写一个方法进行对数据库获取的数据进行相应的处理和转化，应该使用子线程进行处理,private

    //TODO 应该写一个方法给Activity进行调用，为public，暴露处理好的数据给外界调用,public

}
