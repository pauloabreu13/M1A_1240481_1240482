package pt.psoft.g1.psoftg1.idgenerate.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.idgenerate.services.IdGenerationService;

@Component
public class IdGen {

    private static IdGenerationService idGenerationService;

    @Autowired
    public void setIdGenerationService(IdGenerationService service) {
        IdGen.idGenerationService = service;
    }

    public static String generateId() {
        if (idGenerationService == null) {
            throw new IllegalStateException("IdGenerationService not initialized");
        }
        return idGenerationService.generateId();
    }
}
