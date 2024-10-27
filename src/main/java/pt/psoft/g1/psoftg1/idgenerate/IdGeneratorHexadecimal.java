package pt.psoft.g1.psoftg1.idgenerate;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component("hexadecimal")
public class IdGeneratorHexadecimal implements IdGenerator {

    private static final String PREFIX = "hexa-";

    @Override
    public String generateId() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return PREFIX + uuid.substring(0, 24);
    }
}
