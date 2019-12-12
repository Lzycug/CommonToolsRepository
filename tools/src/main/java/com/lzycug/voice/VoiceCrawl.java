
package com.lzycug.voice;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述
 *
 * @author lzycug
 * @since 2019-11-29
 */
class VoiceCrawl implements Runnable {

    private static final Logger LOG = LogManager.getLogger(VoiceCrawl.class);

    /**
     * 文件后缀
     */
    private static final String MP3 = ".mp3";

    /**
     * 休眠时间
     */
    private static final int SLEEP_TIME = 1000;

    /**
     * 相应成功状态码
     */
    private static final int HTTP_OK = 200;

    /**
     * 缓存区大小
     */
    private static final int BUFF = 1024 * 8;

    /**
     * 字节读取返回值为空返回数字
     */
    private static final int END = -1;

    /**
     * 文件存储路径
     */
    private static final String FILE_PATH = "F:\\JavaWork\\resources\\voice";

    private String url;

    VoiceCrawl(String url) {
        this.url = url;
    }

    /**
     * 获取声音资源的下载链接
     *
     * @return key为声音名称，value为声音的下载链接
     */
    private Map<String, String> getDownloadUrl() {
        System.out.println(url + "页面资源开始爬取------" + Thread.currentThread().getName());
        Connection connect = Jsoup.connect(url);
        try {
            Document document = connect.get();
            Elements elements = document.select(".list_sound.mb_xl.cwj");
            Elements audio = elements.select("audio");
            Elements li = elements.select("li");
            Map<String, String> downloadUrlMap = new HashMap<String, String>();
            if (audio.size() != li.size()) {
                LOG.error("Element lengths are inconsistent");
                return downloadUrlMap;
            }
            for (int i = 0; i < audio.size(); i++) {
                String key = li.get(i).select("a").text().replace("下载", StringUtils.EMPTY);
                String value = audio.get(i).attr("src");
                System.out.println("获取到" + key + "-------------资源下载链接" + value + Thread.currentThread().getName());
                downloadUrlMap.put(key, value);
            }
            return downloadUrlMap;
        } catch (IOException e) {
            e.getMessage();
            return Collections.EMPTY_MAP;
        }
    }

    private void downloadByUrl(Map<String, String> downloadUrl) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        File parent = new File(FILE_PATH);
        if (!parent.exists()) {
            parent.mkdir();
        }
        for (Map.Entry<String, String> entry : downloadUrl.entrySet()) {
            String voiceUrl = entry.getValue();
            HttpGet httpGet = new HttpGet(voiceUrl);
            httpGet.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
            OutputStream fos = null;
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HTTP_OK) {
                    InputStream inputStream = response.getEntity().getContent();
                    File file = new File(parent, entry.getKey() + MP3);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    byte[] bytes = new byte[BUFF];
                    int len;
                    while ((len = inputStream.read(bytes)) != END) {
                        fos.write(bytes, 0, len);
                    }
                    System.out.println(entry.getKey() + "写入成功" + Thread.currentThread().getName());
                }
            } catch (IOException e) {
                e.getMessage();
            } finally {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.getMessage();
                }
            }
            try {
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
    }

    private void downloadFile() {
        Map<String, String> downloadUrl = getDownloadUrl();
        downloadByUrl(downloadUrl);
    }

    public void run() {
        downloadFile();
    }
}
