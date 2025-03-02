package com.pitchain.repository;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.dto.QSpWithLikeDto;
import com.pitchain.dto.SpWithLikeDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pitchain.entity.QBm.bm;
import static com.pitchain.entity.QSp.sp;
import static com.pitchain.entity.QSpLike.spLike;

@RequiredArgsConstructor
@Repository
public class SpRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<SpWithLikeDto> getSpWithLikeDtoFilteredCategory(Long memberId, MainCategory category) {
        return queryFactory
                .select(new QSpWithLikeDto(
                        sp,
                        spLike.isNotNull()
                ))
                .from(sp)
                .leftJoin(sp.bm, bm)
                .leftJoin(spLike).on(spLike.sp.id.eq(sp.id).and(spLike.member.id.eq(memberId)))
                .where(eqMainCategory(category))
                .distinct()
                .fetch();
    }

    private static BooleanExpression eqMainCategory(MainCategory category) {
        return bm.mainCategory.eq(category);
    }
}
