/*
 * Copyright (c) Lizhiyang  xi'an China. 2019-2019. All rights reserved.
 */

package com.lzycug.voice;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能描述
 *
 * @author lzycug
 * @since 2019-11-09
 */
public class voiceCrawThread {

    /**
     * 声音素材网站
     */
    private static final String PREFIX_URL = "http://www.smzy.com/smzy/dongwu-yx-711-";

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

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POLL_COUNT);
        for (int i = 1; i <= PAGE_COUNT; i++) {
            // 拼装请求网址
            String url = PREFIX_URL + i + SUFFIX_URL;
            VoiceCrawl animalVoiceCraw = new VoiceCrawl(url);
            executorService.execute(animalVoiceCraw);
        }
    }
}
