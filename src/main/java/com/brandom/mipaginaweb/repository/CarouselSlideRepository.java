package com.brandom.mipaginaweb.repository;

import java.util.List;

import com.brandom.mipaginaweb.model.CarouselSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CarouselSlideRepository extends JpaRepository<CarouselSlide, Long> {

    List<CarouselSlide> findByActivoTrueOrderByOrdenAsc();

    List<CarouselSlide> findAllByOrderByOrdenAsc();

    @Query("SELECT MAX(c.orden) FROM CarouselSlide c")
    Integer findMaxOrden();
}