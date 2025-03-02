package com.digital.tolk.digitaltolk.repository;

import com.digital.tolk.digitaltolk.repository.model.Tag;
import com.digital.tolk.digitaltolk.repository.model.Translation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TranslationRepositoryTest {

    @Autowired
    private TranslationRepository translationRepository;

    private Translation translation1;
    private Translation translation2;

    @BeforeEach
    void setUp() {
        translation1 = new Translation();
        translation1.setKey("greeting");
        translation1.setLocale("en");
        translation1.setContent("Hello");
        translation1.setTags(Set.of(Tag.MOBILE, Tag.DESKTOP));

        translation2 = new Translation();
        translation2.setKey("farewell");
        translation2.setLocale("en");
        translation2.setContent("Goodbye");
        translation2.setTags(Set.of(Tag.WEB));

        translationRepository.save(translation1);
        translationRepository.save(translation2);
    }

    @Test
    void shouldSaveAndFindTranslationById() {
        Optional<Translation> foundTranslation = translationRepository.findById(translation1.getId());

        assertThat(foundTranslation).isPresent();
        assertThat(foundTranslation.get().getKey()).isEqualTo("greeting");
        assertThat(foundTranslation.get().getContent()).isEqualTo("Hello");
        assertThat(foundTranslation.get().getTags()).contains(Tag.MOBILE, Tag.DESKTOP);
    }

    @Test
    void shouldReturnTranslationsBasedOnSearchCriteria() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Translation> searchResult = translationRepository.search("en", "greeting", "Hello", Tag.MOBILE, pageable);

        assertThat(searchResult).isNotEmpty();
        assertThat(searchResult.getContent()).hasSize(1);
        assertThat(searchResult.getContent().get(0).getKey()).isEqualTo("greeting");
        assertThat(searchResult.getContent().get(0).getTags()).contains(Tag.MOBILE);
    }

    @Test
    void shouldDeleteTranslation() {
        translationRepository.deleteById(translation1.getId());
        Optional<Translation> deletedTranslation = translationRepository.findById(translation1.getId());
        assertThat(deletedTranslation).isEmpty();
    }
}