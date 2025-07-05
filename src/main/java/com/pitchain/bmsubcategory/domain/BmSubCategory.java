package com.pitchain.bmsubcategory.domain;

import com.pitchain.bm.domain.Bm;
import com.pitchain.common.constant.SubCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BmSubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bm_sub_category_id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "bm_id", nullable = false)
    private Bm bm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubCategory subCategory;

    public static BmSubCategory create(Bm bm, SubCategory subCategory) {
        BmSubCategory bmSubCategory = new BmSubCategory();
        bmSubCategory.bm = bm;
        bmSubCategory.subCategory = subCategory;
        return bmSubCategory;
    }
}
