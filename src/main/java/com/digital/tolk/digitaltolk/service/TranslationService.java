package com.digital.tolk.digitaltolk.service;

import com.digital.tolk.digitaltolk.repository.TranslationRepository;
import com.digital.tolk.digitaltolk.repository.model.Tag;
import com.digital.tolk.digitaltolk.repository.model.Translation;
import com.digital.tolk.digitaltolk.repository.model.TranslationDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class TranslationService {
    private final TranslationRepository translationRepository;

    public TranslationService(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    @Transactional
    @CacheEvict(value = "translations", allEntries = true)
    public Translation addTranslation(@Valid @NotNull TranslationDTO dto) {
        Translation translation = new Translation();
        translation.setKey(dto.getKey());
        translation.setLocale(dto.getLocale());
        translation.setContent(dto.getContent());

        Set<Tag> tags = mapTags(dto.getTags());
        translation.setTags(tags);

        return translationRepository.save(translation);
    }

    @Transactional
    @CacheEvict(value = "translations", allEntries = true)
    public Optional<Translation> updateTranslation(Long id, @Valid @NotNull TranslationDTO dto) {
        return translationRepository.findById(id).map(translation -> {
            translation.setKey(dto.getKey());
            translation.setLocale(dto.getLocale());
            translation.setContent(dto.getContent());
            translation.setTags(mapTags(dto.getTags()));
            return translationRepository.save(translation);
        });
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "translations")
    public List<TranslationDTO> getAllTranslations() {
        return translationRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<Translation> searchTranslations(String locale, String key, String content, String tag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Tag tagEnum = (tag == null || tag.isEmpty()) ? null : Tag.valueOf(tag.toUpperCase());

        return translationRepository.search(
                locale.isEmpty() ? null : locale,
                key.isEmpty() ? null : key,
                content.isEmpty() ? null : content,
                tagEnum,
                pageable
        );
    }

    @Transactional
    @CacheEvict(value = "translations", allEntries = true)
    public void deleteTranslation(Long id) {
        translationRepository.deleteById(id);
    }

    private Set<Tag> mapTags(Set<String> tags) {
        if (tags == null) return new HashSet<>();
        return tags.stream()
                .map(tag -> {
                    try {
                        return Tag.valueOf(tag.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid tag: " + tag);
                    }
                })
                .collect(Collectors.toSet());
    }

    private TranslationDTO convertToDTO(Translation t) {
        return new TranslationDTO(
                t.getKey(),
                t.getLocale(),
                t.getContent(),
                t.getTags().stream().map(Enum::name).collect(Collectors.toSet())
        );
    }
}
