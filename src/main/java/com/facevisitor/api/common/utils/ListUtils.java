package com.facevisitor.api.common.utils;

import java.util.List;

public class ListUtils {

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }


}


