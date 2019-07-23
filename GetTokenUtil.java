package com.abb.apm51;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abb.apifunc.HttpsUtils;
import com.alibaba.fastjson.JSONObject;
import com.mysql.jdbc.StringUtils;

//TODO: 1）线程安全
//      2) token超时
public class GetTokenUtil {

    protected static Logger logger = LoggerFactory.getLogger(GetTokenUtil.class);

    private static String token = null;

    public static String GetToken() {
        if (StringUtils.isNullOrEmpty(token)) {
            getTokenFromServer();
        }
        return token;
    }

    public static void getTokenFromServer() {
        //getToken
        String url = "https://adfs.andysongxt.top/adfs/oauth2/token";
        Map<String, String> header = new HashMap();
        header.put("Content-Type", " application/x-www-form-urlencoded");
        Map<String, String> param = new HashMap();
        param.put("grant_type", "client_credentials");
        param.put("resource", "APM-webservice-app");
        param.put("client_id", "APM-webservice-import");
        param.put("client_secret", "wanKo8nl01MVuNUnsFyrZv-g_v4fRxNjT-qHKK7A");
        HttpEntity entity = null;
        String returnStr = "";

        try {
            returnStr = HttpsUtils.post(url, header, param, entity);
        } catch (Exception e) {
            // TODO Auto-generated catch block

            logger.error("获取token失败：" + e.toString());
        }

        JSONObject jsonDataObj = JSONObject.parseObject(returnStr);
        String returnAccessToken = jsonDataObj.getString("access_token");
        if (StringUtils.isNullOrEmpty(returnAccessToken)) {
            logger.error("returnAccessToken is null");
        } else {

            token = "Bearer " + returnAccessToken;
        }

    }
}
