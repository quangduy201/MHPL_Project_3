package com.example.project_3.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuenMatKhauRequest {
    @NotBlank(message = "Mật khẩu cũ không được để trống.")
    private String matKhauCu;


    @NotBlank(message = "Mật khẩu mới không được để trống.")
    private String matKhauMoi;


    @NotBlank(message = "Xác nhận mật khẩu mới không được để trống.")
    private String xacNhanMatKhauMoi;

    public boolean isXacNhanMatKhauMoiValid() {
        return matKhauMoi != null && matKhauMoi.equals(xacNhanMatKhauMoi);
    }

}
