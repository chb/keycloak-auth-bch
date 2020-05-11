package org.chip.ihl.certificates.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.stream.Collectors;

@Component
public class ResourceUtils {
    @Autowired
    private ResourceLoader resourceLoader;

    public String getResourceFileAsString(String fileName, Class currentClass) {
        InputStream is = getResourceFileAsInputStream(fileName, currentClass);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return (String)reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            return null;
        }
    }

    public InputStream getResourceFileAsInputStream(String fileName, Class currentClass) {
        ClassLoader classLoader = currentClass.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }


   // public byte[] getBytesFromResource(String filename) {
        //String fileString = "";
        //try {
        //    final Resource resource = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX+ filename);
        //    Reader reader = new InputStreamReader(resource.getInputStream());
        //    fileString =  FileCopyUtils.copyToString(reader);
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        //return fileString.getBytes();
    //}
    public InputStream getInputStreamForResource(String filename) {
        try {
            final Resource resource = resourceLoader.getResource(ResourceLoader.CLASSPATH_URL_PREFIX+filename);
            return resource.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
