package md.ceiti.backend.service.impl;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.exception.NotFoundException;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Institution;
import md.ceiti.backend.repository.InstitutionRepository;
import md.ceiti.backend.service.GenericService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstitutionService implements GenericService<Institution, Long> {

    private final InstitutionRepository institutionRepository;

    public Optional<Institution> findByMaster(Account account) {
        return institutionRepository.findByMaster(account);
    }

    @Override
    public List<Institution> findAll() {
        return institutionRepository.findAll();
    }

    @Override
    public Institution getById(Long id) {
        return institutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Institution", "id", id));
    }

    @Override
    public Institution insert(Institution entity) {
        return institutionRepository.save(entity);
    }

    @Override
    public Institution update(Institution presentEntity, Institution updatedEntity) {
        BeanUtils.copyProperties(updatedEntity, presentEntity,
                "id");
        return institutionRepository.save(presentEntity);
    }

    @Override
    public void delete(Institution entity) {
        institutionRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        institutionRepository.deleteById(id);
    }
}
