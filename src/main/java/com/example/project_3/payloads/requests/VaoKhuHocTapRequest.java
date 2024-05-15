package com.example.project_3.payloads.requests;

import com.example.project_3.validators.OnlyNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VaoKhuHocTapRequest {
    @NotBlank(message = "Mã thành viên không được để trống.")
    @OnlyNumber(message = "Mã thành viên chỉ chứa số.")
    private String maTV;

    private String hoTen;

    private String khoa;

    private String nganh;

    private String sdt;

    private String email;

    private String credentials;
}
