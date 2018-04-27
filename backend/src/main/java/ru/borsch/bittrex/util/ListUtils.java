package ru.borsch.bittrex.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static <T> List<List<T>> splitByEqualParts(List<T> list, int partsCount) {
        List<List<T>> parts = new ArrayList<List<T>>();

        double mod = ((double)(list.size() % partsCount))/partsCount;

        int partLength = (mod > 0.5)?(list.size()/partsCount)+1:list.size()/partsCount;

        for (int i=0; i < partsCount; i++){
            if (i+1 == partsCount && mod <=0.5){
                parts.add(new ArrayList<T>(
                        list.subList(i*partLength, list.size()))
                );
            }else {
                parts.add(new ArrayList<T>(
                        list.subList(i * partLength, Math.min(list.size(), (i + 1) * partLength)))
                );
            }
        }

        return parts;
    }
}
