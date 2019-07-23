package com.xinhua.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class XmlUtil {
    public static JSONObject xml2JSON(byte[] xml) {
        JSONObject json = new JSONObject();
        InputStream is = new ByteArrayInputStream(xml);
        SAXBuilder sb = new SAXBuilder();
        org.jdom2.Document doc;
        try {
            doc = sb.build(is);
            Element root = doc.getRootElement();
            json.put(root.getName(), iterateElement(root));
            return json;
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static JSONObject iterateElement(Element element) {
        List<Element> node = element.getChildren();
        JSONObject obj = new JSONObject();
        List list = null;
        for (Element child : node) {
            list = new LinkedList();
            String text = child.getTextTrim();
            if (StringUtils.isBlank(text)) {
                if (child.getChildren().size() == 0) {
                    continue;
                }
                if (obj.containsKey(child.getName())) {
                    list = (List) obj.get(child.getName());
                }
                list.add(iterateElement(child)); //遍历child的子节点
                obj.put(child.getName(), list);
            } else {
                if (obj.containsKey(child.getName())) {
                    Object value = obj.get(child.getName());
                    try {
                        list = (List) value;
                    } catch (ClassCastException e) {
                        list.add(value);
                    }
                }
                if (child.getChildren().size() == 0) { //child无子节点时直接设置text
                    obj.put(child.getName(), text);
                } else {
                    list.add(text);
                    obj.put(child.getName(), list);
                }
            }
        }
        return obj;
    }


//    private static JSONObject iterateElement(Element element) {
//        List node = element.getChildren();
//        Element et;
//        JSONObject obj = new JSONObject();
//        for (int i = 0; i < node.size(); i++) {
//            et = (Element) node.get(i);
//            if (et.getChildren().size() == 0) {
//                obj.put(et.getName(), et.getValue());
//            } else {
//                System.out.println(et.getName());
//                obj.put(et.getName(), iterateElement(et));
//            }
//        }
//        System.out.println(obj.toJSONString());
//        return obj;
//    }
}