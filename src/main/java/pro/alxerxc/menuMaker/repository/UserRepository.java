package pro.alxerxc.menuMaker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.alxerxc.menuMaker.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
