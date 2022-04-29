package pro.alxerxc.menuMaker.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class NutrientsInfo {

    @NotNull
    @Min(value = 0, message = "calories must in range 0...100")
    @Max(value = 100, message = "calories must in range 0...100")
    @Column(columnDefinition = "FLOAT default '0'", nullable = false)
    private Double calories;

    @NotNull
    @Min(value = 0, message = "fats must in range 0...100")
    @Max(value = 100, message = "fats must in range 0...100")
    @Column(columnDefinition = "FLOAT default '0'", nullable = false)
    private Double fats;

    @NotNull
    @Min(value = 0, message = "carbohydrates must in range 0...100")
    @Max(value = 100, message = "carbohydrates must in range 0...100")
    @Column(columnDefinition = "FLOAT default '0'", nullable = false)
    private Double carbohydrates;

    @NotNull
    @Min(value = 0, message = "proteins must in range 0...100")
    @Max(value = 100, message = "proteins must in range 0...100")
    @Column(columnDefinition = "FLOAT default '0'", nullable = false)
    private Double proteins;

    @Override
    public String toString() {
        return "cal: " + calories +
                ", fat: " + fats +
                ", carb: " + carbohydrates +
                ", prot: " + proteins;
    }

    public NutrientsInfo(Double calories, Double fats, Double carbohydrates, Double proteins) {
        this.calories = calories;
        this.fats = fats;
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ! (o instanceof NutrientsInfo)) return false;

        NutrientsInfo that = (NutrientsInfo) o;

        if (!calories.equals(that.calories)) return false;
        if (!fats.equals(that.fats)) return false;
        if (!carbohydrates.equals(that.carbohydrates)) return false;
        return proteins.equals(that.proteins);
    }

    @Override
    public int hashCode() {
        int result = calories.hashCode();
        result = 31 * result + fats.hashCode();
        result = 31 * result + carbohydrates.hashCode();
        result = 31 * result + proteins.hashCode();
        return result;
    }
}
