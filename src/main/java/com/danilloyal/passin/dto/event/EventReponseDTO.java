package com.danilloyal.passin.dto.event;

import com.danilloyal.passin.domain.event.Event;
import lombok.Getter;

@Getter
public class EventReponseDTO {
    EventDetailDTO event;

    public EventReponseDTO(Event event, Integer amount){
        this.event = new EventDetailDTO(event.getId(), event.getTitle(), event.getDetails(), event.getSlug(), event.getMaximumAttendees(), amount);
    }
}
