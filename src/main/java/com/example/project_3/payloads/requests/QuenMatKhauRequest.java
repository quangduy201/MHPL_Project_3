package com.example.project_3.payloads.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QuenMatKhauRequest {
    @NotBlank(message = "Mật khẩu cũ không được để trống.")
    @Size(min = 2, message = "Họ tên phải chứa ít nhất 2 kí tự.")
    @Size(max = 9, message = "Họ tên phải chứa ít nhất 9 kí tự.")
    private String matKhauCu;


    @NotBlank(message = "Mật khẩu mới không được để trống.")
    @Size(min = 2, message = "Họ tên phải chứa ít nhất 2 kí tự.")
    @Size(max = 9, message = "Họ tên phải chứa ít nhất 9 kí tự.")
    private String matKhauMoi;


    @NotBlank(message = "Xác nhận mật khẩu mới không được để trống.")
    @Size(min = 2, message = "Họ tên phải chứa ít nhất 2 kí tự.")
    @Size(max = 9, message = "Họ tên phải chứa ít nhất 9 kí tự.")
    private String xacNhanMatKhauMoi;

    public boolean isXacNhanMatKhauMoiValid() {
        return matKhauMoi != null && matKhauMoi.equals(xacNhanMatKhauMoi);
    }

}
