package pro.alxerxc.menuMaker.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class RecipeItem {

    @org.hibernate.annotations.Parent
    private Recipe recipe;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Column(nullable = false)
    private BigDecimal qty;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecipeItem that = (RecipeItem) o;

        if (!recipe.equals(that.recipe)) return false;
        if (!product.equals(that.product)) return false;
        return qty.equals(that.qty);
    }

    @Override
    public int hashCode() {
        int result = recipe.hashCode();
        result = 31 * result + product.hashCode();
        result = 31 * result + qty.hashCode();
        return result;
    }
}
