package org.harvest.application.usecase;

import org.harvest.application.dto.command.CreateHarvestReminderCommand;
import org.harvest.application.dto.result.CreateHarvestReminderResult;
import org.harvest.application.port.inbound.AuthenticationUseCase;
import org.harvest.application.port.outbound.HarvestReminderRepository;
import org.harvest.application.port.outbound.PondsRepository;
import org.harvest.application.port.outbound.security.UserSession;
import org.harvest.application.port.service.ReminderSchedulerService;
import org.harvest.domain.HarvestReminder;
import org.harvest.domain.Ponds;
import org.harvest.shared.exception.ValidationException;

import java.time.LocalDateTime;

public class CreateHarvestReminderUseCase extends AuthenticationUseCase<CreateHarvestReminderCommand, CreateHarvestReminderResult> {
    private final HarvestReminderRepository harvestReminderRepository;
    private final PondsRepository pondsRepository;
    private final ReminderSchedulerService reminderSchedulerService;

    public CreateHarvestReminderUseCase(HarvestReminderRepository harvestReminderRepository, PondsRepository pondsRepository, ReminderSchedulerService reminderSchedulerService) {
        this.harvestReminderRepository = harvestReminderRepository;
        this.pondsRepository = pondsRepository;
        this.reminderSchedulerService = reminderSchedulerService;
    }



    @Override
    protected UserSession authenticate(CreateHarvestReminderCommand command) {
        return command.session();
    }

    @Override
    protected CreateHarvestReminderResult executeBusiness(CreateHarvestReminderCommand command, UserSession userSession) {
        Ponds pond = pondsRepository.findById(command.pondId());
        if (pond == null || !pond.organizationId().equals(userSession.getOrganizationId())) {
            throw new IllegalArgumentException("Pond not found");
        }

        HarvestReminder harvestReminder = new HarvestReminder(harvestReminderRepository.nextId(), command.pondId(), command.reminderDate(), command.reminderTime(), command.notes(), userSession.getOrganizationId());
        harvestReminderRepository.save(harvestReminder);
        reminderSchedulerService.scheduleHarvestReminder(harvestReminder, pond);
        return new CreateHarvestReminderResult(harvestReminder.id(), harvestReminder.pondId(), harvestReminder.reminderDate(), harvestReminder.reminderTime(), harvestReminder.notes(), LocalDateTime.now());
    }

    @Override
    protected void validateCommand(CreateHarvestReminderCommand command) {
        if (command.pondId() == null) {
            throw new ValidationException("pond id not found");
        }
        if (command.reminderDate() == null) {
            throw new ValidationException("reminder date cannot be null");
        }
        if (command.reminderTime() == null) {
            throw new ValidationException("reminder time cannot be null");
        }
    }
}
