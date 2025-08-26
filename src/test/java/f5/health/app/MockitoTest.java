package f5.health.app;

import f5.health.app.constant.member.BloodType;
import f5.health.app.constant.member.Gender;
import f5.health.app.entity.member.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockitoTest {

    @Test
    void stubbing() {
        LocalDate now = LocalDate.now();
        Gender male = Gender.MALE;
        int height = 173;
        int weight = 60;
        BloodType bloodType = BloodType.B;
        int daySmokeCigarettes = 6;
        int weekAlcoholDrinks = 500;
        int weekExerciseFrequency = 3;

        Member.MemberCheckUp memberCheckUp = mock(Member.MemberCheckUp.class);
        when(memberCheckUp.getBirthDate()).thenReturn(now);
        when(memberCheckUp.getGender()).thenReturn(male);
        when(memberCheckUp.getHeight()).thenReturn(height);
        when(memberCheckUp.getWeight()).thenReturn(weight);
        when(memberCheckUp.getBloodType()).thenReturn(bloodType);
        when(memberCheckUp.getDaySmokeCigarettes()).thenReturn(daySmokeCigarettes);
        when(memberCheckUp.getWeekAlcoholDrinks()).thenReturn(weekAlcoholDrinks);
        when(memberCheckUp.getWeekExerciseFrequency()).thenReturn(weekExerciseFrequency);

        assertEquals(now, memberCheckUp.getBirthDate());
        assertEquals(male, memberCheckUp.getGender());
        assertEquals(height, memberCheckUp.getHeight());
        assertEquals(weight, memberCheckUp.getWeight());
        assertEquals(bloodType, memberCheckUp.getBloodType());
        assertEquals(daySmokeCigarettes, memberCheckUp.getDaySmokeCigarettes());
        assertEquals(weekAlcoholDrinks, memberCheckUp.getWeekAlcoholDrinks());
        assertEquals(weekExerciseFrequency, memberCheckUp.getWeekExerciseFrequency());
    }

}
