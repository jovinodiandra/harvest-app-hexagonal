package org.harvest.application.usecase;

import org.harvest.application.dto.command.LoginCommand;
import org.harvest.application.dto.result.LoginResult;
import org.harvest.application.dto.value.SessionCreationRequest;
import org.harvest.application.port.inbound.BaseUseCase;
import org.harvest.application.port.outbound.OrganizationRepository;
import org.harvest.application.port.outbound.PasswordSecurity;
import org.harvest.application.port.outbound.UserRepository;
import org.harvest.application.port.outbound.security.SessionManager;
import org.harvest.domain.Organization;
import org.harvest.domain.User;
import org.harvest.shared.exception.ValidationException;

public class LoginUseCase extends BaseUseCase<LoginCommand, LoginResult> {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final SessionManager sessionManager;
    private final PasswordSecurity passwordSecurity;


    public LoginUseCase(UserRepository userRepository, OrganizationRepository organizationRepository, SessionManager sessionManager, PasswordSecurity passwordSecurity) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.sessionManager = sessionManager;
        this.passwordSecurity = passwordSecurity;
    }

    @Override
    protected LoginResult executeBusiness(LoginCommand command) {
        User user = userRepository.findByEmail(command.email());
        if (user == null ||  !passwordSecurity.matches(command.password(), user.password())) {
            throw new ValidationException("Invalid email or password");
        }
        Organization organization = organizationRepository.findById(user.organizationId());
        SessionCreationRequest sessionCreationRequest = new SessionCreationRequest(user.id(), organization.id(), user.role());
        String sessionId = sessionManager.createSession(sessionCreationRequest);
        return new LoginResult(
                user.id(), user.name(), user.email(), user.role(), organization.id(), organization.name(), sessionId
        );
    }

    @Override
    protected void validateCommand(LoginCommand command) {
        if (command.email() == null) {
            throw new ValidationException("Email cannot be null");
        }
        if (command.password() == null) {
            throw new ValidationException("Password cannot be null");
        }

    }
}
