package f5.health.app;

import f5.health.app.member.constant.BloodType;
import f5.health.app.member.constant.Gender;
import f5.health.app.member.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
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

        Member.CheckUp memberCheckUp = mock(Member.CheckUp.class);
        when(memberCheckUp.getBirthDate()).thenReturn(now);
        when(memberCheckUp.getGender()).thenReturn(male);
        when(memberCheckUp.getHeight()).thenReturn(height);
        when(memberCheckUp.getWeight()).thenReturn(weight);
        when(memberCheckUp.getBloodType()).thenReturn(bloodType);

        assertAll(
                () -> assertEquals(now, memberCheckUp.getBirthDate()),
                () -> assertEquals(male, memberCheckUp.getGender()),
                () -> assertEquals(height, memberCheckUp.getHeight()),
                () -> assertEquals(weight, memberCheckUp.getWeight()),
                () -> assertEquals(bloodType, memberCheckUp.getBloodType())
        );
    }

}
