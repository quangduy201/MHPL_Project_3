package com.example.project_3.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "thietbi")
public class ThietBi {
    @Id
    @Column(name = "MaTB", nullable = false)
    private Long maTB;

    @Size(max = 100)
    @NotNull
    @Column(name = "TenTB", nullable = false, length = 100)
    private String tenTB;

    @Size(max = 2000)
    @Column(name = "MoTaTB", length = 2000)
    private String moTaTB;

}