package com.pitchain.repository;

import com.pitchain.dto.PreferenceInfoDto;
import com.pitchain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("""
                SELECT new com.pitchain.dto.PreferenceInfoDto(
                    m.id,
                    bm.id,
                    COALESCE(sp.viewTime, 0),
                    CASE WHEN mbh.id IS NOT NULL THEN true ELSE false END,
                    CASE WHEN i.id IS NOT NULL THEN true ELSE false END
                )
                FROM Member m
                CROSS JOIN Bm bm
                LEFT JOIN MyBmHistory mbh ON bm.id = mbh.bm.id AND mbh.member.id = m.id
                LEFT JOIN MySpHistory sp ON bm.id = sp.bm.id AND sp.member.id = m.id
                LEFT JOIN Investment  i ON bm.id = i.bm.id AND i.member.id = m.id
            """)
    List<PreferenceInfoDto> getMemberPreferenceInfos();
}
