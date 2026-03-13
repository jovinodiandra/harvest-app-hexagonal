package org.harvest.application.usecase;

import org.harvest.application.dto.command.UpdateHarvestReminderCommand;
import org.harvest.application.dto.result.DefaultResult;
import org.harvest.application.dto.result.UpdateHarvestReminderResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestReminderRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.domain.HarvestRecord;
import org.harvest.domain.HarvestReminder;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class UpdateHarvestReminderUseCase extends AuthenticationUseCase<UpdateHarvestReminderCommand, UpdateHarvestReminderResult> {
    private final HarvestReminderRepository harvestReminderRepository;
    private final PondsRepository pondsRepository;

    public UpdateHarvestReminderUseCase(HarvestReminderRepository harvestReminderRepository, PondsRepository pondsRepository) {
        this.harvestReminderRepository = harvestReminderRepository;
        this.pondsRepository = pondsRepository;
    }

    @Override
    protected UserSession authenticate(UpdateHarvestReminderCommand command) {
        return command.session();
    }

    @Override
    protected UpdateHarvestReminderResult executeBusiness(UpdateHarvestReminderCommand command, UserSession userSession) {
        Ponds ponds = pondsRepository.findById(command.pondId());
        if (ponds == null){
            throw new ValidationException("Ponds not found");
        }
        HarvestReminder harvestReminder = harvestReminderRepository.findById(command.id());
        if (harvestReminder == null){
            throw new ValidationException("HarvestRecord not found");
        }
        HarvestReminder harvestReminderUpdate = harvestReminder.update(command.reminderDate(), command.reminderTime(), command.notes());
        harvestReminderRepository.update(harvestReminderUpdate);
        return new UpdateHarvestReminderResult(harvestReminderUpdate.id(), harvestReminderUpdate.pondId(),harvestReminderUpdate.reminderDate(),harvestReminderUpdate.reminderTime(),harvestReminderUpdate.notes(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(UpdateHarvestReminderCommand command) {
        LocalDate reminderDate = command.reminderDate();
        LocalTime reminderTime = command.reminderTime();

        if (command.id() == null) {
            throw new ValidationException("harvest reminder cannot be null");
        }
        if (command.pondId() == null) {
            throw new ValidationException("pond id cannot be null");
        }
        if (reminderDate == null) {
            throw new ValidationException("reminder date cannot be null");
        }

        if (command.reminderDate().isBefore(LocalDate.now())) {
            throw new ValidationException("reminder date cannot be in the past");
        }
        if (reminderTime == null ) {
            throw new ValidationException("reminder time cannot be null");
        }

        LocalDateTime reminderDateTime = LocalDateTime.of(reminderDate,reminderTime);
        if (reminderDateTime.isBefore(LocalDateTime.now()) ) {
            throw new ValidationException("reminder time cannot be in the past");
        }
    }
}
