package com.edu.artexhibit.models.entity;

import com.edu.artexhibit.models.enums.ArtistCategory;
import com.edu.artexhibit.models.enums.Role;
import com.edu.artexhibit.utils.ImageUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "artists")
@ToString
public class ArtistEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String username;
    private ArtistCategory artistCategory;
    private String password;
    private String description;
    private LocalDate birthOfDate;
    private Instant createdAt;
    private Boolean student;
    private Instant updatedAt;
    @Column(name = "email", unique = true)
    private String email;
    private String phoneNumber;
    private String address;
    @Lob
    private byte[] profileImage;
    @OneToOne(mappedBy = "artist", cascade = CascadeType.ALL)
    @ToString.Exclude
    private PortfolioEntity portfolio;
    @OneToMany(mappedBy = "artist", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnore
    private Set<NotificationEntity> notification = new HashSet<>();


    public void setProfileImage(String profileImage) {
        this.profileImage = ImageUtil.compressFile(profileImage);
    }

    public String getProfileImage() {
        return ImageUtil.decompressFile(profileImage);
    }

    public PortfolioEntity getPortfolio() {
        return portfolio != null ? new PortfolioEntity(
                portfolio.getId(),
                portfolio.getName(),
                portfolio.getJsonTheme(),
                portfolio.getCreatedAt(),
                portfolio.getUpdatedAt(),
                portfolio.getFiles(),
                null
        ) : null;
    }

    public void setPortfolio(PortfolioEntity portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArtistEntity that = (ArtistEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_" + Role.ARTIST), new SimpleGrantedAuthority("ROLE_" + Role.USER)));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
