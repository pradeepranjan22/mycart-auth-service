package com.secor.mycartauthservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "credentials")
public class Credential {

    @Id
    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "password", length = 50)
    private String password;

}