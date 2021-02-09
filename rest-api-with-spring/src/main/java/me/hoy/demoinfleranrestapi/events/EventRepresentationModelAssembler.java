package me.hoy.demoinfleranrestapi.events;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EventRepresentationModelAssembler extends RepresentationModelAssemblerSupport<Event, EventModel> {


    public EventRepresentationModelAssembler(Class<?> controllerClass, Class<EventModel> resourceType) {
        super(controllerClass, resourceType);
    }

    public EventRepresentationModelAssembler() {
        super(EventController.class, EventModel.class);
    }

    @Override
    public EventModel toModel(Event entity) {
        EventModel resource = instantiateModel(entity);
        resource.add(linkTo(methodOn(EventController.class).createEvent(EventDto.builder().build(), null)).slash(entity.getId()).withSelfRel())
                .add(linkTo(methodOn(EventController.class).createEvent(null, null)).withRel("query-events"))
                .add(linkTo(methodOn(EventController.class).createEvent(null, null)).slash(entity.getId()).withRel("update-event"))
                ;
        resource.setEvent(entity);
        return resource;
    }
}
