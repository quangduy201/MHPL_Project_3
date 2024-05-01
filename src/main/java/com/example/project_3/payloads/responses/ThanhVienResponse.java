package com.example.project_3.payloads.responses;

import com.example.project_3.validators.VietnamesePhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ThanhVienResponse {
    private Long maTV;
    private String hoTen;
    private String khoa;
    private String nganh;
    private String sdt;
    private String email;
}

