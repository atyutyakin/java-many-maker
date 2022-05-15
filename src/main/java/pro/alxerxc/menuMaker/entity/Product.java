package pro.alxerxc.menuMaker.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "product")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Product implements Persistable<Long>{

    @Id
    //@Setter(AccessLevel.NONE) // problems in creating object from post form
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name shouldn't be empty")
    @Size(min = 2, max = 150, message = "Name should have length from 2 up to 150 characters")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP")
    private Instant createdAt;

    @Column(name = "modified_at", columnDefinition = "TIMESTAMP")
    private Instant modifiedAt;

    @Embedded
    @NotNull
    @Valid
    private NutrientsInfo nutrientsInfo;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof Product)) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
