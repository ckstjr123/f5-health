package f5.health.app.activity.entity;

import f5.health.app.activity.constant.AlcoholType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ALCOHOL_CONSUMPTION")
public class AlcoholConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALCOHOL_CONSUMPTION_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVITY_ID")
    private Activity activity;

    @Column(name = "ALCOHOL_TYPE")
    @Enumerated(EnumType.STRING)
    private AlcoholType alcoholType;

    @Column(name = "ALCOHOL_INTAKE")
    private int alcoholIntake;

    public static AlcoholConsumption newInstance(AlcoholType alcoholType, int alcoholIntake) {
        AlcoholConsumption alcoholConsumption = new AlcoholConsumption();
        alcoholConsumption.alcoholType = alcoholType;
        alcoholConsumption.alcoholIntake = alcoholIntake;
        return alcoholConsumption;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
