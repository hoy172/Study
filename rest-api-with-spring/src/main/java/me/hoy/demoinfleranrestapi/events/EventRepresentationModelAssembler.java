package me.hoy.demoinfleranrestapi.events;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class EventRepresentationModelAssembler extends RepresentationModelAssemblerSupport<Event, EventModel> {
    /**
     * Creates a new {@link RepresentationModelAssemblerSupport} using the given controller class and resource type.
     *
     * @param controllerClass must not be {@literal null}.
     * @param resourceType    must not be {@literal null}.
     */
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
                .add(Link.of("/docs/index.html#resources-events-create").withRel("profile"))
        ;
        resource.setEvent(entity);
        return resource;
    }
}
