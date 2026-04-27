package com.example.joyarta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.joyarta.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long>{

}
