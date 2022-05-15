package pro.alxerxc.menuMaker.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recipe implements Persistable<Long> {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name shouldn't be empty")
    @Size(min = 2, max = 150, message = "Name should have length from 2 up to 150 characters")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Embedded
    @NotNull
    private NutrientsInfo nutrientsInfo;

    @ElementCollection
    @CollectionTable(name = "recipe_items")
    @OrderColumn(name = "line_no")
    private List<RecipeItem> ingredients = new ArrayList<>();
}
