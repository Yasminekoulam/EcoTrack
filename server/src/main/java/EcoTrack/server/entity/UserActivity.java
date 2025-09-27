package EcoTrack.server.entity;

import EcoTrack.server.DTO.UserActivityDTO;
import EcoTrack.server.enums.SharingType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users_Activities")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double quantity;
    private LocalDate date;
    //enum de type de partage
    @Enumerated(EnumType.STRING)
    @Column(name = "sharing_type", length = 20, nullable = false)
    private SharingType sharingType;
    @Min(1)
    private int nbrPersonnes;

    @ManyToOne
    @JoinColumn(name = "activity_type_id", nullable = false)
    private ActivityType activityType;

    @OneToOne(mappedBy = "userActivity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Score score;

    //user-UserActivity
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserActivity(UserActivityDTO userActivityDTO) {
        setDate(userActivityDTO.getDate());
        setNbrPersonnes(userActivityDTO.getNbrPersonnes());
        setQuantity(userActivityDTO.getQuantity());
        setSharingType(userActivityDTO.getSharingType());
    }
}