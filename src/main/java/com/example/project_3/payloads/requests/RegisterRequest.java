package com.example.project_3.payloads.requests;

import com.example.project_3.validators.MatchPassword;
import com.example.project_3.validators.OnlyNumber;
import com.example.project_3.validators.VietnamesePhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Tên thành viên không được để trống.")
    @Size(min = 4, message = "Họ tên phải chứa ít nhất 4 kí tự.")
    @Size(max = 100, message = "Họ tên không được dài quá 100 kí tự.")
    private String hoTen;

    @NotBlank(message = "Tên khoa không được để trống.")
    @Size(max = 100, message = "Tên khoa không được dài quá 100 kí tự.")
    private String khoa;

    @NotBlank(message = "Tên ngành không được để trống.")
    @Size(max = 100, message = "Tên ngành không được dài quá 100 kí tự.")
    private String nganh;

    @NotBlank(message = "SĐT không được để trống.")
    @VietnamesePhoneNumber(message = "SĐT không hợp lệ.")
    private String sdt;

    @NotBlank(message = "Email không được để trống.")
    @Email(message = "Email không hợp lệ.")
    private String email;

    @NotBlank(message = "Mã thành viên không được để trống.")
    @OnlyNumber(message = "Mã thành viên chỉ chứa số.")
    @Size(min = 10, message = "Mã thành viên phải chứa 10 kí tự.")
    @Size(max = 10, message = "Mã thành viên phải chứa 10 kí tự.")
    private String maTV;

    @NotBlank(message = "Mật khẩu không được để trống.")
    @Size(min = 2, message = "Họ tên phải chứa ít nhất 2 kí tự.")
    @Size(max = 9, message = "Họ tên phải chứa ít nhất 9 kí tự.")
    private String matKhau;

    @NotBlank(message = "Xác nhận mật khẩu không được để trống.")
    @Size(min = 2, message = "Họ tên phải chứa ít nhất 2 kí tự.")
    @Size(max = 9, message = "Họ tên phải chứa ít nhất 9 kí tự.")
    private String xacNhanMatKhau;

    public boolean isXacNhanMatKhauValid() {
        return matKhau != null && matKhau.equals(xacNhanMatKhau);
    }

    private String credentials;
}
