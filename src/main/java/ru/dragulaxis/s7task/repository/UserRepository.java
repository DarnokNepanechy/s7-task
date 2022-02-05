package ru.dragulaxis.s7task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dragulaxis.s7task.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
