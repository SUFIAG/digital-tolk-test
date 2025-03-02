package com.digital.tolk.digitaltolk.controller;

import com.digital.tolk.digitaltolk.repository.model.Translation;
import com.digital.tolk.digitaltolk.repository.model.TranslationDTO;
import com.digital.tolk.digitaltolk.service.TranslationService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/translations")
public class TranslationController {
    private final TranslationService translationService;

    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping
    @CacheEvict(value = "translations", allEntries = true)
    public ResponseEntity<Translation> addTranslation(@RequestBody TranslationDTO dto) {
        Translation translation = translationService.addTranslation(dto);
        return ResponseEntity.ok(translation);
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "translations", allEntries = true)
    public ResponseEntity<Translation> updateTranslation(@PathVariable Long id, @RequestBody TranslationDTO dto) {
        return translationService.updateTranslation(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping
    public ResponseEntity<Page<Translation>> searchTranslations(
            @RequestParam(required = false, defaultValue = "") String locale,
            @RequestParam(required = false, defaultValue = "") String key,
            @RequestParam(required = false, defaultValue = "") String content,
            @RequestParam(required = false, defaultValue = "") String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(translationService.searchTranslations(locale, key, content, tag, page, size));
    }


    @DeleteMapping("/{id}")
    @CacheEvict(value = "translations", allEntries = true)
    public ResponseEntity<Void> deleteTranslation(@PathVariable Long id) {
        translationService.deleteTranslation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/export")
    @Cacheable("translations")
    public ResponseEntity<List<TranslationDTO>> exportTranslations() {
        List<TranslationDTO> translations = translationService.getAllTranslations();
        return ResponseEntity.ok(translations);
    }

}

