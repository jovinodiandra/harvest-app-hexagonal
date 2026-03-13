package org.harvest;

import org.harvest.application.port.outbound.PasswordSecurity;
import org.mindrot.jbcrypt.BCrypt;

public class BcryptPasswordSecurity implements PasswordSecurity {
    @Override
    public String hash(String s) {
        return BCrypt.hashpw(s, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String raw, String hash) {
        return BCrypt.checkpw(raw, hash);
    }
}
