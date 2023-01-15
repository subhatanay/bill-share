package com.subhadev.billshare.notificationservice.helper;

import org.apache.commons.text.StringSubstitutor;

import java.util.Map;

public class TemplateParser {

    public static String parseTemplate(String templateContent, Map<String,Object> attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("attributes should not be empty");
        }

        StringSubstitutor stringSubstitutor = new StringSubstitutor(attributes);
        return stringSubstitutor.replace(templateContent);
    }
}
