package com.colvis;

import java.util.*;

public class Converter <T> {

    public Converter() {
    }

    protected Map<Integer, T> converter(Object[] array){
        Map<Integer, T> output = new HashMap<>();
        for(int i = 0; i < array.length; i++){
            output.put(i, (T) array[i]);
        }
        return output;
    }

    protected Map<Integer, T> converter(Iterable<T> iterable){
        Iterator<T> iterator = iterable.iterator();
        Map<Integer, T> output = new HashMap<>();
        int key = 0;
        while(iterator.hasNext()){
            output.put(key, iterator.next());
            key++;
        }
        return output;
    }
}
