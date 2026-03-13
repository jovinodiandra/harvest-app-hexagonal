package org.harvest.application.dto.result;

import org.harvest.domain.HarvestReminder;

import java.util.List;

public record ViewHarvestReminderResult(List<HarvestReminderItem> data) {
}
