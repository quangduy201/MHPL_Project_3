package com.example.project_3.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "xuly")
public class XuLy {
    @Id
    @Column(name = "MaXL", nullable = false)
    private Integer maXL;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MaTV", nullable = false)
    private ThanhVien maTV;

    @Size(max = 250)
    @Column(name = "HinhThucXL", length = 250)
    private String hinhThucXL;

    @Column(name = "SoTien")
    private Integer soTien;

    @Column(name = "NgayXL")
    private Instant ngayXL;

    @Column(name = "TrangThaiXL")
    private Integer trangThaiXL;

}