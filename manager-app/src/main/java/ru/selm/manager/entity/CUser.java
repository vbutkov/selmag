package ru.selm.manager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user", schema = "management")
public class CUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @ManyToMany
    @JoinTable(
            name = "t_user_authority", schema = "management",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_authority")
    )
    private List<Authority> authorities;

}
