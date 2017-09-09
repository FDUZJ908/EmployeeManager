package EmployeeManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lsh on 04/09/2017.
 */
public class Server {

    static final String PASecret = "SjKBiPi1lPrTjGCgjUEv4cOZvcVvaV3RMn0a3kQmlnY";
    Map<String, AccessToken> tokenList = new HashMap<String, AccessToken>();

    public String getAccessToken(String corpsecret, boolean newToken) {
        int time = (int) (System.currentTimeMillis() / 1000);
        if (!newToken && tokenList.containsKey(corpsecret) && tokenList.get(corpsecret).expireTime < time) {
            return tokenList.get(corpsecret).value;
        }
        HttpsGet http = new HttpsGet();
        try {
            JSONObject jsonObject = http.sendGet("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=wx7635106ee1705d6d&corpsecret=" + corpsecret);
            String value = jsonObject.getString("access_token");
            int expireTime = time + jsonObject.getInt("expires_in") / 4 * 3;
            tokenList.put(corpsecret, new AccessToken(value, expireTime));
            return value;
        } catch (Exception e) {
            return "failure";
        }
    }

    public String getUserId(String code, String corpsecret) {
        String UserId;
        String token = getAccessToken(corpsecret, false);
        HttpsGet http = new HttpsGet();
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=" + token + "&code=" + code;
            JSONObject jsonObject = http.sendGet(url);
            UserId = jsonObject.getString("UserId");
        } catch (Exception e) {
            return "failure";
        }
        return UserId;
    }

    public boolean isUser(String UserId) {
        String token = getAccessToken(PASecret, false);
        HttpsGet http = new HttpsGet();
        int errcode;
        String errmsg;
        try {
            String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=" + token + "&userid=" + UserId;
            JSONObject jsonObject = http.sendGet(url);
            errcode = jsonObject.getInt("errcode");
            errmsg = jsonObject.getString("errmsg");
        } catch (Exception e) {
            return false;
        }
        return errcode == 0 && errmsg.equals("ok");
    }


}
