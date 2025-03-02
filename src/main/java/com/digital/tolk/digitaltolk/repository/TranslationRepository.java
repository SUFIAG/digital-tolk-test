package com.digital.tolk.digitaltolk.repository;

import com.digital.tolk.digitaltolk.repository.model.Tag;
import com.digital.tolk.digitaltolk.repository.model.Translation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    @Query("SELECT t FROM Translation t " +
            "LEFT JOIN t.tags tag " +
            "WHERE (:locale IS NULL OR LOWER(t.locale) LIKE LOWER(CONCAT('%', :locale, '%'))) " +
            "AND (:key IS NULL OR LOWER(t.key) LIKE LOWER(CONCAT('%', :key, '%'))) " +
            "AND (:content IS NULL OR LOWER(t.content) LIKE LOWER(CONCAT('%', :content, '%'))) " +
            "AND (:tag IS NULL OR tag IN (SELECT tg FROM Translation tr JOIN tr.tags tg WHERE tg = :tag))")
    Page<Translation> search(
            @Param("locale") String locale,
            @Param("key") String key,
            @Param("content") String content,
            @Param("tag") Tag tag,
            Pageable pageable);

}
