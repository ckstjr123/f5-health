package f5.health.app.activity.domain.alcoholconsumption;

import f5.health.app.activity.constant.AlcoholType;
import f5.health.app.activity.domain.Activity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(AlcoholConsumptionId.class)
@Table(name = "ALCOHOL_CONSUMPTION")
public class AlcoholConsumption {

    @Id
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_ID")
    private Activity activity;

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "ALCOHOL_TYPE")
    @Enumerated(EnumType.STRING)
    private AlcoholType alcoholType;

    @Column(name = "INTAKE")
    private int intake;

    protected AlcoholConsumption(AlcoholType alcoholType, int intake) {
        this.alcoholType = alcoholType;
        this.intake = intake;
    }


    void setActivity(Activity activity) {
        this.activity = activity;
    }


    AlcoholConsumptionId getId() {
        return AlcoholConsumptionId.of(this.activity.getId(), this.alcoholType);
    }

     void update(int intake) {
        this.intake = intake;
    }
}

