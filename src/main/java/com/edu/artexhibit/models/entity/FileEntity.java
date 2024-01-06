package com.edu.artexhibit.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] content;
    private Instant createdAt;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;

}
