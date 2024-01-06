package com.edu.artexhibit.models.entity;

import com.edu.artexhibit.models.dtos.PortfolioDto;
import com.edu.artexhibit.utils.ImageUtil;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Instant createdAt;
    private Instant updatedAt;
    private LocalDateTime time;
    @Lob
    private byte[] photo;
    private String address;
    private String description;
    private String category;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private ArtistEntity artist;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private PortfolioEntity portfolio;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getPhoto() {
        return ImageUtil.decompressFile(photo);
    }

    public void setPhoto(String photo) {
        this.photo = ImageUtil.compressFile(photo);
    }
}
