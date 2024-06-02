package md.ceiti.backend.service.impl;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.exception.NotFoundException;
import md.ceiti.backend.model.Document;
import md.ceiti.backend.repository.DocumentRepository;
import md.ceiti.backend.service.GenericService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentService implements GenericService<Document, UUID> {

    private final DocumentRepository documentRepository;

    @Override
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    @Override
    public Document getById(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Document", "id", id));
    }

    @Override
    public Document insert(Document entity) {
        return documentRepository.save(entity);
    }

    @Override
    public Document update(Document presentEntity, Document updatedEntity) {
        BeanUtils.copyProperties(updatedEntity, presentEntity,
                "id");
        return documentRepository.save(presentEntity);
    }

    @Override
    public void delete(Document entity) {
        documentRepository.delete(entity);
    }

    @Override
    public void deleteById(UUID id) {
        documentRepository.deleteById(id);
    }
}
