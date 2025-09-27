package EcoTrack.server.entity;


import EcoTrack.server.DTO.ScoreDTO;
import EcoTrack.server.enums.QualitativeScore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private QualitativeScore qualitativeScore;

    private double totalco2;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "userActivity_id",unique=true)
    private UserActivity userActivity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Score(ScoreDTO scoreDTO) {
        setQualitativeScore(scoreDTO.getQualitativeScore());
        setTotalco2(scoreDTO.getTotalco2());
    }
}
