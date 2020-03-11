package util;

import java.util.HashMap;

public  class KVMap {

    private static HashMap<String,Object> map;
    public static void setKV(String key,String value){
        map.put(key,value);
    }

    public static Object getValue(String key){
        return map.get(key);
    }
}
