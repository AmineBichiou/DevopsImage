package com.example.event.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String description;
    private Date date;
    private String lieu;
    private double prix;
    private Long organisateurId;
    private String imageUrl;
    private int nbplaces;
    private int nbrlikes;
    @ElementCollection
    private List<String> domains;
}
