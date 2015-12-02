package com.studioidan.turaco.utils;

import java.util.ArrayList;

/**
 * Created by PopApp_laptop on 02/12/2015.
 */
public class ArrayUtils {

    public static boolean isArrayEmpty(Object[] array){
        return array == null || array.length == 0;
    }

    public static boolean isArrayListEmpty(ArrayList arrayList){
        return arrayList == null || arrayList.size() == 0;
    }
}
