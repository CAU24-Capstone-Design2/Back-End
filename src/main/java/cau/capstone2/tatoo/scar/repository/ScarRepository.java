package cau.capstone2.tatoo.scar.repository;

import cau.capstone2.tatoo.scar.domain.Scar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScarRepository extends JpaRepository<Scar, Long> {

    List<Scar> findAllByUserId(Long userId);
}
