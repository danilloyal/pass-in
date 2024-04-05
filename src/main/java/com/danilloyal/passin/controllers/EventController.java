package com.danilloyal.passin.controllers;

import com.danilloyal.passin.dto.event.EventIdDTO;
import com.danilloyal.passin.dto.event.EventReponseDTO;
import com.danilloyal.passin.dto.event.EventRequestDTO;
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
}
