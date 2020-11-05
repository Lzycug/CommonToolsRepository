package com.lzycug.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class FileUtils {

    private final static Logger logger = LogManager.getLogger(FileUtils.class);

    public static void main(String[] args) {
        writeFile("F:\\echase\\JNRM\\AutoPayRequest.xml", "123123123");

    }

    /**
     * 读取文件转换为指定字符集的字符串
     *
     * @param path     文件路径
     * @param encoding 文件字符集
     */
    public static String getXmlStr(String path, String encoding) {
        FileInputStream inputStream = null;
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                inputStream = new FileInputStream(file);
                String lineTxt;
                read = new InputStreamReader(inputStream, encoding);
                bufferedReader = new BufferedReader(read);
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt);
                }
            } else {
                logger.error("the file isn't exists");
                return StringUtils.EMPTY;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != read) {
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 读取xml文件 默认系统编码
     *
     * @param path
     * @return
     */
    public static String getXmlStr(String path) {
        StringBuilder sb = new StringBuilder();
        FileInputStream fileInputStream = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            File file = new File(path);
            fileInputStream = new FileInputStream(file);
            reader = new InputStreamReader(fileInputStream);
            br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fileInputStream) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 追加写入文件
     *
     * @param path    文件路径
     * @param contect 追加写入内容
     */
    public static void writeFileAddEnd(String path, String contect) {
        FileWriter fileWriter = null;
        try {
            File file = new File(path);
            fileWriter = new FileWriter(file, true);
            fileWriter.write(contect);
        } catch (IOException e) {
            logger.error("写入文件错误:", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    logger.error("关闭写入流错误:", e);
                }
            }
        }
    }

    /**
     * 写入文件，不追加，直接覆盖原文件
     *
     * @param path    文件路径
     * @param contect 重新写入文件内容
     */
    public static void writeFile(String path, String contect) {
        FileWriter fileWriter = null;
        try {
            File file = new File(path);
            fileWriter = new FileWriter(file);
            fileWriter.write(contect);
        } catch (IOException e) {
            logger.error("写入文件错误:", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    logger.error("关闭写入流错误:", e);
                }
            }
        }
    }

    /**
     * 创建文件夹(如果不存在)
     *
     * @param filePath 文件路径
     */
    public static void createFileIsNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private FileUtils() {
    }
}
