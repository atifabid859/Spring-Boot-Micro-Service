package com.lcwd.user.service.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "micro_users")
//@org.hibernate.annotations.Table(appliesTo = "micro_users", comment = "ENGINE=MyISAM")
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "about")
    private String about;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Transient
    private List<Rating> ratings;
}
