package dev.xfj.app.repositories;

import dev.xfj.app.entities.Keys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeysRepository extends JpaRepository<Keys, String> {
}