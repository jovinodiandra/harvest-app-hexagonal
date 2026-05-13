package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateUserCommand;
import org.harvest.application.dto.result.CreateUserResult;
import org.harvest.application.dto.value.Role;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.PasswordSecurity;
import org.harvest.application.port.outbound.UserRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.User;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class CreateUserUseCase extends AuthenticationUseCase<CreateUserCommand, CreateUserResult> {
    private final UserRepository userRepository;
    private final PasswordSecurity passwordSecurity;

    public CreateUserUseCase(UserRepository userRepository, PasswordSecurity passwordSecurity) {
        this.userRepository = userRepository;
        this.passwordSecurity = passwordSecurity;
    }

    @Override
    protected UserSession authenticate(CreateUserCommand command) {
        return command.userSession();
    }

    @Override
    protected CreateUserResult executeBusiness(CreateUserCommand command, UserSession userSession) {
        if (userRepository.existsByEmail(command.email())){
            throw new ValidationException("Email already exists");
        }

        if (userSession.getRole() != Role.OWNER || userSession.getRole() != Role.ADMIN){
            throw new ValidationException("role must be owner or admin");
        }
        User user = new User(userRepository.nextId(), command.name(), command.email(), passwordSecurity.hash(command.password()), userSession.getOrganizationId(), Role.USER);
        userRepository.save(user);


        return new CreateUserResult(user.id(), user.name(),user.email(),user.role(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(CreateUserCommand command) {
        if (command.name()==null){
            throw new ValidationException("Name cannot be null");
        }

        if (command.email()==null){
            throw new ValidationException("Email cannot be null");
        }
        if (command.password()==null){
            throw new ValidationException("Password cannot be null");
        }

    }
}
