package com.example.myschool.ui.home;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/*
使用LiveData包装的值，然后在Activity或Fragment中观察它，就可以主动将数据变化通知给Activity或Fragment，解决线程问题
*/
public class ViewPagerViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    /**
     * 第一个参数是mIndex，map会对它进行观察；当mIndex发生变化，就会调用这里的转换函数
     * 第二参数是转换函数，map方法可以将第一个LiveData参数自由转换成任意类型的LiveData
     * Activity或Fragment只要观察这个LiveData对象index就行了
     */
    private LiveData<Integer> index = Transformations.map(mIndex, new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer i) {
            return i;
        }
    });

    /**
     * 调用这个方法就会导致mIndex被观察到变化*
     */
    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<Integer> getIndex() {
        return index;
    }
}