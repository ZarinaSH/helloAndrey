package com.example.rssreader.utils.url_validator;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class SimpleUrlValidator implements IUrlValidator {
    @Override
    public boolean validate(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            return false;
        }
    }
}
