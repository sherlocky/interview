package com.sherlocky.interview.javase.zip;

import com.sherlocky.common.util.HttpFileNameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * 打包下载文件夹为zip包 测试代码
 * @author zhangcx
 * @date 2018-04-04
 */
public class DownloadDir2ZipTest {
    private static Log log = LogFactory.getLog(DownloadDir2ZipTest.class);

    public static void downloadDir(HttpServletRequest request, HttpServletResponse response, Map<String, List<String>> fileInfo, String downloadFileName) {
        if (fileInfo == null) {
            return;
        }
        // 文件实体 绝对路径
        List<String> filePaths = fileInfo.get("filePaths");
        // zip打包时的文件路径+文件名；长度必须和 filePaths一致
        List<String> fileEntrys = fileInfo.get("fileEntrys");
        // 空目录
        List<String> folderEntrys = fileInfo.get("folderEntrys");
        InputStream in = null;
        ZipOutputStream out = null;
        try {
            byte[] buf = new byte[1024];
            response.setContentType("application/octet-stream");// 均不提供直接打开
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + HttpFileNameUtils.convert(request, downloadFileName + ".zip"));
            //长度屏蔽掉
            out = new ZipOutputStream(response.getOutputStream());
            out.setEncoding("GBK");
            boolean isAllNotExist = true;
            if (filePaths != null && filePaths.size() > 0) {
                for (int i = 0; i < filePaths.size(); i++) {
                    File file = new File(filePaths.get(i));
                    if (file.exists()) {
                        isAllNotExist = false;
                        in = new FileInputStream(file);
                        out.putNextEntry(new ZipEntry(fileEntrys.get(i)));//每个文件的压缩路径(目录结构+文件名)
                        int temp = 0;
                        while ((temp = in.read(buf)) != -1) {
                            out.write(buf, 0, temp);
                        }
                        out.flush();
                    }
                }
            }
            if (folderEntrys != null && folderEntrys.size() > 0) {//写出空目录
                for (String entry : folderEntrys) {
                    if (StringUtils.isNotBlank(entry)) {
                        if (!entry.endsWith(File.separator)) {
                            entry = entry + File.separator;
                        }
                        out.putNextEntry(new ZipEntry(entry));
                    }
                }
            }
            if (isAllNotExist) {
                out.setComment("您所要下载的文件都不存在，如急需使用请联系管理员！");
            }
        } catch (Exception e) {
            log.error("读取文件列表报错", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                    log.error("关闭文件流出错", ex);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception ex) {
                    log.error("关闭输出流出错", ex);
                }
            }
        }
    }

    /**
     * 打包下载文件
     * 文件地址为网络流格式文件（http://xxx）
     *
     * @param request
     * @param response
     * @param fileInfo         文件目录信息
     * @param downloadFileName
     */
    public static void downloadDirByURL(HttpServletRequest request, HttpServletResponse response,
                                        Map<String, List<String>> fileInfo, String downloadFileName) {

        List<String> filePaths = fileInfo.get("filePaths");
        List<String> fileEntrys = fileInfo.get("fileEntrys");
        List<String> folderEntrys = fileInfo.get("folderEntrys");

        InputStream in = null;
        ZipOutputStream out = null;
        try {
            byte[] buf = new byte[1024];
            response.setContentType("application/octet-stream");// 均不提供直接打开
            response.addHeader("Content-Disposition",
                    "attachment;filename=" + HttpFileNameUtils.convert(request, downloadFileName + ".zip"));
            // 长度屏蔽掉
            out = new ZipOutputStream(response.getOutputStream());
            out.setEncoding("GBK");
            if (null != filePaths && filePaths.size() > 0) {
                for (int i = 0; i < filePaths.size(); i++) {
                    URL url = new URL(filePaths.get(i));
                    URLConnection openConnection = url.openConnection();
                    openConnection.setDoInput(true);
                    in = openConnection.getInputStream();
                    int temp = 0;
                    out.putNextEntry(new ZipEntry(fileEntrys.get(i)));
                    while ((temp = in.read(buf)) != -1) {
                        out.write(buf, 0, temp);
                    }
                    out.flush();
                }
            }
            // 写出空目录
            if (folderEntrys != null && folderEntrys.size() > 0) {
                for (String entry : folderEntrys) {
                    if (StringUtils.isNotBlank(entry)) {
                        if (!entry.endsWith(File.separator)) {
                            entry = entry + File.separator;
                        }
                        out.putNextEntry(new ZipEntry(entry));
                    }
                }
            }
        } catch (IOException e) {
            log.error("读取文件列表报错", e);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }

    }
}