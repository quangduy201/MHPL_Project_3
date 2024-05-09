package com.example.project_3.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LayLaiMatKhauRequest {
    @NotBlank(message = "Mật khẩu cũ không được để trống.")
    private String matKhau;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống.")
    private String xacNhanMatKhau;

    private String token;

    public boolean isXacNhanMatKhauValid() {
        return matKhau != null && matKhau.equals(xacNhanMatKhau);
    }

}
