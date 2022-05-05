package pro.alxerxc.menuMaker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GeneralPageableRepository<E> {

    public Page<E> findByNameContainingAllIgnoreCase(String searchPattern, Pageable pageable);
}

