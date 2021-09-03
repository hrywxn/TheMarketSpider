package com.yun.market.task;

import com.alibaba.fastjson.JSONObject;
import com.yun.market.model.push.PushModel;
import com.yun.market.model.push.Text;
import com.yun.market.model.push.YybModel;
import com.yun.market.service.push.PushService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * description: TT <br>
 * date: 2021/8/12 下午5:25 <br>
 * author: chenxiangfa <br>
 * version: 1.0 <br>
 */
@Component
public class TT {

    @Autowired
    private PushService pushService;

    @Value("${url}")
    private String url;

//    @PostConstruct
    public void tt(){
        HttpPost post = new HttpPost(url);;
        HttpClient httpClient = new DefaultHttpClient();
        post.setHeader("Content-type", "application/json; charset=utf-8");
        List<YybModel> byLocalTimeData = pushService.getByLocalTimeData();

        Text text =new Text();
        text.setContent(JSONObject.toJSONString(byLocalTimeData));

        PushModel pushModel =new PushModel(text);

        System.out.println(byLocalTimeData);


//        StringEntity entity = new StringEntity(, Charset.forName("UTF-8"));


    }
}
