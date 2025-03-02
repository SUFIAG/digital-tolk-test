package com.digital.tolk.digitaltolk.controller;

import com.digital.tolk.digitaltolk.repository.model.Tag;
import com.digital.tolk.digitaltolk.repository.model.Translation;
import com.digital.tolk.digitaltolk.repository.model.TranslationDTO;
import com.digital.tolk.digitaltolk.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TranslationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TranslationService translationService;

    @InjectMocks
    private TranslationController translationController;

    private Translation translation;
    private TranslationDTO translationDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(translationController).build();

        translation = new Translation();
        translation.setId(1L);
        translation.setKey("hello");
        translation.setLocale("en");
        translation.setContent("Hello");
        translation.setTags(Set.of(Tag.MOBILE, Tag.DESKTOP, Tag.WEB));

        Set<String> tagStrings = translation.getTags().stream()
                .map(Tag::name)
                .collect(Collectors.toSet());

        translationDTO = new TranslationDTO("hello", "en", "Hello", tagStrings);
    }

    @Test
    void testAddTranslation() throws Exception {
        when(translationService.addTranslation(any(TranslationDTO.class))).thenReturn(translation);

        mockMvc.perform(post("/api/translations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"hello\",\"locale\":\"en\",\"content\":\"Hello\",\"tags\":[\"MOBILE\",\"DESKTOP\",\"WEB\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("hello"))
                .andExpect(jsonPath("$.tags", hasSize(3)))
                .andExpect(jsonPath("$.tags").isArray());
    }

    @Test
    void testUpdateTranslation_Found() throws Exception {
        when(translationService.updateTranslation(eq(1L), any(TranslationDTO.class)))
                .thenReturn(Optional.of(translation));

        mockMvc.perform(put("/api/translations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"hello\",\"locale\":\"en\",\"content\":\"Hello\",\"tags\":[\"MOBILE\",\"DESKTOP\",\"WEB\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("hello"))
                .andExpect(jsonPath("$.tags", hasSize(3)));
    }

    @Test
    void testExportTranslations() throws Exception {
        List<TranslationDTO> translations = List.of(translationDTO);
        when(translationService.getAllTranslations()).thenReturn(translations);

        mockMvc.perform(get("/api/translations/export"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].tags", hasSize(3)))
                .andExpect(jsonPath("$[0].tags").isArray());
    }


    @Test
    void testUpdateTranslation_NotFound() throws Exception {
        when(translationService.updateTranslation(eq(1L), any(TranslationDTO.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/translations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"key\":\"hello\",\"locale\":\"en\",\"content\":\"Hello\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTranslation() throws Exception {
        doNothing().when(translationService).deleteTranslation(1L);

        mockMvc.perform(delete("/api/translations/1"))
                .andExpect(status().isNoContent());
    }

}
