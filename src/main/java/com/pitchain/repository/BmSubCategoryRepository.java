package com.pitchain.repository;

import com.pitchain.entity.BmSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BmSubCategoryRepository extends JpaRepository<BmSubCategory, Long> {
    List<BmSubCategory> findAllByBmId(Long bmId);

    void deleteAllByBmId(Long bmId);
}
