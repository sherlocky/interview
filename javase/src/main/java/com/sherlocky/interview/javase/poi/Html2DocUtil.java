package com.sherlocky.interview.javase.poi;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.regex.Pattern;

public class Html2DocUtil {
	protected static Log logger = LogFactory.getLog(Html2DocUtil.class);
	
	public static void main(String[] args) {
		Html2DocUtil t = new Html2DocUtil();
//		System.out.println(dealwithHTML("水电费就撒娇的咖啡机撒接口及撒飞机快放假快乐世纪东方卡萨极乐空间<按实际的空间看了>>24234<<"));
		System.out.println(dealwithHTML("水电费就撒娇的咖啡机撒接口及<div><p>123</p>gkjkjk</div>撒飞机快放假快乐世纪东方卡萨"));
		
		try {
			InputStream cssFileInputStream = t.getClass().getResourceAsStream("testTableStyle.css");
			Html2DocUtil.convert(getContent(new FileInputStream("c:\\html2word.html")), cssFileInputStream, "c:\\html2wordPOI.doc");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done!");
	}
	
	 public static void convert(String body, InputStream cssFileInputStream, String outputFile) {
		 String css = StringUtils.EMPTY;
		 if (cssFileInputStream != null) {
			 try {
				css = getContent(cssFileInputStream);
			} catch (Exception e) {
				css = StringUtils.EMPTY;
				logger.error("获取css文件内容失败！", e);
			}
		 }
         //拼一个标准的HTML格式文档
         StringBuffer html = new StringBuffer("<!DOCTYPE html><html><head><style>").append(css).append("</style></head><body>").append(dealwithHTML(body)).append("</body></html>");
         if (logger.isDebugEnabled()) {
        	 logger.debug("### 拼装后的html 代码：");
        	 logger.debug(html);
         }
         InputStream is;
         OutputStream os;
		try {
			is = new ByteArrayInputStream(html.toString().getBytes("UTF8"));
			os = new FileOutputStream(outputFile);
			inputStreamToWord(is, os);
		} catch (IOException e) {
			logger.error("### html 转换成 doc 文件失败！", e);
		}
      }
      
	 /**
	  * 把 input 标签处理掉，保留下其value值
	  * @param content
	  * @return
	  * @author zhangcx
	  * @date 2016年11月15日
	  * @since
	  */
	 private static String dealwithHTML(String content) {
	     Pattern pHtml = Pattern.compile("<(\\S*?) [^>]*>.*?</\\1>|<.*? />");
		 if (StringUtils.isEmpty(content) || !pHtml.matcher(content).find()) {
			 return content;
		 }
		 org.jsoup.nodes.Document html = Jsoup.parse(content);
		 // 处理html代码片段中的<table> 给其加上word识别的border
		 org.jsoup.select.Elements tables = html.getElementsByTag("table");
		 if (tables != null && tables.size() > 0) {
			 for (int i = 0, len = tables.size(); i < len; i++) {
				 tables.get(i).attr("border", "1");
			 }
		 }
		 // 使用jsoup处理html中的 文本框、域
		 org.jsoup.select.Elements inputs = html.getElementsByTag("input");
		 if (inputs != null && inputs.size() > 0) {
			 for (int i = 0, len = inputs.size(); i < len; i++) {
				 String val = inputs.get(i).val();
				 inputs.get(i).parent().html(val);
			 }
		 }
		 org.jsoup.select.Elements textareas = html.getElementsByTag("textarea");
		 if (textareas != null && textareas.size() > 0) {
			 for (int i = 0, len = textareas.size(); i < len; i++) {
				 String text = textareas.get(i).text();
				 textareas.get(i).parent().html(text);
			 }
		 }
		return html.body().html();
	}

      /**
       * 把is写入到对应的word输出流os中
       * 不考虑异常的捕获，直接抛出
       * @param is
       * @param os
       * @throws IOException
       */
      private static void inputStreamToWord(InputStream is, OutputStream os) throws IOException {
         POIFSFileSystem fs = new POIFSFileSystem();
         //对应于org.apache.poi.hdf.extractor.WordDocument
         fs.createDocument(is, "WordDocument");
         fs.writeFilesystem(os);
         os.close();
         is.close();
      }
      
      /**
       * 把输入流里面的内容以UTF-8编码当文本取出。
       * 不考虑异常，直接抛出
       * @param ises
       * @return
       * @throws IOException
       */
      private static String getContent(InputStream... ises) throws IOException {
         if (ises != null) {
            StringBuilder result = new StringBuilder();
            BufferedReader br;
            String line;
            for (InputStream is : ises) {
               br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
               while ((line=br.readLine()) != null) {
                   result.append(line);
               }
            }
            return result.toString();
         }
         return null;
      }
}
