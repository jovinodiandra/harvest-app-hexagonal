package org.harvest.application.usecase;

import org.harvest.application.dto.query.ViewUserQuery;
import org.harvest.application.dto.result.ViewUserResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.UserRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.User;
import org.harvest.shared.exception.ValidationException;

import java.util.List;

public class ViewUserUseCase extends AuthenticationUseCase<ViewUserQuery, ViewUserResult> {
    private final UserRepository userRepository;

    public ViewUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected UserSession authenticate(ViewUserQuery command) {
        return command.session();
    }

    @Override
    protected ViewUserResult executeBusiness(ViewUserQuery command, UserSession userSession) {
        int offset = (command.page() -1 ) * command.limit();
        List<User> user = userRepository.findAllByOrganizationId(userSession.getOrganizationId(),offset,command.limit());
        return new ViewUserResult(user);
    }

    @Override
    protected void validateCommand(ViewUserQuery command) {
if (command.page() < 1){
    throw new ValidationException("Page must be greater than 0");
}

if(command.limit()<1){
    throw new ValidationException("Limit must be greater than 0");
}
    }
}
