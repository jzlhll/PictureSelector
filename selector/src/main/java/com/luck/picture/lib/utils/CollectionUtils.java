package com.luck.picture.lib.utils;

import java.util.List;

public class CollectionUtils {
    public static <T> List<T> asList(T element) {
        var list = new java.util.ArrayList<T>();
        list.add(element);
        return list;
    }

    public static <T> List<T> asList(List<T> elements) {
        return new java.util.ArrayList<T>(elements);
    }
}
