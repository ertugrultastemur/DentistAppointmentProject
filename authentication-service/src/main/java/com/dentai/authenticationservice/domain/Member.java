package com.dentai.authenticationservice.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE mistakes SET is_deleted = true WHERE id=id")
@SQLRestriction("is_deleted=false")
public class Member {

    public Member(String email, String password, Set<String> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @ElementCollection
    private Set<String> roles;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    
}
