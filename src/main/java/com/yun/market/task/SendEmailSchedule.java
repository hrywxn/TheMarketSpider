package com.yun.market.task;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import gui.ava.html.image.generator.HtmlImageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class SendEmailSchedule {


    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        LocalDate now = LocalDate.now();
        System.out.println(now.toString().replaceAll("-", ""));
//        MailUtil.send("547555539@qq.com", "Wealth password", "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <html lang=\"en\"> <head> <meta charset=\"UTF-8\"> <title>邮件提醒</title> 　　 <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/> <!--<script type=\"text/javascript\">--> <!--// var para = function () {--> <!--// }--> <!--var url = document.location.toString();--> <!--var arrUrl = url.split(\"?\");--> <!--var para = arrUrl[1];--> <!--alert('===='+url);--> <!--</script>--> </head> <body style=\"margin: 0; padding: 0;\"> <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;\"> 　 <tr> <td> <div style=\"margin: 20px;text-align: center;margin-top: 50px\"> <img src=\"https://cxbres.oss-cn-beijing.aliyuncs.com/CTSP/mail/header/20180904115147.png\" border=\"0\" style=\"display:block;width: 100%;height: 100%\"> </div> </td> </tr> <tr> <td> <div style=\"border: #36649d 1px dashed;margin: 30px;padding: 20px\"> <label style=\"font-size: 22px;color: #36649d;font-weight: bold\">恭喜您，注册成功！</label> <p style=\"font-size: 16px\">亲爱的&nbsp;<label style=\"font-weight: bold\"> XXX先生/女士</label>&nbsp; 您好！欢迎加入xxx。 </p> <p style=\"font-size: 16px\">您已于2018年10月1日充值成功，希望更好的为您服务！</p> </div> </td> </tr> 　 <tr> <td> <div style=\"margin: 40px\"> <p style=\"font-size: 16px\">xxx管理团队</p> <p style=\"color:red;font-size: 14px \">（这是一封自动发送的邮件，请勿回复。）</p> </div> </td> </tr> <!--<tr>--> <!--<td>--> <!--<div style=\"font-size:14px;margin-left: 30px;margin-right: 30px;padding: 20px\">--> <!--<img src=\"tile-wide.png\" alt=\"\" style=\"width: 100px;height: 100px\"/>--> <!--<label style=\"font-size: 12px;display:block;\"> 关注公众号了解更多</label>--> <!--&lt;!&ndash;<p style=\"font-size: 12px\">&ndash;&gt;--> <!--&lt;!&ndash;如有任何问题，可以与我们联系，我们将尽快为你解答。<br/>&ndash;&gt;--> <!--&lt;!&ndash;电话：4008-622-333&ndash;&gt;--> <!--&lt;!&ndash;</p>&ndash;&gt;--> <!--</div>--> <!--</td>--> <!--</tr>--> <tr> <td> <div align=\"right\" style=\"margin: 40px;border-top: solid 1px gray\" id=\"bottomTime\"> <p style=\"margin-right: 20px\">xxx科技有限公司</p> <label style=\"margin-right: 20px\">2018年08月30日</label> </div> </td> </tr> </table> </body> </html>", true);
    }

    //        @Scheduled(cron = "0 0 8 * * *")
//    @Scheduled(cron = "0 0/1 * * * ?")
    @PostConstruct
    public void SendEmail() throws FileNotFoundException {
        // 获取当前时间
        String date = LocalDate.now().toString().replaceAll("-", "");
//        String date = LocalDate.of(2021,8,31).toString().replaceAll("-", "");

        // 查询抄底
        List<Map<String, Object>> cddata = queryDb("SELECT\n" +
                "        sum(buyamount) AS buyamount,\n" +
                "        SUM(sellamount) AS sellamount,\n" +
                "        SUM(buyamount - sellamount) AS jmeamount,\n" +
                "        GROUP_CONCAT(DISTINCT(`price`)) AS price,\n" +
                "        foxxcode,\n" +
                "        GROUP_CONCAT(DISTINCT(`name`)) AS name,\n" +
                "\t\t\t\tGROUP_CONCAT(DISTINCT(`com`)) AS com,\n" +
                "        opendate\n" +
                "FROM\n" +
                "        t_market_yyb\n" +
                "WHERE\n" +
                "        opendate = '" + date + "' and ratio<0  \n" +
                "GROUP BY\n" +
                "        foxxcode\n" +
                "ORDER BY\n" +
                "        jmeamount DESC\n" +
                "LIMIT 10");
        // 查询打板
        List<Map<String, Object>> dbdata = queryDb("SELECT\n" +
                "        sum(buyamount) AS buyamount,\n" +
                "        SUM(sellamount) AS sellamount,\n" +
                "        SUM(buyamount - sellamount) AS jmeamount,\n" +
                "        GROUP_CONCAT(DISTINCT(`foxxcode`)) AS foxxcode,\n" +
                "        GROUP_CONCAT(DISTINCT(`name`)) AS NAMES,\n" +
                "        com,\n" +
                "        opendate\n" +
                "FROM\n" +
                "        t_market_yyb\n" +
                "WHERE\n" +
                "        opendate = '" + date + "' \n" +
                "GROUP BY\n" +
                "        com\n" +
                "ORDER BY\n" +
                "        jmeamount DESC\n" +
                "LIMIT 10");
        StringBuffer stringBuffer = htmlTemplate(cddata, dbdata);

//        html2Image(stringBuffer.toString(),"/Users/cxf/Downloads/TheMarketSpider/src/main/resources/photo/抄底.png");

        FileReader fileReader = new FileReader("email.txt");
        List<String> list = fileReader.readLines();
        MailUtil.send(list, "Wealth password", stringBuffer.toString(), true);
        System.out.println("发送成功");
//        File cdfile = makeExcel(cddata, date + "抄底.xlsx");
//        File dbfile = makeExcel(dbdata, date + "打板.xlsx");
////        ArrayList<String> tos = CollUtil.newArrayList(
////                "793161464@qq.com");

//        MailUtil.send(list, "Wealth password", "邮件来自明日之星", false, cdfile, dbfile);
    }

    public void html2Image(String htmlText,String saveLocalFile){
        HtmlImageGenerator imageGenerator =new HtmlImageGenerator();
        try {
            imageGenerator.loadHtml(htmlText);
            Thread.sleep(100);
            imageGenerator.getBufferedImage();
            Thread.sleep(100);
            imageGenerator.saveAsImage(saveLocalFile);
        }catch (Exception e){

        }
    }


    public StringBuffer htmlTemplate(List<Map<String, Object>> data,List<Map<String, Object>> dbdata) {
        StringBuffer tem = new StringBuffer();
        tem.append("<!DOCTYPE html>");
        tem.append("<html>");
        tem.append("<style type=\"text/css\">\n" +
                "\ttd{\n" +
                "\t\ttext-align:center;\n" +
                "\t\t/* width: 250px; */\n" +
                "\t\tword-break: break-all;\n" +
                "\t}\n" +
                "\ta{\n" +
                "\t\ttext-decoration: none;\n" +
                "\t}\n" +
                "\timg{\n" +
                "\t\twidth: 25px;height:25px;\n" +
                "\t\tborder-radius: 50%;\n" +
                "\t\talign=\"left\";\n" +
                "\t}" +
                "</style>");
        tem.append("<head>\n" +
                "\t<meta charset=\"utf-8\">\n" +
                "\t<title>demo</title>\n" +
                "</head>");
        tem.append("<body>\n" +
                "<!-- <strong></strong>\n" +
                "<em></em>\n" +
                "<pre></pre>格式化原样式输出 -->\n" +
                "<br><br>");
        tem.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\" align=\"center\" width=\"90%\">\n" +
                "\t<caption><strong>抄底</strong></caption>\n" +
                "\t<!-- <colgroup span=\"1\" bgcolor=\"lightgreen\"></colgroup>\n" +
                "\t<colgroup span=\"1\" bgcolor=\"lightyellow\"></colgroup>\n" +
                "\t<colgroup span=\"2\" bgcolor=\"lightgreen\">\n" +
                "\t\t<col bgcolor=\"lightgray\">\n" +
                "\t\t<col>\n" +
                "\t</colgroup>\n" +
                "\t<colgroup span=\"1\" bgcolor=\"cyan\"></colgroup> -->\n" +
                "\t<!-- thead,tbody,tfoot表格的分区 -->\n" +
                "\t<tr bgcolor=\"skyblue\">\n");
        for (String key : data.get(0).keySet()) {
            tem.append("<th>" + key + "</th>");
        }
        tem.append("</tr>\n");
        data.forEach(x -> {
            tem.append("\t<tr>\n");
            for (Map.Entry<String, Object> entry : x.entrySet()) {
                tem.append("<td>" + entry.getValue() + "</td>");
            }
            tem.append("\t</tr>\n");
        });
        tem.append("</table>\n");

        tem.append("<hr>");

        tem.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\" align=\"center\" width=\"90%\">\n" +
                "\t<caption><strong>打板</strong></caption>\n" +
                "\t<!-- <colgroup span=\"1\" bgcolor=\"lightgreen\"></colgroup>\n" +
                "\t<colgroup span=\"1\" bgcolor=\"lightyellow\"></colgroup>\n" +
                "\t<colgroup span=\"2\" bgcolor=\"lightgreen\">\n" +
                "\t\t<col bgcolor=\"lightgray\">\n" +
                "\t\t<col>\n" +
                "\t</colgroup>\n" +
                "\t<colgroup span=\"1\" bgcolor=\"cyan\"></colgroup> -->\n" +
                "\t<!-- thead,tbody,tfoot表格的分区 -->\n" +
                "\t<tr bgcolor=\"skyblue\">\n");
        for (String key : dbdata.get(0).keySet()) {
            tem.append("<th>" + key + "</th>");
        }
        tem.append("</tr>\n");
        dbdata.forEach(x -> {
            tem.append("\t<tr>\n");
            for (Map.Entry<String, Object> entry : x.entrySet()) {
                tem.append("<td>" + entry.getValue() + "</td>");
            }
            tem.append("\t</tr>\n");
        });
        tem.append("</table>\n");

        tem.append("</body>\n" +
                "</html>");
        return tem;
    }


    private List<Map<String, Object>> queryDb(String sql) {
        List<Map<String, Object>> datas = jdbcTemplate.queryForList(sql);
        return datas;
    }

    private File makeExcel(List<Map<String, Object>> data, String name) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.passCurrentRow();

        //合并单元格后的标题行，使用默认标题样式
        writer.merge(data.size() - 1, "测试标题");
        //一次性写出内容，强制输出标题
        writer.write(data, true);
        //out为OutputStream，需要写出到的目标流
        File fileOutputStream = new File("./email\\" + name);
        writer.flush(fileOutputStream);
        //关闭writer，释放内存
        writer.close();
        return fileOutputStream;
    }

}
