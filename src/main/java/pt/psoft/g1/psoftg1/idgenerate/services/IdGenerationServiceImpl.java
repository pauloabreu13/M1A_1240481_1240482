package pt.psoft.g1.psoftg1.idgenerate.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.idgenerate.IdGenerator;


@Service
@PropertySource({"classpath:application.properties"})
public class IdGenerationServiceImpl implements IdGenerationService {

    @Autowired
    private ApplicationContext context;

    @Value("${generationMethod}")
    private String generationMethod;

    @Override
    public String generateId() {
        IdGenerator idGenerator = context.getBean(generationMethod, IdGenerator.class);
        return idGenerator.generateId();
    }
}