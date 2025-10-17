package com.bookstore.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "authors", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @OneToMany(mappedBy = "author", cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JsonManagedReference
    private List<Book> books = new ArrayList<>();

    @OneToOne(optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @NotBlank(message = "Bio is required")
    private String bio;

    @NotBlank(message = "Pseudonym is required")
    @Column(unique = true)
    private String pseudonym;

    @OneToMany(mappedBy = "author")
    @JsonIgnore
    private List<ModerationRequest> moderationRequests = new ArrayList<>();
}
