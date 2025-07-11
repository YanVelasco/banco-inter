package com.ecom.testeinter.repository;

import com.ecom.testeinter.model.Carteira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, UUID> {
    Optional<Carteira> findByUsuarioId(UUID usuarioId);
}
