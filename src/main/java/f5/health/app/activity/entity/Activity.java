package f5.health.app.activity.entity;

import f5.health.app.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 건강 관련 활동 기록
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ACTIVITY", uniqueConstraints = {@UniqueConstraint(columnNames = {"MEMBER_ID", "RECORD_DATE"})})
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACTIVITY_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "WATER_INTAKE")
    private int waterIntake;

    @Column(name = "SMOKED_CIGARETTES")
    private int smokedCigarettes;

    @Column(name = "ALCOHOL_INTAKE")
    private int alcoholIntake; // 총 음주량(비정규화 컬럼)
//    @OneToMany(mappedBy = "", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<AlcoholConsumption> alcoholConsumptionList = new ArrayList<>();

    @Column(name = "RECORD_DATE")
    private LocalDate recordDate;

    public static ActivityBuilder builder(Member writer) {
        return new ActivityBuilder(writer);
    }


    public static class ActivityBuilder {

        private final Activity activity;

        private ActivityBuilder(final Member member) {
            this.activity = new Activity();
            activity.member = member;
            member.addPoint(50);
        }

        public ActivityBuilder waterIntake(final int waterIntake) {
            activity.waterIntake = waterIntake;
            return this;
        }

        public ActivityBuilder smokedCigarettes(final int smokedCigarettes) {
            activity.smokedCigarettes = smokedCigarettes;
            return this;
        }

        public ActivityBuilder alcoholIntake(final int alcoholIntake) {
            activity.alcoholIntake = alcoholIntake;
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
