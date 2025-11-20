package org.project.model;

import jakarta.persistence.*;

@Entity
public class JaccardEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int referenceBookISBN;

    @Column(nullable = false)
    private int similarBookISBN;

    @Column(nullable=false)
    private String similarBookTitle;

    @Column(nullable = false)
    private double similarityScore;

    protected JaccardEntry() {}

    public JaccardEntry(int referenceBookISBN, int similarBookISBN, String similarBookTitle, double similarityScore) {
        this.referenceBookISBN = referenceBookISBN;
        this.similarBookISBN = similarBookISBN;
        this.similarBookTitle = similarBookTitle;
        this.similarityScore = similarityScore;
    }

    public Long getId() { return id; }
    public int getReferenceBookIsbn() { return referenceBookISBN; }
    public int getSimilarBookIsbn() { return similarBookISBN; }
    public double getSimilarityScore() { return similarityScore; }
    public String getSimilarBookTitle(){return similarBookTitle;}
}
