package com.lzycug.file;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * description：
 * author：lzyCug
 * date: 2020/11/5 14:30
 */
public class XmlUtil {
    private final static Logger logger = LogManager.getLogger(XmlUtil.class);

    private final static String classBegin = "public class %s {";
    private final static String classEnd = "}";
    private final static String attr = "	private %s %s ;";
    private final static String attrStr = "	private String %s ;";
    private final static String attrList = "	private List<%s> %s ;";
    private final static String wrap = "\r\n";
    private final static String eclipsePath = "src" + File.separator + "main" + File.separator + "java";
    private final static String systemPath = System.getProperty("user.dir");
    private static final String packageInfo = "package ";
    private static final String semicolon = ";";
    private static final String importList = "import java.util.List;";
    private static final String getStrHead = "	public %s get%s() {";
    private static final String getStrMiddle = "        return %s;";
    private static final String getStrEnd = "    }";
    private static final String setStrHead = "    public void set%s(%s %s) {";
    private static final String setStrMiddle = "        this.%s = %s;";
    private static final String setStrEnd = "    }";
    private static final String stringType = "String";
    private static final String listType = "List<%s>";
    private static final String annotation = "	" + XmlConfigUtil.getProperty("annotation") == null ? "" : XmlConfigUtil.getProperty("annotation");
    private static final String annotationRoot = XmlConfigUtil.getProperty("annotation.root");
    private static final String annotationGet = "	" + XmlConfigUtil.getProperty("annotation.get");
    private static final String rootPackage = "import javax.xml.bind.annotation.XmlRootElement;";
    private static final String xmlElementPackage = "import javax.xml.bind.annotation.XmlElement;";

    private static String classFileName = "";
// ur301202
    /**
     * 根据xml文件生成java代码
     *
     * @param path     xml文件所在的全路径
     * @param pageName 生成文件所在的包名
     */
    public static void writeClassByFile(String path, String pageName) {
        SAXReader reader = new SAXReader();
        try {
            File file = new File(path);
            classFileName = file.getName().substring(0, file.getName().length() - 4);

            Document parseText = reader.read(file);
            Element rootElement = parseText.getRootElement();
            writeClass(rootElement, pageName, 0, "first");
        } catch (DocumentException e) {
            e.printStackTrace();
            logger.error("xml文件读取失败!!!");
        }
    }

    /**
     * 根据xml字符串生成java代码
     *
     * @param xmlStr   xml字符串
     * @param pageName 生成文件所在的包名
     */
    public static void writeClassByString(String xmlStr, String pageName) {
        try {
            Document parseText = DocumentHelper.parseText(xmlStr);
            Element rootElement = parseText.getRootElement();
            writeClass(rootElement, pageName, 0, "other");
        } catch (DocumentException e) {
            logger.error("xml字符串读取失败!!!");
        }
    }

    /**
     * 自动生成代码的主方法
     *
     * @param element
     * @param pageName
     */
    private static void writeClass(Element element, String pageName, int i, String first) {
        i++;
        String filePath = systemPath + File.separator + eclipsePath + File.separator
                + pageName.replace(".", File.separator);
        FileUtils.createFileIsNotExists(filePath);
        @SuppressWarnings("unchecked")
        List<Element> elements = element.elements();
        StringBuffer sb = new StringBuffer();
        HashSet<String> set = new HashSet<String>();//每一个模块中的标签名储存的set,判断下一个标签名是否重复
        //String fileAllPath = filePath + File.separator + upperCaseFirst(element.getName()) + ".java";
        String fileAllPath = "";
        if ("first".equals(first)) {
            fileAllPath = filePath + File.separator + classFileName + ".java";
        } else {
            fileAllPath = filePath + File.separator + upperCaseFirst(element.getName()) + ".java";
        }

        if (!elements.isEmpty()) {
            if (i == 1)
                sb = writeClassHeaderRoot(sb, pageName, element, i);
            else
                sb = writeClassHeader(sb, pageName, element, i);
            sb = writeClassMiddle(sb, elements, set);
            set.clear();
            sb = writeClassEnd(sb);
            FileUtils.writeFile(fileAllPath, sb.toString());
            logger.info(sb.toString());
            for (Element ele : elements) {
                if (!set.contains(ele.getName())) {
                    writeClass(ele, pageName, i, "other");
                    set.add(ele.getName());
                }
            }
        }
    }

    private static StringBuffer writeClassHeaderRoot(StringBuffer sb, String pageName, Element element, int i) {
        addClassHeader(sb, pageName, i);
        sb.append(wrap);
        sb.append(String.format(annotationRoot, element.getName()));
        sb.append(wrap);
        //sb.append(String.format(classBegin, upperCaseFirst(element.getName())));
        sb.append(String.format(classBegin, classFileName));
        sb.append(wrap);
        sb.append(wrap);
        return sb;
    }

    /**
     * 写入类的尾信息
     *
     * @param sb
     * @return
     */
    private static StringBuffer writeClassEnd(StringBuffer sb) {
        sb.append(wrap);
        sb.append(classEnd);
        return sb;
    }

    /**
     * 写入类的头信息
     *
     * @param sb
     * @param pageName
     * @param element
     * @return
     */
    private static StringBuffer writeClassHeader(StringBuffer sb, String pageName, Element element, int i) {
        addClassHeader(sb, pageName, i);
        sb.append(String.format(classBegin, upperCaseFirst(element.getName())));
        sb.append(wrap);
        sb.append(wrap);
        return sb;
    }

    private static StringBuffer writeClassMiddle(StringBuffer sb, List<Element> elements, HashSet<String> set) {
        Map<String, String> map = new HashMap<String, String>();
        for (Element ele : elements) {
            @SuppressWarnings("unchecked")
            List<Element> listEle = ele.elements();
            if (listEle.isEmpty()) {
                if (StringUtils.isNotBlank(annotation)) {
                    sb.append(String.format(annotation, ele.getName()));
                    sb.append(wrap);
                }
                sb.append(String.format(attrStr, downCase(ele.getName())));
                map.put(ele.getName(), stringType);
            } else {
                if (set.contains(ele.getName())) {
                    continue;
                }
                int i = 0;//是否是list的标识
                for (Element el : elements) {
                    if (el.getName().equals(ele.getName())) {
                        i++;
                    }
                }
                if (i == 1) {
                    if (StringUtils.isNotBlank(annotation)) {
                        sb.append(String.format(annotation, ele.getName()));
                        sb.append(wrap);
                    }
                    sb.append(String.format(attr, upperCaseFirst(ele.getName()), downCase(ele.getName())));
                    map.put(ele.getName(), upperCaseFirst(ele.getName()));
                } else {
                    if (StringUtils.isNotBlank(annotation)) {
                        sb.append(String.format(annotation, ele.getName()));
                        sb.append(wrap);
                    }
                    sb.append(String.format(attrList, upperCaseFirst(ele.getName()), downCase(ele.getName())));
                    set.add(ele.getName());
                    map.put(ele.getName(), String.format(listType, upperCaseFirst(ele.getName())));
                }

            }
            sb.append(wrap);
        }
        for (String key : map.keySet()) {
            sb = writeGetMethod(map, key, sb);
            sb = writeSetMethod(map, key, sb);
        }
        return sb;
    }

    /**
     * get方法的编写
     *
     * @param map
     * @param key
     * @param sb
     * @return
     */
    private static StringBuffer writeGetMethod(Map<String, String> map, String key, StringBuffer sb) {
        sb.append(wrap);
        sb.append(String.format(annotationGet, key));
        sb.append(wrap);
        sb.append(String.format(getStrHead, map.get(key), downCase(key)));
        sb.append(wrap);
        sb.append(String.format(getStrMiddle, downCase(key)));
        sb.append(wrap);
        sb.append(getStrEnd);
        return sb;
    }

    /**
     * set方法的编写
     *
     * @param map
     * @param key
     * @param sb
     * @return
     */
    private static StringBuffer writeSetMethod(Map<String, String> map, String key, StringBuffer sb) {
        sb.append(wrap);
        sb.append(String.format(setStrHead, downCase(key), map.get(key), downCase(key)));
        sb.append(wrap);
        sb.append(String.format(setStrMiddle, downCase(key), downCase(key)));
        sb.append(wrap);
        sb.append(setStrEnd);
        return sb;
    }

    /**
     * 首字符大写
     *
     * @param str
     * @return
     */
    private static String upperCaseFirst(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 首字符小写
     *
     * @param str
     * @return
     */
    private static String downCaseFirst(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    private static String downCase(String str) {
        String[] strArray = str.split("_");
        int length = strArray.length;
        StringBuilder stringBuilder = new StringBuilder();
        if (length > 1) {
            for (int i = 0; i < length; i++) {
                if (i == 0) {
                    stringBuilder.append(strArray[i].toLowerCase());
                } else {
                    stringBuilder.append(upperCaseFirst(strArray[i].toLowerCase()));
                }
            }
        } else if (length == 1) {
            stringBuilder.append(strArray[0].toLowerCase());
        } else {
            System.out.println(str);
        }
        return stringBuilder.toString();
    }

    /**
     * 添加类中的包信息和导入信息
     *
     * @param sb
     * @param pageName
     * @return
     */
    private static StringBuffer addClassHeader(StringBuffer sb, String pageName, int i) {
        sb.append(packageInfo);
        sb.append(pageName);
        sb.append(semicolon);
        sb.append(wrap);
        sb.append(wrap);
        sb.append(importList);
        sb.append(wrap);
        sb.append(xmlElementPackage);
        if (i == 1) {
            sb.append(wrap);
            sb.append(rootPackage);
        }
        sb.append(wrap);
        sb.append(wrap);
        boolean flag = true;
        if (flag) {
            sb.append("");
            sb.append(wrap);
        }
        return sb;
    }
}
