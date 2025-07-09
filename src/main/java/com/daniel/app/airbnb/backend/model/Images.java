package com.daniel.app.airbnb.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Images {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    @Column(columnDefinition = "TEXT")
    private String main;
    @ElementCollection
    @CollectionTable(name = "image_gallery", joinColumns = @JoinColumn(name = "images_id"))

    @Column(name = "gallery_image", columnDefinition = "TEXT")
    private List<String> gallery;
}
