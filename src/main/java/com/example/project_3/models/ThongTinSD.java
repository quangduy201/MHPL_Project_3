package com.example.project_3.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "thongtinsd")
public class ThongTinSD {
    @Id
    @Column(name = "MaTT", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maTT;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MaTV", nullable = false)
    private ThanhVien maTV;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "MaTB")
    private ThietBi maTB;

    @Column(name = "TGVao")
    private Instant tgVao;

    @Column(name = "TGMuon")
    private Instant tgMuon;

    @Column(name = "TGTra")
    private Instant tgTra;

    @Column(name = "TGDatcho")
    private Instant tgDatcho;

    @Column(name = "TrangThai")
    private Integer trangThai;

    // Phương thức toString để in ra giá trị của tất cả các thuộc tính
    @Override
    public String toString() {
        return "ThongTinSD{" +
                "maTT=" + maTT +
                ", maTV=" + maTV +
                ", maTB=" + maTB +
                ", tgVao=" + tgVao +
                ", tgMuon=" + tgMuon +
                ", tgTra=" + tgTra +
                ", tgDatcho=" + tgDatcho +
                ", trangThai=" + trangThai +
                '}';
    }
}
