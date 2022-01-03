package com.github.elielcena.domain.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author elielcena
 *
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USERS",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "USERNAME"),
            @UniqueConstraint(columnNames = "EMAIL")
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Length(max = 20)
    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Length(max = 50)
    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Length(max = 120)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public boolean isNew() {
        return getId() == null;
    }

    public User addOrRemoveRole(Boolean isAdss, Role role) {
        if (Boolean.TRUE.equals(isAdss))
            getRoles().add(role);
        else
            getRoles().remove(role);

        return this;
    }

}
