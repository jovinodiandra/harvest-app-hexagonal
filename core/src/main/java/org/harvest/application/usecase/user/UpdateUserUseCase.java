package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateUserCommand;
import org.harvest.application.dto.result.UpdateUserResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.UserRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.User;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class UpdateUserUseCase extends AuthenticationUseCase<UpdateUserCommand, UpdateUserResult> {
    private final UserRepository userRepository;

    public UpdateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected UserSession authenticate(UpdateUserCommand command) {
        return command.session();
    }

    @Override
    protected UpdateUserResult executeBusiness(UpdateUserCommand command, UserSession userSession) {
        User user = userRepository.findById(command.userId());
        if (user == null) {
            throw new ValidationException("User not found");
        }
        User updateUser = user.UpdateRoleAndName( command.role(), command.name());
        userRepository.updateUser(updateUser);
        return new UpdateUserResult(user.id(), updateUser.name(), updateUser.email(), updateUser.role(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(UpdateUserCommand command) {
        if (command.name() == null && command.role() == null)  {
            throw new ValidationException("Name and Role cannot be null");
        }

        if (command.userId() == null) {
            throw new ValidationException("User Id cannot be null");
        }



    }
}
