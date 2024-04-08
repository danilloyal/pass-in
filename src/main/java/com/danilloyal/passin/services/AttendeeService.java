package com.danilloyal.passin.services;

import com.danilloyal.passin.domain.attendee.Attendee;
import com.danilloyal.passin.domain.attendee.exception.AttendeeAlreadyRegisteredException;
import com.danilloyal.passin.domain.attendee.exception.AttendeeNotFoundException;
import com.danilloyal.passin.domain.check_in.Checkin;
import com.danilloyal.passin.dto.attendee.AttendeeBadgeResponseDTO;
import com.danilloyal.passin.dto.attendee.AttendeeDetails;
import com.danilloyal.passin.dto.attendee.AttendeeListResponseDTO;
import com.danilloyal.passin.dto.attendee.AttendeeBadgeDTO;
import com.danilloyal.passin.repositories.AttendeeRepository;
import com.danilloyal.passin.repositories.CheckinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;
    private final CheckInService checkInService;

    public List<Attendee> getAllAttendeesFromEvent(String eventId){
        return this.attendeeRepository.findByEventId(eventId);
    }

    public AttendeeListResponseDTO getEventAttendees(String eventId){
        List<Attendee> attendeeList = this.getAllAttendeesFromEvent(eventId);

        List<AttendeeDetails> attendeeDetailsList = attendeeList.stream().map(attendee -> {
            Optional<Checkin> checkin = this.checkInService.getCheckIn(attendee.getId());
            LocalDateTime checkInAt = checkin.isPresent() ? checkin.get().getCreatedAt() : null;
            return new AttendeeDetails(attendee.getId(), attendee.getName(), attendee.getEmail(), attendee.getCreatedAt(), checkInAt);
        }).toList();

        return new AttendeeListResponseDTO(attendeeDetailsList);
    }

    public Attendee registerAttendee(Attendee newAttendee){
        this.attendeeRepository.save(newAttendee);
        return newAttendee;
    }

    public void verifyAttendeeSubscription(String email, String eventId){
        Optional<Attendee> isAttendeeRegistered = this.attendeeRepository.findByEventIdAndEmail(eventId, email);
        if(isAttendeeRegistered.isPresent()) throw new AttendeeAlreadyRegisteredException("Attendee is already registered");
    }

    public AttendeeBadgeResponseDTO getAttendeeBadge(String attendeeId, UriComponentsBuilder uriComponentsBuilder){
        Attendee attendee = getAttendee(attendeeId);

        var uri = uriComponentsBuilder.path("/attendee/{attendeeId}/check-in").buildAndExpand(attendeeId).toUri().toString();

        AttendeeBadgeDTO attendeeBadgeDTO = new AttendeeBadgeDTO(attendee.getName(), attendee.getEmail(), uri, attendee.getEvent().getId());

        return new AttendeeBadgeResponseDTO(attendeeBadgeDTO);
    }

    private Attendee getAttendee(String attendeeId){
        return this.attendeeRepository.findById(attendeeId).orElseThrow(() -> new AttendeeNotFoundException("Attendee not found with ID: " + attendeeId));
    }

    public void checkInAttendee(String attendeeId){
        Attendee attendee = getAttendee(attendeeId);
        this.checkInService.registerCheckIn(attendee);
    }
}
