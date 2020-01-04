/*
 * Copyright (c) Lizhiyang  xi'an China. 2019-2019. All rights reserved.
 */

package com.lzycug.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ObjectUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩和解压工具类（zip格式）
 *
 * @author lzycug
 * @since 2019-12-30
 */
public class ZipUtil {
    private static final Logger LOGGER = LogManager.getLogger(ZipUtil.class);

    /**
     * 解压的最大文件，100MB
     */
    private static final int ERROR_TOO_BIG = 0x6400000;

    /**
     * 最大文件数，1024个文件
     */
    private static final int ERROR_TOO_MANY = 1024;

    /**
     * 最大缓存，1MB
     */
    private static final int BUFFER_MAX_SIZE = 1024;

    /**
     * 压缩指定文件为zip格式
     *
     * @param srcPath 需要压缩的文件全路径
     * @param destPath 压缩后文件的全路径
     */
    public static void zip(String srcPath, String destPath) {
        // 校验是否为空
        if (ObjectUtils.isEmpty(srcPath)) {
            LOGGER.error("The filePath is null");
            throw new NullPointerException("The srcPath is null");
        }
        // 校验是否存在此路径
        File file = new File(srcPath);
        if (!file.exists()) {
            LOGGER.error("The filePath is not exist");
            throw new RuntimeException("The srcPath is not exist");
        }
        // 防止多线程写入文件导致异常
        try (FileOutputStream fileOutputStream = new FileOutputStream(destPath);
            FileChannel fileChannel = fileOutputStream.getChannel();
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream zipOutputStream = new ZipOutputStream(cos)) {
            fileChannel.lock();
            anyToZip(StringUtils.EMPTY, file, zipOutputStream);
        } catch (Throwable e) {
            LOGGER.error("Fail to zip file");
            throw new RuntimeException("Fail to zip file", e);
        }
    }

    private static void fileToZip(String rootPath, File file, ZipOutputStream zipOutputStream) {
        if (!file.exists()) {
            return;
        }
        try (FileInputStream fileInputStream = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
            ZipEntry zipEntry = new ZipEntry(rootPath + file.getName());
            zipOutputStream.putNextEntry(zipEntry);
            int count;
            byte[] data = new byte[BUFFER_MAX_SIZE];
            while ((count = bufferedInputStream.read(data, 0, BUFFER_MAX_SIZE)) != -1) {
                zipOutputStream.write(data, 0, count);
            }
        } catch (IOException e) {
            LOGGER.error("Handler the some file fail");
            throw new RuntimeException("Handler the some file fail.", e);
        }
    }

    private static void directoryToZip(String rootPath, File directory, ZipOutputStream zipOutputStream) {
        if (!directory.exists()) {
            return;
        }
        File[] allFiles = directory.listFiles();
        if (ObjectUtils.isEmpty(allFiles)) {
            return;
        }
        for (File file : allFiles) {
            anyToZip(rootPath + directory.getName() + File.separator, file, zipOutputStream);
        }
    }

    private static void anyToZip(String rootPath, File file, ZipOutputStream zipOutputStream) {
        if (file.isDirectory()) {
            directoryToZip(rootPath, file, zipOutputStream);
        } else {
            fileToZip(rootPath, file, zipOutputStream);
        }
    }

    private static File getFileName(String entryName, String destDir) throws IOException {
        File file = new File(destDir, entryName);
        String canonicalPath = file.getCanonicalPath();
        File destFile = new File(destDir);
        String canonicalId = destFile.getCanonicalPath();
        if (!canonicalPath.startsWith(canonicalId)) {
            LOGGER.error("File is outside extraction target directory");
            throw new IllegalStateException("File is outside extraction target directory.");
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            LOGGER.error("Fail to create target directory");
            throw new RuntimeException("Fail to create target directory.");
        }
        return file;
    }

    private static void zipToFile(ZipInputStream zipInputStream, File file, int[] total) {
        // 防止多线程写入文件导致异常
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
            FileChannel fileChannel = fileOutputStream.getChannel();
            BufferedOutputStream out = new BufferedOutputStream(fileOutputStream, BUFFER_MAX_SIZE)) {
            fileChannel.lock();
            byte[] data = new byte[BUFFER_MAX_SIZE];
            int count;
            while ((count = zipInputStream.read(data, 0, BUFFER_MAX_SIZE)) != -1) {
                total[0] += count;
                if (total[0] > ERROR_TOO_BIG) {
                    throw new RuntimeException("The zip is so big");
                }
                out.write(data, 0, count);
            }
        } catch (IOException e) {
            LOGGER.error("Fail to unzip some file");
            throw new RuntimeException("Fail to unzip some file", e);
        }
    }

    /**
     * 解压zip格式文件
     *
     * @param srcPath 需要解压的zip文件全路径
     * @param destDir 解压文件存放路径
     */
    public static void unzip(String srcPath, String destDir) {
        // 校验是否为空
        if (ObjectUtils.isEmpty(srcPath)) {
            throw new NullPointerException("The srcPath is null");
        }
        // 校验是否存在此路径
        File file = new File(srcPath);
        if (!file.exists()) {
            LOGGER.error("The filePath is not exist");
            throw new RuntimeException("The srcPath is not exist");
        }
        int entries = 0;
        int[] total = {0};
        try (FileInputStream fileInputStream = new FileInputStream(file);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                File destFile = getFileName(entry.getName(), destDir);
                zipToFile(zipInputStream, destFile, total);
                entries++;
                if (entries > ERROR_TOO_MANY) {
                    LOGGER.error("The zip have too many file");
                    throw new RuntimeException("The zip have too many file");
                }
            }
        } catch (IOException e) {
            LOGGER.error("Fail to unzip file");
            throw new RuntimeException("Fail to unzip file", e);
        }
    }
}