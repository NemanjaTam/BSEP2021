package com.project.securitybackend.entity;

import com.project.securitybackend.util.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUser extends User {

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.PENDING;

    private LocalDateTime confirmationTime = LocalDateTime.now();

    private boolean isUserConfirmAccount = false;

}
