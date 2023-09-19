package com.tsa.movieland.entity;

import com.tsa.movieland.common.Role;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Setter
@ToString
@Entity
@Table(name = "user_credentials")
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    @Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private int userId;
    @Column(name = "md5_password")
    private String password;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private Role role;
}
