package com.yun.market.task.spider.other;

import com.yun.market.config.HttpConfig;
import com.yun.market.model.FoxxcodeMode;
import com.yun.market.model.quotation.QuotationDayModel;
import com.yun.market.model.spider.SpiderTaskMode;
import com.yun.market.model.spider.SpiderTaskParameterMode;
import com.yun.market.service.foxxcode.FoxxcodeService;
import com.yun.market.service.foxxcode.QuotationDayService;
import com.yun.market.service.sprider.SpriderTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuotationDayMis {
    @Autowired
    SpriderTaskService spriderTaskService;

    @Autowired
    FoxxcodeService foxxcodeService;

    @Autowired
    QuotationDayService quotationDayService;

    @Async
    public void doFoxxCodeMis(SpiderTaskMode spiderNode, List<SpiderTaskParameterMode> parameterModeList, List<FoxxcodeMode> queryFoxxList) {
        if (!spiderNode.getDescribe().contains("爬取历史行情")) {
            return;
        }
        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 20); //标识抓取开始

        String filePath = "C:\\install\\quotation\\";

        final int[] flag = {1};
        //遍历下载所有历史股票数据
        queryFoxxList.stream().parallel().forEach(foxxcodeMode -> {

            System.out.println(String.format("下载股票数据,股票代码：code=%s当前第%s页，占比%s / %s", foxxcodeMode.getFoxxcode(), flag[0], flag[0], queryFoxxList.size()));
            String url = spiderNode.getSpiderHttp();
            String code = foxxcodeMode.getFoxxcode();
            String openDate = DateToStr(new Date());
            openDate = "end="+openDate;
            if (code.startsWith("0") || code.startsWith("3")) {
                code = "code=1" + foxxcodeMode.getFoxxcode();
                url = url.replace("code=0600000", code).replace("end=20210322",openDate);
            } else {
                code = "code=0" + foxxcodeMode.getFoxxcode();
                url = url.replace("code=0600000", code).replace("end=20210322",openDate);
            }

            String html = HttpConfig.downloadHttpUrl(url, filePath, foxxcodeMode.getFoxxcode() + ".csv", parameterModeList);
            flag[0]++;
        });


        List<QuotationDayModel> quotationDayModelList = new LinkedList<>();
        List<String> foxxcodes = new LinkedList<>();
        final int[] flags = {1};
        queryFoxxList.stream().filter(x->x.getStatus()!=80).forEach(foxxcodeMode -> {

            foxxcodes.add(foxxcodeMode.getFoxxcode());
            System.out.println(String.format("读取csv股票数据,股票代码：code=%s当前第%s页，占比%s / %s", foxxcodeMode.getFoxxcode(), flags[0], flags[0], queryFoxxList.size()/20));
            //读取Csv文件
            try {
                BufferedReader reader = new BufferedReader(new FileReader(String.format("%s%s.csv",filePath,foxxcodeMode.getFoxxcode())));//换成你的文件名
                reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉
                String line = null;
                while((line=reader.readLine())!=null){
                    String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分

                    QuotationDayModel quotationDayModel = new QuotationDayModel();
                    quotationDayModel.setFoxxcode(foxxcodeMode.getFoxxcode());
                    quotationDayModel.setOpendate(Integer.parseInt(StrToDate(item[0])));
                    quotationDayModel.setOpen(Double.parseDouble(item[6]));
                    quotationDayModel.setHigh(Double.parseDouble(item[4]));
                    quotationDayModel.setLow(Double.parseDouble(item[5]));
                    quotationDayModel.setClose(Double.parseDouble(item[3]));
                    quotationDayModel.setPre_close(Double.parseDouble(item[7]));
                    quotationDayModel.setRise_amount(item[8]);
                    quotationDayModel.setRise_applies(item[9]);
                    quotationDayModel.setVol(item[11]);
                    quotationDayModel.setAmout(item[12]);
                    quotationDayModel.setSz_value(item[13]);
                    quotationDayModel.setLt_value(item[14]);

                    quotationDayModelList.add(quotationDayModel);
                }
                flags[0]++;

                if (flags[0]==20) {
                    //数据删除
//                    quotationDayService.delQuotationDay(foxxcodeMode.getFoxxcode());
                    String foxx =foxxcodes.stream().map(x->"'"+x+"'").collect(Collectors.joining(","));
                    foxxcodeService.updateFoxxcodeStatus(foxx);

                    //数据保存
                    quotationDayService.saveQuotationDay(quotationDayModelList);
                    quotationDayModelList.clear();
                    foxxcodes.clear();
                    flags[0]=0;
                }

            } catch (Exception e) {
                spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 10); //标识抓取开始
                e.printStackTrace();
            }
        });

        //数据保存
        quotationDayService.saveQuotationDay(quotationDayModelList);
        quotationDayModelList.clear();

        spriderTaskService.updateTask(String.valueOf(spiderNode.getId()), 80); //标识抓取开始
    }

    public String StrToDate(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        format = new SimpleDateFormat("yyyyMMdd");
        String strs = format.format(date);
        return strs;
    }

    public String DateToStr(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String str = format.format(date);
        return str;
    }
}
