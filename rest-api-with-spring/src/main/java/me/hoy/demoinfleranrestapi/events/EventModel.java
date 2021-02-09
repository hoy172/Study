package me.hoy.demoinfleranrestapi.events;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class EventModel extends RepresentationModel<EventModel> {

    @JsonUnwrapped
    private Event event;


    public EventModel() {
        super();
    }
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
// 없어져 버렷어
//public class EventResource extends EntityModel<Event> {
//    public EventResource(Event content, Link... links) {
//        super(content, links);
//    }
//}
