package md.ceiti.frontend.component;

import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.CrudException;
import md.ceiti.frontend.exception.ValidationException;
import md.ceiti.frontend.service.CrudService;
import md.ceiti.frontend.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.crudui.crud.CrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;

import java.util.Collection;

public class CustomCrudListener<T extends CrudService<G>, G> implements CrudListener<G> {

    private final Logger logger = LoggerFactory.getLogger(CustomCrudListener.class);

    private final T service;
    private final GridCrud<G> crud;

    public CustomCrudListener(T service, GridCrud<G> crud) {
        this.service = service;
        this.crud = crud;
    }


    @Override
    public Collection<G> findAll() {
        String errorMessage;
        try {
            return service.findAll();
        } catch (BadRequestException e) {
            errorMessage = getLogError("FIND ALL", e.getMessage());
            ErrorHandler.handle(e);
        }

        logger.error(errorMessage);
        throw new CrudException();
    }

    @Override
    public G add(G g) {
        String errorMessage;
        try {
            return service.insert(g);
        } catch (BadRequestException | ValidationException e) {
            errorMessage = getLogError("INSERT", e.getMessage());
            if (e instanceof BadRequestException badRequestException) {
                ErrorHandler.handle(badRequestException);
            } else {
                ValidationException validationException = (ValidationException) e;
                ErrorHandler.setErrors(DMFormFactory.getBinder(crud.getCrudFormFactory()), validationException);
            }
        }

        logger.error(errorMessage);
        throw new CrudException();
    }

    @Override
    public G update(G g) {
        String errorMessage;
        try {
            return service.update(g);
        } catch (BadRequestException | ValidationException e) {
            errorMessage = getLogError("UPDATE", e.getMessage());
            if (e instanceof BadRequestException badRequestException) {
                ErrorHandler.handle(badRequestException);
            } else {
                ValidationException validationException = (ValidationException) e;
                ErrorHandler.setErrors(DMFormFactory.getBinder(crud.getCrudFormFactory()), validationException);
            }
        }

        logger.error(errorMessage);
        throw new CrudException();
    }

    @Override
    public void delete(G g) {
        try {
            service.delete(g);
        } catch (BadRequestException e) {
            logger.error(getLogError("DELETE", e.getMessage()));
            ErrorHandler.handle(e);
            throw new CrudException();
        }
    }

    private String getLogError(String operation, String badRequestMessage) {
        return String.format("%s - Error doing [%s] returned status [%s]",
                service.getClass().getSimpleName(), operation, badRequestMessage);
    }
}
