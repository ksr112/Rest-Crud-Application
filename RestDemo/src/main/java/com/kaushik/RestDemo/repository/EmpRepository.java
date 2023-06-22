package com.kaushik.RestDemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kaushik.RestDemo.model.Employee;

@Repository
public interface EmpRepository extends JpaRepository<Employee, Long>{

	List<Employee> findByTech(String tech);
	List<Employee> findBySalaryGreaterThan(double salary);
}
