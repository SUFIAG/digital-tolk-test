package com.digital.tolk.digitaltolk.repository.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.stream.Collectors;

public class TranslationDTO {

    @NotBlank
    private String key;

    @NotBlank
    @Size(max = 5)
    private String locale;

    @NotBlank
    private String content;

    private Set<String> tags;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocale() {
        return locale;
    }

    public String getContent() {
        return content;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public TranslationDTO(String key, String locale, String content, Set<String> tags) {
        this.key = key;
        this.locale = locale;
        this.content = content;
        this.tags = tags;
    }

    public TranslationDTO() {
    }
}
