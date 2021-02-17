package me.hoy.demoinfleranrestapi.common;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.hoy.demoinfleranrestapi.index.IndexController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsModel extends EntityModel<Errors> {

    public ErrorsModel() {
    }
}
