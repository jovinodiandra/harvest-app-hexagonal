package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateContactCommand;
import org.harvest.application.dto.result.CreateContactResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.ContactRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.Contact;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class CreateContactUseCase extends AuthenticationUseCase<CreateContactCommand, CreateContactResult> {

    private final ContactRepository contactRepository;

    public CreateContactUseCase(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    protected UserSession authenticate(CreateContactCommand command) {
        return command.session();
    }

    @Override
    protected CreateContactResult executeBusiness(CreateContactCommand command, UserSession userSession) {
        Contact contact = new Contact(contactRepository.nextId(), command.supplierId(), command.name(), command.address(), command.phone(), command.email(), command.notes());
        contactRepository.save(contact);
        return new CreateContactResult(contact.id(), contact.supplierId(), contact.name(), contact.email(), contact.phone(), contact.address(), contact.notes(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(CreateContactCommand command) {
        if (command.supplierId() == null) {
            throw new ValidationException("Supplier Id cannot be null");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new ValidationException("Name cannot be empty");
        }
        if (command.address() == null || command.address().isBlank()) {
            throw new ValidationException("Address cannot be empty");
        }
        if (command.phone() == null || command.phone().isBlank()) {
            throw new ValidationException("Phone cannot be empty");
        }
        if (command.email() == null || command.email().isBlank()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (command.notes() == null || command.notes().isBlank()) {
            throw new ValidationException("Notes cannot be empty");
        }
    }
}
