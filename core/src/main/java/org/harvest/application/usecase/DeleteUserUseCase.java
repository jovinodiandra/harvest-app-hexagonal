package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteUserCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.UserRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.User;
import org.harvest.shared.exception.ValidationException;

public class DeleteUserUseCase extends AuthenticationUseCase<DeleteUserCommand, DefaultResult> {
    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected UserSession authenticate(DeleteUserCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteUserCommand command, UserSession userSession) {
        User user = userRepository.findById(command.userId());
        if (user == null) {
            throw new ValidationException("User not found");
        }
        userRepository.delete(user);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteUserCommand command) {

        if (command.userId() == null){
            throw new ValidationException("User Id cannot be null");
        }

    }
}
