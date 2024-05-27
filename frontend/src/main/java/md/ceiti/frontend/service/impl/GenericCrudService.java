package md.ceiti.frontend.service.impl;

import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.service.CrudService;
import md.ceiti.frontend.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class GenericCrudService<T extends CrudService<G>, G> {

    private final Logger logger = LoggerFactory.getLogger(GenericCrudService.class);

    private final T service;

    public GenericCrudService(T service) {
        this.service = service;
    }

    public List<G> findAll() {
        try {
            return service.findAll();
        } catch (BadRequestException e) {
            logError("FIND ALL", e.getMessage());
            ErrorHandler.handle(e);
        }

        return Collections.emptyList();
    }

    public G insert(G request) {
        return service.insert(request);
    }

    public G update(G request) {
        return service.update(request);
    }

    public void delete(G request) {
        service.delete(request);
    }

    private void logError(String operation, String badRequestMessage) {
        logger.error(String.format("%s - Error doing [%s] returned status [%s]",
                service.getClass().getSimpleName(), operation, badRequestMessage));
    }
}
