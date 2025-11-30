package org.project.repository;

import java.util.List;

import org.project.model.JaccardEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JaccardRepository extends JpaRepository<JaccardEntry, Long> {

    // Top similar books for a given reference book, ordered by similarity descending
    @Query("""
        SELECT j
        FROM JaccardEntry j
        WHERE j.referenceBookISBN = :ISBN
        ORDER BY j.similarityScore DESC
        """)
    public List<JaccardEntry> findTopSimilarBooks(@Param("ISBN") Long iSBN, Pageable pageable);

    public boolean existsByreferenceBookISBN(Long iSBN);

}
