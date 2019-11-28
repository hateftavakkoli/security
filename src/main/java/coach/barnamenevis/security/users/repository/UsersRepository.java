package coach.barnamenevis.security.users.repository;

import coach.barnamenevis.security.users.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {

}
