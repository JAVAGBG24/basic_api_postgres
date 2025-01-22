package com.postgres.bookshop.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "books")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
// @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
// är en Jackson-annotation som talar om för JSON-serialiseraren att
// ignorera vissa specifika fält när ett objekt ska konverteras till JSON.
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String title;

    @Min(1)
    private Integer pages;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "co_author_id")
    private Author coAuthor;

    @NotNull
    @Positive
    private Double priceExVat;

    @Pattern(regexp = "^(?:[0-9]{10}|[0-9]{13})$", message = "ISBN must be exactly 10 or 13 digits")
    private String isbn;

    @NotNull
    private String bookCoverUrl;

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author getCoAuthor() {
        return coAuthor;
    }

    public void setCoAuthor(Author coAuthor) {
        this.coAuthor = coAuthor;
    }

    public Double getPriceExVat() {
        return priceExVat;
    }

    public void setPriceExVat(Double priceExVat) {
        this.priceExVat = priceExVat;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBookCoverUrl() {
        return bookCoverUrl;
    }

    public void setBookCoverUrl(String bookCoverUrl) {
        this.bookCoverUrl = bookCoverUrl;
    }
}
