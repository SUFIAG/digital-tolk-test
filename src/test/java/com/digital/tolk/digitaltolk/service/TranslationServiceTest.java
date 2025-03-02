package com.digital.tolk.digitaltolk.service;

import com.digital.tolk.digitaltolk.repository.TranslationRepository;
import com.digital.tolk.digitaltolk.repository.model.Tag;
import com.digital.tolk.digitaltolk.repository.model.Translation;
import com.digital.tolk.digitaltolk.repository.model.TranslationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @Mock
    private TranslationRepository translationRepository;

    @InjectMocks
    private TranslationService translationService;

    private Translation translation;
    private TranslationDTO translationDTO;

    @BeforeEach
    void setUp() {
        translation = new Translation();
        translation.setId(1L);
        translation.setKey("hello");
        translation.setLocale("en");
        translation.setContent("Hello");
        translation.setTags(Set.of(Tag.MOBILE, Tag.DESKTOP));

        translationDTO = new TranslationDTO("hello", "en", "Hello", Set.of("MOBILE", "DESKTOP"));
    }

    @Test
    void testAddTranslation() {
        when(translationRepository.save(any(Translation.class))).thenReturn(translation);

        Translation savedTranslation = translationService.addTranslation(translationDTO);

        assertNotNull(savedTranslation);
        assertEquals("hello", savedTranslation.getKey());
        assertEquals(Set.of(Tag.MOBILE, Tag.DESKTOP), savedTranslation.getTags());
        verify(translationRepository, times(1)).save(any(Translation.class));
    }

    @Test
    void testUpdateTranslation_Found() {
        when(translationRepository.findById(1L)).thenReturn(Optional.of(translation));
        when(translationRepository.save(any(Translation.class))).thenReturn(translation);

        Optional<Translation> updatedTranslation = translationService.updateTranslation(1L, translationDTO);

        assertTrue(updatedTranslation.isPresent());
        assertEquals("hello", updatedTranslation.get().getKey());
        assertEquals(Set.of(Tag.MOBILE, Tag.DESKTOP), updatedTranslation.get().getTags());
        verify(translationRepository, times(1)).save(any(Translation.class));
    }

    @Test
    void testUpdateTranslation_NotFound() {
        when(translationRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Translation> updatedTranslation = translationService.updateTranslation(1L, translationDTO);

        assertFalse(updatedTranslation.isPresent());
        verify(translationRepository, never()).save(any(Translation.class));
    }

    @Test
    void testSearchTranslations() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Translation> page = new PageImpl<>(List.of(translation));

        when(translationRepository.search("en", "hello", "Hello", Tag.MOBILE, pageRequest)).thenReturn(page);

        Page<Translation> result = translationService.searchTranslations("en", "hello", "Hello", "MOBILE", 0, 10);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("hello", result.getContent().get(0).getKey());
    }

    @Test
    void testSearchTranslations_NoResults() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Translation> emptyPage = new PageImpl<>(Collections.emptyList());

        when(translationRepository.search("fr", "bye", "Goodbye", null, pageRequest)).thenReturn(emptyPage);

        Page<Translation> result = translationService.searchTranslations("fr", "bye", "Goodbye", "", 0, 10);

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteTranslation() {
        doNothing().when(translationRepository).deleteById(1L);

        translationService.deleteTranslation(1L);

        verify(translationRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllTranslations() {
        when(translationRepository.findAll()).thenReturn(List.of(translation));

        List<TranslationDTO> translations = translationService.getAllTranslations();

        assertFalse(translations.isEmpty());
        assertEquals(1, translations.size());
        assertEquals("hello", translations.get(0).getKey());
        assertEquals(Set.of("MOBILE", "DESKTOP"), translations.get(0).getTags());
    }

    @Test
    void testGetAllTranslations_EmptyList() {
        when(translationRepository.findAll()).thenReturn(Collections.emptyList());

        List<TranslationDTO> translations = translationService.getAllTranslations();

        assertTrue(translations.isEmpty());
    }
}
