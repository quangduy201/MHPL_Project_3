package com.example.project_3.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class XuLyRequest {
    @NotBlank(message = "Mã thành viên không được để trống.")
    private String maTV;

    @NotBlank(message = "Hình thức xử lý không được để trống.")
    @Size(max = 100, message = "Hình thức xử lý không được dài quá 100 kí tự.")
    private String hinhThucXL;

    private String soTien;

    @NotBlank(message = "Ngày xử lý không được để trống.")
    private String ngayXL;

    private Integer trangThaiXL;
}
