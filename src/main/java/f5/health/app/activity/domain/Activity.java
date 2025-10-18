package f5.health.app.activity.domain;

import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumption;
import f5.health.app.activity.domain.alcoholconsumption.AlcoholConsumptionFactory;
import f5.health.app.activity.vo.ActivityRequest;
import f5.health.app.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 건강 관련 활동 기록
 */
@Getter
@Entity
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ACTIVITY", uniqueConstraints = {@UniqueConstraint(columnNames = {"MEMBER_ID", "RECORD_DATE"})})
public class Activity {

    public static final int DAILY_MAX_WATER_ML = 5000;
    public static final int DAILY_MAX_CIGARETTES = 40;
    public static final int DAILY_MAX_ALCOHOL_ML = 1500;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<AlcoholConsumption> alcoholConsumptions = new ArrayList<>();

    @Column(name = "WATER_INTAKE")
    private Integer waterIntake;

    @Column(name = "SMOKED_CIGARETTES")
    private Integer smokedCigarettes;

    @Column(name = "RECORD_DATE")
    private LocalDate recordDate;

    public static ActivityBuilder createActivity(Member writer, List<ActivityRequest.AlcoholConsumptionParam> alcoholParams) {
        List<AlcoholConsumption> alcoholConsumptions = AlcoholConsumptionFactory.from(alcoholParams);
        return new ActivityBuilder(writer, alcoholConsumptions);
    }

    public void addAlcoholConsumption(AlcoholConsumption alcoholConsumption) {
        this.alcoholConsumptions.add(alcoholConsumption);
        alcoholConsumption.setActivity(this);
    }

    public boolean isOwnedBy(Long memberId) {
        return this.member.getId().equals(memberId);
    }


    public static class ActivityBuilder {

        private final Activity activity;

        private ActivityBuilder(Member member, List<AlcoholConsumption> alcoholConsumptions) {
            activity = new Activity();
            activity.member = member;
            alcoholConsumptions.forEach(activity::addAlcoholConsumption);
        }

        public ActivityBuilder waterIntake(final Integer waterIntake) {
            activity.waterIntake = waterIntake;
            return this;
        }

        public ActivityBuilder smokedCigarettes(final Integer smokedCigarettes) {
            activity.smokedCigarettes = smokedCigarettes;
            return this;
        }

        public ActivityBuilder recordDate(final LocalDate recordDate) {
            activity.recordDate = recordDate;
            return this;
        }

        public Activity build() {
            return this.activity;
        }
    }
}
