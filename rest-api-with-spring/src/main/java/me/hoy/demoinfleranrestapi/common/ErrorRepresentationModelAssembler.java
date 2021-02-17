package me.hoy.demoinfleranrestapi.common;

import me.hoy.demoinfleranrestapi.index.IndexController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.validation.Errors;

public class ErrorRepresentationModelAssembler extends RepresentationModelAssemblerSupport<Errors,ErrorsModel> {

    public ErrorRepresentationModelAssembler(Class<?> controllerClass, Class<ErrorsModel> resourceType) {
        super(controllerClass, resourceType);
    }
    @Override
    public ErrorsModel toModel(Errors entity) {
        return null;
    }
}
