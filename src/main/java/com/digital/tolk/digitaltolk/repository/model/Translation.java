package com.digital.tolk.digitaltolk.repository.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "translations", indexes = {
        @Index(name = "idx_locale", columnList = "locale"),
        @Index(name = "idx_translation_key", columnList = "translation_key")
})
public class Translation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "translation_key", nullable = false)
    private String key;

    @Column(nullable = false, length = 5)
    private String locale;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "translation_tags", joinColumns = @JoinColumn(name = "translation_id"))
    @Column(name = "tag")
    private Set<Tag> tags;

    public Translation(Long id, String key, String locale, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Set<Tag> tags) {
        this.id = id;
        this.key = key;
        this.locale = locale;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tags = tags;
    }

    public Translation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
}
