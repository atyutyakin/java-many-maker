package pro.alxerxc.menuMaker.service;

import org.springframework.stereotype.Service;
import pro.alxerxc.menuMaker.entity.User;
import pro.alxerxc.menuMaker.repository.UserRepository;

@Service
public class UserService extends GenericCrudService<User, Long> {

    public UserService(UserRepository repository) {
        super(repository);
    }
}
