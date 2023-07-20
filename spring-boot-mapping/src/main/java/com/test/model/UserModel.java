package com.test.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserModel {

    @Id
    @SequenceGenerator(name = "od", sequenceName = "id", initialValue = 2000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id")
    private int userId;
    private String userName;
    private String email;
    private String password;


}
