package com.example.project_3.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "thanhvien")
public class ThanhVien {
    @Id
    @Column(name = "MaTV", nullable = false)
    private Long maTV;

    @Size(max = 100)
    @NotNull
    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Size(max = 100)
    @Column(name = "Khoa", length = 100)
    private String khoa;

    @Size(max = 100)
    @Column(name = "Nganh", length = 100)
    private String nganh;

    @Size(max = 15)
    @Column(name = "SDT", length = 15)
    private String sdt;

    @Size(max = 25)
    @NotNull
    @Email
    @Column(name = "Email", nullable = false, length = 25)
    private String email;

    @Size(max = 10)
    @Column(name = "Password", length = 10)
    private String password;
    
    @Column(name = "reset_password_token")
    private String resetPasswordToken;

}