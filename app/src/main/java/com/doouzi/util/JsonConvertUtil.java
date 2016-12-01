package com.doouzi.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by wangyan51 on 2016/11/29.
 */
public class JsonConvertUtil {

    public String Json2Json(JSONObject input,JSONObject conf)
    {
        return value_convert(conf,input);
    }

    private String Object_convert(JSONObject conf,JSONObject input)
    {
        StringBuilder output=new StringBuilder();
        output.append("{");
        Iterator<String> keys=conf.keys();
        while(keys.hasNext())
        {
            String key=keys.next();
            Object obj=null;
            try {
                obj=conf.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(output.length()==1)
                output.append("\""+key+"\":"+value_convert(obj,input));
            else
                output.append(",\""+key+"\":"+value_convert(obj,input));
        }
        output.append("}");
        return output.toString();
    }

    private String Array_convert(JSONArray conf,JSONObject input)
    {
        StringBuilder output=new StringBuilder();
        output.append("[");
        for(int x=0;x<conf.length();x++)
        {
            Object obj=null;
            try {
                obj=conf.get(x);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(output.length()==1)
                output.append(value_convert(obj,input));
            else
                output.append(","+value_convert(obj,input));
        }
        output.append("]");
        return output.toString();
    }

    private String value_convert(Object conf,JSONObject input)
    {
        StringBuilder output=new StringBuilder();
        if(conf==null)
        {
            output.append("null");
        }else if(conf instanceof JSONArray)
        {
            output.append(Array_convert((JSONArray)conf,input));
        }else if(conf instanceof JSONObject)
        {
            output.append(Object_convert((JSONObject)conf,input));
        }else if(conf instanceof String)
        {
            output.append(extract_data((String)conf,input));
        }else if(conf instanceof Integer)
        {
            output.append(conf);
        }else if(conf instanceof Boolean)
        {
            output.append(conf);
        }else
        {
            output.append("null");
        }
        return output.toString();
    }

    private String extract_data(String path,JSONObject input)
    {
        Log.d("wy_path",path);
        StringBuilder output=new StringBuilder();
        String[] paths=path.split("\\.");
        boolean is_array=path.contains("[*]");
        try {
            Iterator<Object> objs=sub_extract_data(input,paths,0).iterator();
            while(objs.hasNext())
            {
                if(output.length()==0)
                {
                    output.append("\""+objs.next()+"\"");
                }else
                {
                    output.append(",\""+objs.next()+"\"");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(is_array)
        {
            return "["+output.toString()+"]";
        }else
        {
            return output.toString();
        }
    }

    private Collection<Object> sub_extract_data(Object obj, String[] paths, int point) throws JSONException {
        Collection<Object> result=new ArrayList<>();
        Object object=obj;
        int x;
        Log.d("wy_length",""+paths.length);
        for(x=point;x<paths.length;x++)
        {
            if(paths[x].equals("[0]"))
            {
                if(object instanceof JSONArray && ((JSONArray)object).length()>0)
                    object=((JSONArray)object).get(0);
                else
                {
                    result.add("");
                    return result;
                }
            }else if(paths[x].equals("[*]"))
            {
                if(object instanceof JSONArray && ((JSONArray)object).length()>0)
                {
                    JSONArray array=(JSONArray)object;
                    for(int i=0;i<array.length();i++)
                    {
                        result.addAll(sub_extract_data(array.get(i),paths,x+1));
                    }
                    break;
                }else
                {
                    result.add("");
                    return result;
                }
            }else
            {
                if(((JSONObject)object).has(paths[x]))
                    object=((JSONObject)object).get(paths[x]);
                else {
                    result.add("");
                    return result;
                }
            }
        }
        if(x==paths.length)
        {
            Log.d("wy_obj",object.toString());
            result.add(object);
        }
        return result;
    }
}
