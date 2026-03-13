package org.harvest.application.dto.result;

import org.harvest.domain.Contact;

import java.util.List;

public record ViewContactResult (List<Contact> contacts){
}
