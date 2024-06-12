package com.bwie.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

/**
 * 小马哥（Monte）
 */
public class FreemarkerUtil {

    /**
     *
     * @param conHashMap 模板参数
     * @param templateName 模板名
     * @param outputFilePath 输出路径+名称+后缀
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    public String createHtml(HashMap<String, Object> conHashMap, String templateName, String outputFilePath) throws IOException, TemplateException {

        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());

        // 获取resources路径
        String classpath = this.getClass().getResource("/").getPath();

        // 配置模板路径
        configuration.setDirectoryForTemplateLoading(new File(classpath + "/template/"));

        // 获取模板
        Template template = configuration.getTemplate(templateName+".ftl");

        // 给模板设置值
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, conHashMap);

        InputStream inputStream = IOUtils.toInputStream(content);

        //输出文件
        FileOutputStream fileOutputStream = new FileOutputStream(
                new File(outputFilePath)
        );
        int copy = IOUtils.copy(inputStream, fileOutputStream);

        return outputFilePath;

    }

    public String freemarkerCreateHtml() throws TemplateException, IOException {
        FreemarkerUtil freemarkerUtil = new FreemarkerUtil();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("title", "名字");
        hashMap.put("content", "内容");
        return freemarkerUtil.createHtml(hashMap, "new", "E:\\授课\\专高5资料\\"+ UUID.randomUUID().toString() +".html");

    }

}
