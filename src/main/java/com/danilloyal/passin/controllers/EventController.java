package com.danilloyal.passin.controllers;

import com.danilloyal.passin.dto.attendee.AttendeeIdDTO;
import com.danilloyal.passin.dto.attendee.AttendeeListResponseDTO;
import com.danilloyal.passin.dto.attendee.AttendeeRequestDTO;
import com.danilloyal.passin.dto.event.EventIdDTO;
import com.danilloyal.passin.dto.event.EventReponseDTO;
import com.danilloyal.passin.dto.event.EventRequestDTO;
import com.danilloyal.passin.services.AttendeeService;
import com.danilloyal.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{id}")
    public ResponseEntity<EventReponseDTO> getEvent(@PathVariable String id){
        EventReponseDTO eventReponseDTO= this.eventService.getEventDetails(id);
        return ResponseEntity.ok(eventReponseDTO);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);

        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();

        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @GetMapping("/attendees/{id}")
    public ResponseEntity<AttendeeListResponseDTO> getEventAttendees(@PathVariable String id){
        AttendeeListResponseDTO attendeeListResponseDTO= this.attendeeService.getEventAttendees(id);
        return ResponseEntity.ok(attendeeListResponseDTO);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder){
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);
        var uri = uriComponentsBuilder.path("/attendees/{attendId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();

        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }
}
