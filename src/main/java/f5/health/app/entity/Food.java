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


    /** 해당 메뉴 칼로리 함량 */
    public int calculateServingKcal() {
        return (int) (calculateServingRatio() * this.kcal);
    }

    // ========= 탄수화물, 단백질, 지방 함량 ========= //
    public double calculateServingCarbohydrate() {
        return (calculateServingRatio() * this.carbohydrate);
    }
    public double calculateServingProtein() {
        return (calculateServingRatio() * this.protein);
    }
    public double calculateServingFat() {
        return (calculateServingRatio() * this.fat);
    }


    /** 식품 중량과 영양성분 함량 기준량에 따른 1인분 영양성분 함량 구하는 용도 */
    private double calculateServingRatio() {
        double foodWeight = Double.parseDouble(parseNumericString(this.foodWeight));
        double nutritionContentStdQuantity = Double.parseDouble(parseNumericString(this.nutritionContentStdQuantity));
        return (foodWeight / nutritionContentStdQuantity);
    }

    /** 단위(g, ml) 제거 */
    private String parseNumericString(String unitStr) {
        return unitStr.replaceAll("[^\\d.]", "");
    }
}
