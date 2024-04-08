package com.danilloyal.passin.services;

import com.danilloyal.passin.domain.attendee.Attendee;
import com.danilloyal.passin.domain.event.Event;
import com.danilloyal.passin.domain.event.exception.EventFullException;
import com.danilloyal.passin.domain.event.exception.EventNotFoundException;
import com.danilloyal.passin.dto.attendee.AttendeeIdDTO;
import com.danilloyal.passin.dto.attendee.AttendeeRequestDTO;
import com.danilloyal.passin.dto.event.EventIdDTO;
import com.danilloyal.passin.dto.event.EventReponseDTO;
import com.danilloyal.passin.dto.event.EventRequestDTO;
import com.danilloyal.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService  attendeeService;

    public EventReponseDTO getEventDetails(String eventId){
        Event event = getEventById(eventId);
        List<Attendee> attendeeList = attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventReponseDTO(event, attendeeList.size());
    }

    public EventIdDTO createEvent(EventRequestDTO eventRequestDTO){
        Event newEvent = new Event();
        newEvent.setTitle(eventRequestDTO.title());
        newEvent.setDetails(eventRequestDTO.details());
        newEvent.setSlug(this.createSlug(eventRequestDTO.title()));
        newEvent.setMaximumAttendees(eventRequestDTO.maximumAttendees());

        this.eventRepository.save(newEvent);

        return new EventIdDTO(newEvent.getId());
    }

    private String createSlug(String text){
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "")
                .toLowerCase();
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO){
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);

        Event event = getEventById(eventId);
        List<Attendee> attendeeList = attendeeService.getAllAttendeesFromEvent(eventId);

        if(event.getMaximumAttendees() <= attendeeList.size()) throw new EventFullException("Event is full");

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);
        return new AttendeeIdDTO(newAttendee.getId());
    }

    private Event getEventById(String eventId){
        return this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId));
    }
}
