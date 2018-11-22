package com.sherlocky.interview.javase.itext.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 测试itext套打pdf（需要pdf带有占位符，可用 Adobe Acrobat DC 编辑实现）
 *
 * @author Sherlocky
 * @date 2016年9月8日
 */
public class ItextTemplatePrintPdf {
    public static String BASE_DIR = "F:/dev_tmp/templete_print/";

    public static void main(String[] args) throws IOException {
        String[] files = new String[5];
        for (int i = 0; i < 5; i++) {
            files[i] = "语文" + i + ".pdf";
            replace("1语文_form.pdf", files[i]);
            files[i] = BASE_DIR + files[i];
        }
        mergePdfFiles(files, BASE_DIR + "语文_merge.pdf");
    }

    public static void replace(String src, String dest) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("grade", "2016");
        map.put("clazz", "2");
        map.put("name", "杨栋栋");
        map.put("subject", "生物");

        map.put("ele_1_1", "1");
        map.put("ele_2_1", "2");
        map.put("ele_3_1", "3");
        map.put("ele_1_2", "2");
        map.put("ele_2_2", "3");
        map.put("ele_3_2", "4");
        map.put("ele_4_1", "2");
        map.put("ele_5_1", "3");
        map.put("ele_6_1", "9");
        map.put("ele_7_1", "12");
        map.put("remark", "   fuck you~ ");
        map.put("star", "★★★★★★★");
        replacePdfFiled(map, src, dest);
        System.out.println("生成完成！");
    }

    private static void replacePdfFiled(Map<String, String> map, String src, String dest) {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        try {
            String pdfTemplate = BASE_DIR + src;
            PdfReader reader = new PdfReader(pdfTemplate);
            PdfStamper stamper = new PdfStamper(reader, ba);

            /*
             * Image img = Image.getInstance("/Users/me/Documents/顺丰速递快递单.jpg");
             * img.setAbsolutePosition(0,0);
             * img.scaleToFit(reader.getPageSize(1));
             */
            //PdfContentByte under = stamper.getUnderContent(1);
            // under.addImage(img,img.getScaledWidth(),0,0,img.getScaledHeight(),0,0);
            // under.addImage(img);

            BaseFont bf = null; // BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font = null;
            try {
                bf = BaseFont.createFont("simsun.ttc,1", // 注意这里有一个,1
                        BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                font = new Font(bf, 12, 1);
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
            }

            AcroFields form = stamper.getAcroFields();
            form.addSubstitutionFont(bf);
            // form.setFieldProperty("companyOfSender", "textfont", bf, null);
            // form.setField("companyOfSender","上海XXX有限公司");
            // form.setField("sender", "张XX");
            // form.setField("addressOfSender", "上海市XX区XX路XX号XX大厦XX楼");
            // form.setField("phoneOfSender", "13800210021");
            // form.setField("companyOfReceiver", "xxxxxxxxxx");
            // form.setField("receiver", "李XX");
            // form.setField("province", "上海");
            // form.setField("city", "上海");
            // form.setField("district", "徐汇");
            // form.setField("road", "虹漕路");
            // form.setField("room", "XXX号XXX室");
            // form.setField("phoneOfReceiver", "13999999999");

            for (Entry<String, String> en : (Set<Entry<String, String>>) map.entrySet()) {
                form.setField(en.getKey(), en.getValue());
            }
            //form.setField("$1$", "13999999999");
            //System.out.println(form.getFields());
            stamper.setFormFlattening(true);

            stamper.close();
            ba.writeTo(new FileOutputStream(BASE_DIR + dest));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            try {
                ba.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void mergePdfFiles(String[] files, String savepath) {
        try {
            Document document = new Document(new PdfReader(files[0]).getPageSize(1));

            PdfCopy copy = new PdfCopy(document, new FileOutputStream(savepath));

            document.open();

            for (int i = 0; i < files.length; i++) {
                PdfReader reader = new PdfReader(files[i]);

                int n = reader.getNumberOfPages();

                for (int j = 1; j <= n; j++) {
                    document.newPage();
                    PdfImportedPage page = copy.getImportedPage(reader, j);
                    copy.addPage(page);
                }
            }

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        System.out.println("合成完毕！");
    }
}
