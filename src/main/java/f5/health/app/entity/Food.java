package f5.health.app.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "FOOD")
public class Food {

    public static final int FOOD_CODE_LENGTH = 19;

    @Id
    @Column(name = "FOOD_CODE", length = FOOD_CODE_LENGTH)
    private String foodCode;

    @Column(name = "FOOD_NAME", unique = true)
    private String foodName;

    @Column(name = "FOOD_TYPE")
    private String foodType;

    @Column(name = "KCAL")
    private int kcal;

    @Column(name = "NATRIUM")
    private int natrium;

    @Column(name = "CARBOHYDRATE")
    private double carbohydrate;

    @Column(name = "SUGAR")
    private double sugar;

    @Column(name = "PROTEIN")
    private double protein;

    @Column(name = "FAT")
    private double fat;

    /** 영양성분 함량 기준량 */
    @Column(name = "NUT_CON_STD_QUA")
    private String nutritionContentStdQuantity;

    @Column(name = "FOOD_WEIGHT")
    private String foodWeight;


    /** 영양성분 함량 기준량과 식품 중량에 따라 해당 음식 1인분 칼로리 계산 */
    public int calculateOneServingKcal() {
        double nutConStdQua = Double.parseDouble(parseNumericString(this.nutritionContentStdQuantity));
        double foodSize = Double.parseDouble(parseNumericString(this.foodWeight));
        return (int) ((foodSize / nutConStdQua) * this.kcal);
    }

    /** 단위 문자 제거(숫자, 소수점 제외) */
    private String parseNumericString(String unitStr) {
        return unitStr.replaceAll("[^\\d.]", "");
    }
}
