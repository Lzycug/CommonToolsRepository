package com.lzycug.picture;

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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BiaoQingCrawl implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(BiaoQingCrawl.class);

    private static final String SAVE_FILE = "F:\\test1\\";

    private String url;

    public BiaoQingCrawl(String url) {
        this.url = url;
    }

    /**
     * 根据网站的网址获取表情的分类信息
     *
     * @param url 需要爬取的资源网站
     * @return Map集合的key对应表情的系列名称，value对应的表情包的访问地址
     */
    private static Optional<Map<String, String>> getClassifyAndUrl(String url) {
        System.out.println("页面访问链接：" + url);
        if (StringUtils.isEmpty(url)) {
            LOGGER.error("url is empty");
            return Optional.empty();
        }
        // 创建连接
        Connection connection = Jsoup.connect(url);
        Map<String, String> map = new HashMap<>();
        try {
            // 获取页面document
            Document document = connection.get();
            Elements bqBlock = document.select(".bqblock");
            for (Element element : bqBlock) {
                Elements down = element.select(".down a");
                String text = down.attr("title");
                String attr = down.attr("href");
                System.out.println("获取到表情包信息：" + text + "--------" + attr);
                map.put(text, attr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(map);
    }

    /**
     * 根据集合中每个表情包信息获取该类表情包的下载信息
     *
     * @param map 表情包信息集合
     * @return key对应表情包名称，value对应该包下的下载链接集合
     */
    private static Optional<Map<String, List<String>>> getDownloadUrl(Map<String, String> map) {
        if (CollectionUtils.isEmpty(map)) {
            LOGGER.error("map is empty");
            return Optional.empty();
        }
        Map<String, List<String>> listMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            List<String> list = new ArrayList<>();
            String url = entry.getValue();
            Connection connect = Jsoup.connect(url);
            try {
                Thread.sleep(100);
                Document document = connect.get();
                Elements down_img = document.select(".down_img");
                Elements div = down_img.select("div");
                Element element = div.get(3);
                Elements img = element.select("img");
                for (Element image : img) {
                    String src = image.attr("src");
                    list.add(src);
                    System.out.println("表情包下载地址：" + src);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            listMap.put(entry.getKey(), list);
        }
        return Optional.ofNullable(listMap);
    }

    private static void downloadImg(Map<String, List<String>> listMap) {
        for (Map.Entry<String, List<String>> entry : listMap.entrySet()) {
            int num = 0;
            File file = new File(SAVE_FILE, entry.getKey());
            if (!file.exists()) {
                file.mkdir();
            }
            for (String url : entry.getValue()) {
                System.out.println(Thread.currentThread().getName() + "开始下载表情包" + url);
                writeSource(file, url, ++num);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeSource(File fileDir, String sourceUrl, int num) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            HttpGet httpGet = new HttpGet(sourceUrl);
            httpGet.setHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
            CloseableHttpResponse response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();
            File file = new File(fileDir, num + ".gif");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            int len;
            byte[] bytes = new byte[1024 * 8];
            while ((len = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, len);
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        Optional<Map<String, String>> classifyAndUrl = getClassifyAndUrl(url);
        if (!classifyAndUrl.isPresent()) {
            LOGGER.error("classifyAndUrl is empty");
            return;
        }
        Optional<Map<String, List<String>>> downloadUrl = getDownloadUrl(classifyAndUrl.get());
        if (!downloadUrl.isPresent()) {
            LOGGER.error("downloadUrl is empty");
            return;
        }
        downloadImg(downloadUrl.get());
    }
}

class BiaoQingCrawlThread {
    /**
     * 表情素材网站
     */
    private static final String PREFIX_URL = "http://sc.chinaz.com/biaoqing/";

    /**
     * 网页拼接串
     */
    private static final String CONNECT_STR = "index_";

    /**
     * 网页后缀
     */
    private static final String SUFFIX_URL = ".html";

    /**
     * 固定线程池线程数量
     */
    private static final int THREAD_POLL_COUNT = 5;

    /**
     * 声音资源需要爬取的页数
     */
    private static final int PAGE_COUNT = 10;

    public static void main(String[] args) {
        String url = StringUtils.EMPTY;
        for (int i = 1; i <= PAGE_COUNT; i++) {
            if (i == 1) {
                url = PREFIX_URL;
            } else {
                url = PREFIX_URL + CONNECT_STR + i + SUFFIX_URL;
            }
            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POLL_COUNT);
            executorService.execute(new BiaoQingCrawl(url));
//            new BiaoQingCrawl(url).run();
        }
    }
}
