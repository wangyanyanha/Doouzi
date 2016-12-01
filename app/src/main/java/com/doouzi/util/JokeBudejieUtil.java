package com.doouzi.util;


import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author wangyan
 */
public class JokeBudejieUtil {

    private Parser parser = null;   //用于分析网页的分析器。

    public JokeBudejieUtil() {
    }

    public static String getJokes(int page)
    {
        return new JokeBudejieUtil().parser("http://www.budejie.com/text/"+page);
    }


    /**
     * 对新闻URL进行解析提取新闻，同时将新闻插入到数据库中。
     * @param url 新闻连接。
     */
    public String parser(String url) {
        try {
            JSONArray array=new JSONArray();
            parser = new Parser(url);
            NodeFilter timeFilter=new AndFilter(new TagNameFilter("span"), new HasAttributeFilter("class", "u-time  f-ib f-fr"));
            NodeFilter contentFilter = new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "j-r-list-c-desc"));
            OrFilter mainFilter = new OrFilter(timeFilter,contentFilter);
            NodeList list = parser.extractAllNodesThatMatch(mainFilter);
            JSONObject obj=null;
            for (int i = 0; i < list.size(); i++) {
        		Node tag = list.elementAt(i);
        		if(tag.getText().equals("span class=\"u-time  f-ib f-fr\""))
        		{
        			String time=((Span)tag).getStringText();
        			System.out.println(time+" ");
                    obj=new JSONObject();
                    obj.put("time", time);
                }else if(tag.getText().equals("div class=\"j-r-list-c-desc\""))
        		{
        			String joke=((Div)tag).getStringText();
                    int s=joke.indexOf(">");
                    joke=joke.substring(s+1);
                    int e=joke.indexOf("<");
                    joke=joke.substring(0,e);
                    joke=joke.replaceAll("<br />","\n");
                    joke=joke.replaceAll(" ","");
                    System.out.println(joke + "\n");
                    if(obj!=null) {
                        obj.put("content", joke);
                        array.put(obj);
                    }
        		}
        	}
            return array.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (ParserException ex) {
            Logger.getLogger(JokeBudejieUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "[]";
    }

//    //单个文件测试网页
//    public static void main(String[] args) {
//        Jokes news = new Jokes();
//        news.parser("http://www.budejie.com/text");
//        for(int x=2;x<=100;x++)
//        {
//        	try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	news.parser("http://www.budejie.com/text/"+x);
//        }
//    }
}
