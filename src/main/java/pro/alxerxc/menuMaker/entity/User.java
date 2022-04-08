package pro.alxerxc.menuMaker.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@Getter
@Table(name = "app_user")
public class User implements Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Email should be not blank")
    @Email(message = "Email should be valid email address")
    @Column(name = "email", nullable = false)
    private String email;

}
