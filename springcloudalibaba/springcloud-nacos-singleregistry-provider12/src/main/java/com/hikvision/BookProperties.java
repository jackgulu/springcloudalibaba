package com.hikvision;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "book")
@Component
public class BookProperties {
    private String category1;

    private String author;

    @Override
    public String toString() {
        return "BookProperties{" +
                "category1='" + category1 + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
