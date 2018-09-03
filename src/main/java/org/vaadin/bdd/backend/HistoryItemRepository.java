package org.vaadin.bdd.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import org.vaadin.bdd.backend.data.entity.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
