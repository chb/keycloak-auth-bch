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
public class LocalResourceUtils {

    public String getResourceFileAsString(String fileName, Class currentClass) {
        InputStream is = getResourceFileAsInputStream(fileName, currentClass);
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } else {
            return null;
        }
    }

    public InputStream getResourceFileAsInputStream(String fileName, Class currentClass) {
        ClassLoader classLoader = currentClass.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }



}
