package com.yun.market.task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yun.market.common.CreatedSqlCommon;
import com.yun.market.model.ddx.MarketYybDayModel;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * description: GubitDDxTask <br>
 * date: 2021/8/23 下午4:05 <br>
 * author: chenxiangfa <br>
 * version: 1.0 <br>
 */
@Component
public class GuBitDDxTask {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Logger logger = LoggerFactory.getLogger(getClass());

//    @PostConstruct
    private void doSpiderTask() throws InterruptedException {
        List<SpiderTaskParameterMode> parameterModeList = new ArrayList<>();
        parameterModeList.add(new SpiderTaskParameterMode("Accept","*/*"));
        parameterModeList.add(new SpiderTaskParameterMode("Accept-Encoding","gzip, deflate"));
        parameterModeList.add(new SpiderTaskParameterMode("Accept-Language","zh-CN,zh;q=0.9"));
        parameterModeList.add(new SpiderTaskParameterMode("Connection","keep-alive"));
        parameterModeList.add(new SpiderTaskParameterMode("Cookie","__gads=ID=1c31277d8b749ff8-224adad717cb0062:T=1629703964:RT=1629703964:S=ALNI_MYFS1i1sWXJImxsjua-ICKSHIxq9A"));
        parameterModeList.add(new SpiderTaskParameterMode("Host","ddx.gubit.cn"));
        parameterModeList.add(new SpiderTaskParameterMode("Referer","http://ddx.gubit.cn/new2.html"));
        parameterModeList.add(new SpiderTaskParameterMode("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.164 Safari/537.36"));
        parameterModeList.add(new SpiderTaskParameterMode("X-Requested-With","XMLHttpRequest"));
//        String date = LocalDate.now().toString().replaceAll("-", "");

        LocalDate localDate = LocalDate.of(2021,8,30);
        Boolean f =true;
        while (f){
            String day =  localDate.toString();
            String date =  localDate.toString().replaceAll("-", "");
            String message = httpGet("http://ddx.gubit.cn/ygetnewallddxpm.php?zf=0&ddx=0&ddy=0&pagenum=4500&t=0.8342799674576753&lsdate="+day,parameterModeList);
            JSONObject jsonObject = JSONObject.parseObject(message);
            try {
                JSONArray data = (JSONArray)jsonObject.get("data");
                List<MarketYybDayModel> list =new ArrayList<>();
                Iterator<Object> iterator = data.iterator();
                while (iterator.hasNext()){
                    JSONArray json = (JSONArray) iterator.next();
                    String foxxcode = (String) json.get(0);
                    Double price = Double.valueOf(json.get(1).toString());
                    Double odds = Double.valueOf(json.get(14).toString());
                    MarketYybDayModel marketYybDayModel =new MarketYybDayModel();
                    marketYybDayModel.setFoxxcode(foxxcode);
                    marketYybDayModel.setPrice(price);
                    marketYybDayModel.setOpendate(date);
                    marketYybDayModel.setOdds(odds);
                    list.add(marketYybDayModel);
                }
                String sql = String.format("delete from t_market_yyb_day where opendate = %s",date);
                jdbcTemplate.execute(sql);
                String saveSql = CreatedSqlCommon.initCreatedTable(MarketYybDayModel.class, "t_market_yyb_day");
                namedParameterJdbcTemplate.batchUpdate(saveSql, SqlParameterSourceUtils.createBatch(list));
            }catch (Exception e){
                logger.info("不是开盘日期");
            }
            if(localDate.isEqual(LocalDate.now())){
                f = false;
            }
            localDate = localDate.plusDays(1);
            Thread.sleep(1000);
        }
    }

    public String httpGet(String url,List<SpiderTaskParameterMode> parameterModeList){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权

            for (SpiderTaskParameterMode parameterMode : parameterModeList) {
                httpGet.setHeader(parameterMode.getRawKey(), parameterMode.getRawValue());
            }
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
