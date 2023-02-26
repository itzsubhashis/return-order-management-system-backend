package com.roms.auth.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "login")
public class User {
	@Id
    private long id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String roles;

}
