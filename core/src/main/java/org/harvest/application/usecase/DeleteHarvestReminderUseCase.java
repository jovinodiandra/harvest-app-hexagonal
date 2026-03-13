package org.harvest.application.usecase;

import org.harvest.application.dto.command.DeleteHarvestReminderCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestReminderRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestReminder;
import org.harvest.shared.exception.ValidationException;

public class DeleteHarvestReminderUseCase extends AuthenticationUseCase<DeleteHarvestReminderCommand, DefaultResult> {
    private final HarvestReminderRepository harvestReminderRepository;

    public DeleteHarvestReminderUseCase(HarvestReminderRepository harvestReminderRepository) {
        this.harvestReminderRepository = harvestReminderRepository;

    }

    @Override
    protected UserSession authenticate(DeleteHarvestReminderCommand command) {
        return command.session();
    }

    @Override
    protected DefaultResult executeBusiness(DeleteHarvestReminderCommand command, UserSession userSession) {
        HarvestReminder harvestReminder = harvestReminderRepository.findById(command.id());
        if (harvestReminder == null) {
            throw new ValidationException("Harvest Reminder not found");
        }

        harvestReminderRepository.delete(harvestReminder);
        return new DefaultResult();
    }

    @Override
    protected void validateCommand(DeleteHarvestReminderCommand command) {
        if (command.id() == null) {
            throw new ValidationException("Harvest Reminder Id cannot be null");
        }
    }
}
