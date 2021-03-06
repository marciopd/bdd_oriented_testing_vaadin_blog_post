package org.vaadin.bdd.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import org.vaadin.bdd.backend.data.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
