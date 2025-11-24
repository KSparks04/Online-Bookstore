package org.project.model;

import jakarta.persistence.*;

@Entity
public class JaccardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long referenceBookISBN;

    @Column(nullable = false)
    private long similarBookISBN;

    @Column(nullable=false)
    private String similarBookTitle;

    @Column(nullable = false)
    private double similarityScore;

    protected JaccardEntry() {}

    public JaccardEntry(long referenceBookISBN, long similarBookISBN, String similarBookTitle, double similarityScore) {
        this.referenceBookISBN = referenceBookISBN;
        this.similarBookISBN = similarBookISBN;
        this.similarBookTitle = similarBookTitle;
        this.similarityScore = similarityScore;
    }

    public Long getId() { return id; }
    public long getReferenceBookIsbn() { return referenceBookISBN; }
    public long getSimilarBookIsbn() { return similarBookISBN; }
    public double getSimilarityScore() { return similarityScore; }
    public String getSimilarBookTitle(){return similarBookTitle;}
}
