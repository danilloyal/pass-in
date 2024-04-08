package com.danilloyal.passin.repositories;

import com.danilloyal.passin.domain.attendee.Attendee;
import com.danilloyal.passin.domain.check_in.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckinRepository extends JpaRepository<Checkin, Integer> {
    Optional<Checkin> findByAttendeeId(String id);
}
