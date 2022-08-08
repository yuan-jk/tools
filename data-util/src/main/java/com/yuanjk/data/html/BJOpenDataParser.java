package com.yuanjk.data.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类<code>Doc</code>用于：TODO
 *
 * @author yuanjk
 * @version 1.0
 */
public class BJOpenDataParser {

    public static void main(String[] args) throws IOException {

        String ts = "进京检查站(共24条)";

//        Pattern pattern = Pattern.compile(".*?(\\d+).*");
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(ts);
        if (matcher.find()) {
            System.out.println("match result count:"+matcher.groupCount());
            System.out.println("match result:"+matcher.group());
        }else {
            System.out.println("not find");
        }
//        htmlParser();
    }


    public static void htmlParser() throws IOException {
        File input = new File("C:\\Users\\yuanj\\Desktop\\地理空间_files\\mapnew.html");
        Document doc = Jsoup.parse(input, "UTF-8");
//
        Element content = doc.getElementById("map_result_headDiv_L11006");
//        Elements links = content.getElementsByTag("a");

//        Elements elements = doc.getElementsByClass("one-map-result-head");
        Elements elements = content.parent().children();

        String sizePatternStr = ".*(\\d+).*";

        Pattern sizePattern = Pattern.compile(sizePatternStr);

        for (Element childElement : elements) {
            System.out.println("==" + childElement.id());
//            System.out.println("class name: "+childElement.className());
            int total = 0;
            if (childElement.className().equalsIgnoreCase("one-map-result-head")) {
                String txt = childElement.getElementsByClass("one-map-result-title-lb").text();
                System.out.println(txt);
                Matcher matcher = sizePattern.matcher(txt);
//                System.out.println(matcher.group(1));
            } else if (childElement.className().equalsIgnoreCase("one-map-result-list")) {
                int curr = 0;
                Elements links = childElement.children();
                for (Element link : links) {
                    System.out.println("link item: " + link.attr("onclick"));
                }
            }
        }
    }

}
