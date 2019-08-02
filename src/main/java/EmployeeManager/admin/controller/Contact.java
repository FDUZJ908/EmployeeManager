package EmployeeManager.admin.controller;

import EmployeeManager.Server;
import EmployeeManager.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;

@Controller
@RequestMapping("/contact")
public class Contact {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    Server server;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public String Verify(@RequestParam(value = "msg_signature") String msgsig,
                         @RequestParam(value = "timestamp") String timestamp,
                         @RequestParam(value = "nonce") String nonce,
                         @RequestParam(value = "echostr") String echostr,
                         Model model) {
        String text = "";
        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(Variable.callbackToken, Variable.callbackEncodingAESKey, Variable.corpid);
            text = wxcpt.VerifyURL(msgsig, timestamp, nonce, echostr);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return text;
    }

    private String getTagByDefault(Element root, String tag) {
        return getTagByDefault(root, tag, "");
    }

    private String getTagByDefault(Element root, String tag, String def) {
        NodeList nodeList = root.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) return def;
        else return nodeList.item(0).getTextContent();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String Update(@RequestParam(value = "msg_signature") String msgsig,
                         @RequestParam(value = "timestamp") String timestamp,
                         @RequestParam(value = "nonce") String nonce,
                         @RequestBody String data,
                         Model model) {

        try {
            WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(Variable.callbackToken, Variable.callbackEncodingAESKey, Variable.corpid);
            String msg = wxcpt.DecryptMsg(msgsig, timestamp, nonce, data);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(msg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            String changeType = root.getElementsByTagName("ChangeType").item(0).getTextContent();
            if (changeType.equals("create_user")) {
                Map<String, Object> user = new HashMap<>();

                user.put("userID", root.getElementsByTagName("UserID").item(0).getTextContent());
                user.put("userName", root.getElementsByTagName("Name").item(0).getTextContent());
                user.put("gender", getTagByDefault(root, "Gender", "0"));
                user.put("tel", getTagByDefault(root, "Mobile"));
                user.put("email", getTagByDefault(root, "Email"));
                user.put("avatarURL", getTagByDefault(root, "Avatar"));
                server.insertMap(user, "user");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "Callback succeed!";
    }
}
