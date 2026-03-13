package org.harvest.application.usecase;

import org.harvest.application.dto.command.RegisterCommand;
import org.harvest.application.dto.result.RegisterResult;
import org.harvest.application.dto.value.Role;
import org.harvest.application.port.inbound.BaseUseCase;
import org.harvest.application.port.outbound.OrganizationRepository;
import org.harvest.application.port.outbound.PasswordSecurity;
import org.harvest.application.port.outbound.UserRepository;
import org.harvest.domain.Organization;
import org.harvest.domain.User;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class RegisterUseCase extends BaseUseCase<RegisterCommand, RegisterResult> {
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordSecurity passwordSecurity;
    public RegisterUseCase(UserRepository userRepository, OrganizationRepository organizationRepository, PasswordSecurity passwordSecurity) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.passwordSecurity = passwordSecurity;
    }

    @Override
    protected void validateCommand(RegisterCommand command) {
        if (command.password() == null) {
            throw new ValidationException("Password cannot be null");
        }

        if (command.confirmPassword() == null) {
            throw new ValidationException("Confirm Password cannot be null");
        }

        if (!command.password().equals(command.confirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }
        if (command.email() == null) {
            throw new ValidationException("Email cannot be null");
        }

        if (command.name() == null) {
            throw new ValidationException("Name cannot be null");
        }

        if (command.organizationName() == null) {
            throw new ValidationException("Organization Name cannot be null");
        }
    }

    @Override
    protected RegisterResult executeBusiness(RegisterCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ValidationException("Email already exists");
        }
        Organization organization = new Organization(
                organizationRepository.nextId(),
                command.organizationName()
        );
        organizationRepository.save(organization);
        User user = new User(userRepository.nextId(), command.name(), command.email(),passwordSecurity.hash(command.password()), organization.id(), Role.OWNER);
        userRepository.save(user);
        return new RegisterResult(user.id(), organization.id(), command.name(), command.email(), command.organizationName(), user.role().getDescription(), LocalDateTime.now());
    }
}
