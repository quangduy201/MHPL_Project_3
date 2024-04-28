package com.example.project_3.payloads.requests;

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
public class ThietBiRequest {
    @NotBlank(message = "Tên thiết bị không được để trống.")
    @Size(min = 4, message = "Tên thiết bị phải chứa ít nhất 4 kí tự.")
    @Size(max = 100, message = "Tên thiết bị không được dài quá 100 kí tự.")
    private String tenTB;

    @NotBlank(message = "Mô tả không được để trống.")
    @Size(max = 100, message = "Mô tả không được dài quá 100 kí tự.")
    private String moTaTB;
}
