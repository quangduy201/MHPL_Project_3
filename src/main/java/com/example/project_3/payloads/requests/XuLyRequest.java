package com.example.project_3.payloads.requests;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class XuLyRequest {
    private int maXL;

    @NotBlank(message = "Mã thành viên không được để trống.")
    private String maTV;

    @NotBlank(message = "Hình thức xử lý không được để trống.")
    @Size(max = 100, message = "Hình thức xử lý không được dài quá 100 kí tự.")
    private String hinhThucXL;

    @NotEmpty(message = "Số tiền không được để trống.")
    @Pattern(regexp = "^(0|[1-9]\\d*)$", message = "Số tiền phải là số không âm.")
    private String soTien;

    @NotNull(message = "Ngày xử lý không được để trống.")
    private LocalDateTime ngayXL;

    private Boolean trangThaiXL;
}
