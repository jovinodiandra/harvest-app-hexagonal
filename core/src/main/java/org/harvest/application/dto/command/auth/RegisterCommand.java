package org.harvest.application.dto.command;

public record RegisterCommand(String name, String email, String password, String confirmPassword,
                              String organizationName) {

}
