package org.example.fujitsu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, WeatherId> {

}
