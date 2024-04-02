package com.danilloyal.passin.repositories;

import com.danilloyal.passin.domain.check_in.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckinRepository extends JpaRepository<Checkin, Integer> {
}
