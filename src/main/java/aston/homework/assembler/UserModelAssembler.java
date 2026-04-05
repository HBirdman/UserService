package aston.homework.assembler;

import aston.homework.controller.UserController;
import aston.homework.dto.UserResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler {

    public UserResponseDTO toModel(UserResponseDTO dto) {
        Link selfLink = linkTo(methodOn(UserController.class)
                .showUserById(dto.getId())).withSelfRel();

        Link allUsersLink = linkTo(methodOn(UserController.class)
                .showAllUsers()).withRel("all");

        Link createLink = linkTo(methodOn(UserController.class)
                .createUser(null)).withRel("create");

        Link updateLink = linkTo(methodOn(UserController.class)
                .updateUser(dto.getId(), null)).withRel("update");

        Link deleteLink = linkTo(UserController.class)
                .slash(dto.getId()).withRel("delete");

        return dto.add(selfLink,allUsersLink, createLink, updateLink, deleteLink);
    }
}
