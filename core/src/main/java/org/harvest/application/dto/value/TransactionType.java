package org.harvest.application.dto.value;

public enum TransactionType {
    INCOME("income"),EXPENSE("expense");

    private final String description;

    TransactionType(String transactionType) {
        this.description = transactionType;
    }

    public String getDescription() {
        return description;
    }
}
