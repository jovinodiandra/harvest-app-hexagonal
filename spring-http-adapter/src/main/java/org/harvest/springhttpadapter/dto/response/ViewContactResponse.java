package org.harvest.springhttpadapter.dto.response;

import org.harvest.application.dto.result.ViewContactResult;
import org.harvest.domain.Contact;

import java.util.List;
import java.util.UUID;

public record ViewContactResponse(List<ContactItem> contacts) {

    public static ViewContactResponse from(ViewContactResult result) {
        List<ContactItem> items = result.contacts().stream()
                .map(ContactItem::from)
                .toList();
        return new ViewContactResponse(items);
    }

    public record ContactItem(UUID id, UUID supplierId, String name, String address, String phone, String email, String notes) {
        public static ContactItem from(Contact contact) {
            return new ContactItem(contact.id(), contact.supplierId(), contact.name(), contact.address(), contact.phone(), contact.email(), contact.notes());
        }
    }
}
