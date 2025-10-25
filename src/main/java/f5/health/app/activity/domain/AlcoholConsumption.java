package f5.health.app.activity.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(AlcoholConsumptionId.class)
@Table(name = "ALCOHOL_CONSUMPTION")
public class AlcoholConsumption {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_ID")
    private Activity activity;
    @Id
    @Column(name = "ALCOHOL_TYPE")
    @Enumerated(EnumType.STRING)
    private AlcoholType alcoholType;

    @Column(name = "INTAKE")
    private int intake;

    public AlcoholConsumption(AlcoholType alcoholType, int intake) {
        this.alcoholType = alcoholType;
        this.intake = intake;
    }


    void setActivity(Activity activity) {
        this.activity = activity;
    }

    boolean isSameId(AlcoholConsumptionId id) {
        return Objects.equals(this.activity.getId(), id.activityId())
                && Objects.equals(this.alcoholType, id.alcoholType());
    }

    void update(int intake) {
        this.intake = intake;
    }
}

