package md.ceiti.frontend.service;

import md.ceiti.frontend.dto.Role;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class RoleService implements CrudService<Role> {
    @Override
    public List<Role> findAll() {
        return Arrays.stream(Role.values()).toList();
    }

    @Override
    public Role insert(Role request) {
        return null;
    }

    @Override
    public Role update(Role request) {
        return null;
    }

    @Override
    public void delete(Role request) {

    }
}
