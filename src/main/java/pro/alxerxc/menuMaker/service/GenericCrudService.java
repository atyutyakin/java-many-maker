package pro.alxerxc.menuMaker.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import pro.alxerxc.menuMaker.entity.Persistable;
import pro.alxerxc.menuMaker.repository.GenericPageableRepository;

import javax.el.MethodNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class GenericCrudService<E extends Persistable<ID>, ID> {
    private JpaRepository<E, ID> repository;
    private String repositoryClassName;

    public GenericCrudService(JpaRepository<E, ID> repository) {
        this.repository = repository;
        this.repositoryClassName = repository.getClass().getSimpleName();
    }

    public List<E> findAll(){
        return repository.findAll();
    }

    public Page<E> findPage(String searchString, int pageIndex, int size, Sort sort) {
        PageRequest request = PageRequest.of(pageIndex, size, sort);
        if (getRepository() instanceof GenericPageableRepository) {
            //noinspection unchecked
            return ((GenericPageableRepository<E>) getRepository()).findByNameContainingAllIgnoreCase(searchString, request);
        } else {
            throw new MethodNotFoundException("repository is not an instance of GeneralPageableRepository and doesn't support pagination");
        }
    }

    public E findById(ID id) {
        return repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(repositoryClassName + ": entity is not found by id " + id));
    }

    @Transactional
    public E add(E entityToAdd) {
        if (entityToAdd.hasId()) {
            throw new IllegalArgumentException(repositoryClassName + ": new entity should not have id");
        }
        return repository.save(entityToAdd);
    }

    @Transactional
    public E update(E entityToUpdate) {
        if (!entityToUpdate.hasId()) {
            throw new IllegalArgumentException(repositoryClassName + ": entity for update should have id");
        } else if (!repository.existsById(entityToUpdate.getId())) {
            throw new IllegalArgumentException(repositoryClassName + ": entity for update with id " +
                    entityToUpdate.getId() + " not exists");
        }
        return repository.save(entityToUpdate);
    }

    @Transactional
    public void deleteById(ID id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(repositoryClassName + ": entity for deletion with id " + id + " not exists");
        }
        repository.deleteById(id);
    }

}
