package ru.kovalenkojuls.usersorders.domain;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.kovalenkojuls.usersorders.view.UserView;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(UserView.UserSummary.class)
    private Long id;

    @JsonView(UserView.UserSummary.class)
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @JsonView(UserView.UserSummary.class)
    @Email(message = "Invalid email format")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonView(UserView.UserDetails.class)
    private List<Order> orders;

}
