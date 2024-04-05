package com.danilloyal.passin.services;

import com.danilloyal.passin.domain.attendee.Attendee;
import com.danilloyal.passin.domain.event.Event;
import com.danilloyal.passin.domain.event.exception.EventNotFoundException;
import com.danilloyal.passin.dto.event.EventIdDTO;
import com.danilloyal.passin.dto.event.EventReponseDTO;
import com.danilloyal.passin.dto.event.EventRequestDTO;
import com.danilloyal.passin.repositories.AttendeeRepository;
import com.danilloyal.passin.repositories.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeRepository attendeeRepository;

    public EventReponseDTO getEventDetails(String eventId){
        Event event = this.eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found with Id: " + eventId));
        List<Attendee> attendeeList = this.attendeeRepository.findByEventId(eventId);
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
}
