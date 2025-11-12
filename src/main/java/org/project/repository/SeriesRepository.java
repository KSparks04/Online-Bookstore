package org.project.repository;

import org.project.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SeriesRepository extends JpaRepository<Series,Integer> {
    public Series findBySeriesName(String seriesName);
    public Series findBySeriesCode(int seriesCode);
}
