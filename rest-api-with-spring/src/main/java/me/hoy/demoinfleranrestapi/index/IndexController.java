package me.hoy.demoinfleranrestapi.index;

import me.hoy.demoinfleranrestapi.events.EventController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel<?> index(){
        return RepresentationModel.of(null)
                .add(linkTo(EventController.class).withRel("events"));
    }
}
