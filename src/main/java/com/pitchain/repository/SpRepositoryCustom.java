package com.pitchain.repository;

import com.pitchain.common.constant.SubCategory;
import com.pitchain.dto.QSpWithLikeDto;
import com.pitchain.dto.SpWithLikeDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pitchain.entity.QBm.bm;
import static com.pitchain.entity.QBmSubCategory.bmSubCategory;
import static com.pitchain.entity.QMyBm.myBm;
import static com.pitchain.entity.QSp.sp;

@RequiredArgsConstructor
@Repository
public class SpRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<SpWithLikeDto> getSpWithLikeDtoFilteredCategory(Long memberId, SubCategory category) {
        return queryFactory
                .select(new QSpWithLikeDto(
                        sp,
                        myBm.isNotNull()
                ))
                .from(sp)
                .leftJoin(sp.bm, bm)
                .leftJoin(myBm).on(myBm.bm.id.eq(bm.id).and(myBm.member.id.eq(memberId)))
                .leftJoin(bm.subCategories, bmSubCategory)
                .where(categoryFilter(category))
                .distinct()
                .fetch();
    }

    private BooleanExpression categoryFilter(SubCategory category) {
        return category == null ? null : bmSubCategory.subCategory.eq(category);
    }
}
