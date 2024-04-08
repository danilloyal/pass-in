package com.danilloyal.passin.dto.attendee;

import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public record AttendeeDetails(String id, String name, String email, LocalDateTime createdAt, LocalDateTime checkedInAt) {
}
