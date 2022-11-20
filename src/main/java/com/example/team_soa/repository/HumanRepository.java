package com.example.team_soa.repository;

import com.example.team_soa.model.Car;
import com.example.team_soa.model.Human;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface HumanRepository extends CrudRepository<Human, Long>, JpaRepository<Human, Long>, PagingAndSortingRepository<Human, Long> {
    List<Human> findByName(String name);


    @Query(value = "SELECT human.id, " +
            "human.name, " +
            "human.creation_date, " +
            "human.real_hero, " +
            "human.has_toothpick, " +
            "human.impact_speed, " +
            "human.soundtrack_name, " +
            "human.minutes_of_waiting, " +
            "human.mood, " +
            "human.is_driver, " +
            "coordinate.id AS coordinate_id, " +
            "coordinate.x, " +
            "coordinate.y, " +
            "car.id AS car_id, " +
            "car.name AS car_name, " +
            "car.cool, " +
            "car.max_seats " +
            "FROM human " +
            "LEFT JOIN coordinate ON human.coordinate_id = coordinate.id " +
            "LEFT JOIN car ON human.car_id = car.id " +
            "WHERE (LOWER(human.name) like LOWER(:name)) " +
            "AND human.creation_date >= :creationDate_min and human.creation_date <= :creationDate_max " +
            "AND (:realHero IS NULL OR COALESCE(CAST(CAST(:realHero AS CHARACTER VARYING) AS BOOLEAN), human.real_hero) = human.real_hero )" +
            "AND (:hasToothpick IS NULL OR COALESCE(CAST(CAST(:hasToothpick AS CHARACTER VARYING) AS BOOLEAN), human.has_toothpick) = human.has_toothpick )" +
            "AND human.impact_speed >= :impactSpeed_min and human.impact_speed <= :impactSpeed_max " +
            "AND (LOWER(human.soundtrack_name) like LOWER(:soundtrackName)) " +
            "AND human.minutes_of_waiting >= :minutesOfWaiting_min and human.minutes_of_waiting <= :minutesOfWaiting_max " +
            "AND (LOWER(human.mood) like LOWER(:mood)  or :real_mood is null)" +
            "AND coordinate.x >= :x_min and coordinate.x <= :x_max " +
            "AND coordinate.y >= :y_min and coordinate.y <= :y_max " +
            "AND ((LOWER(car.name) like LOWER(:carName)) or :real_carName is null) " +
            "and ((:carCool IS NULL OR COALESCE(CAST(CAST(:carCool AS CHARACTER VARYING) AS BOOLEAN), car.cool) = car.cool )) " +
            "and ((:carMaxSeats_min <= car.max_seats and car.max_seats  <= :carMaxSeats_max)  or (:real_carMaxSeats_min is null and :real_carMaxSeats_max is null) )" +
            "AND (:isDriver IS NULL OR COALESCE(CAST(CAST(:isDriver AS CHARACTER VARYING) AS BOOLEAN), human.is_driver) = human.is_driver )"
            , nativeQuery = true)
    Stream<Object[]> findHumanFilter(Pageable pageable,
                                     String name,
                                     LocalDate creationDate_min,
                                     LocalDate creationDate_max,
                                     Boolean realHero,
                                     Boolean hasToothpick,
                                     Float impactSpeed_min,
                                     Float impactSpeed_max,
                                     String soundtrackName,
                                     Integer minutesOfWaiting_min,
                                     Integer minutesOfWaiting_max,
                                     String real_mood,
                                     String mood,
                                     Integer x_min,
                                     Integer x_max,
                                     Integer y_min,
                                     Integer y_max,
                                     String real_carName,
                                     String carName,
                                     Boolean carCool,
                                     Integer real_carMaxSeats_min,
                                     Integer real_carMaxSeats_max,
                                     Integer carMaxSeats_min,
                                     Integer carMaxSeats_max,
                                     Boolean isDriver);


    List<Human> findHumansByCarId(Long id);

    List<Human> findHumansBySoundtrackName(String soundtrackName);


    List<Human> findHumansByImpactSpeedGreaterThan(Float amount);


    List<Human> findHumansByMinutesOfWaiting(Integer amount);
}
