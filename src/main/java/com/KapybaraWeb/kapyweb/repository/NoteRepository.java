package com.KapybaraWeb.kapyweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.KapybaraWeb.kapyweb.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {}
