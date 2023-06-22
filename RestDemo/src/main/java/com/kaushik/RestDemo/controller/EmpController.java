package com.kaushik.RestDemo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kaushik.RestDemo.model.Employee;
import com.kaushik.RestDemo.repository.EmpRepository;

@RestController
@RequestMapping("/emp")
public class EmpController {

	@Autowired
	EmpRepository repo;

	// This adds a new Employee only if the id is already not present, if present
	// sends conflict as a response. Endpoint /emp/add
	@PostMapping("/add")
	public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
		try {
			// Validating the data coming through
			if (employee.getId() > 0 && employee.getName() != null && employee.getSalary() > 0.0
					&& employee.getTech() != null) {
				Optional<Employee> emp = repo.findById(employee.getId());
				if (emp.isPresent()) {
					return new ResponseEntity<Employee>(HttpStatus.CONFLICT);
				} else {
					Employee res = repo.save(employee);
					return new ResponseEntity<Employee>(res, HttpStatus.OK);
				}

			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// This fetches the Employee details on the basis of the id. Endpoint
	// /emp/the_id_you_want
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable long id) {
		try {

			Optional<Employee> emp = repo.findById(id);
			if (emp.isPresent()) {
				return new ResponseEntity<Employee>(emp.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all")
	public ResponseEntity<List<Employee>> getAllEmployee() {
		try {

			List<Employee> emp = repo.findAll();
			if (!emp.isEmpty()) {
				return new ResponseEntity<List<Employee>>(emp, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// This gets all the employees of a particular tech. Endpoint
	// /emp?tech=the_tech_you_want
	// This also gets all employees whose salary is greater than the number passed.
	// Endpoint /emp?salary=the_salary_you_want
	@GetMapping
	public ResponseEntity<List<Employee>> getEmployeeByTechAndGreaterSalary(
			@RequestParam(required = false, value = "tech") String tech,
			@RequestParam(required = false, value = "salary") Double salary) {
		try {
			List<Employee> lst = null;
			if (tech != null) {
				lst = repo.findByTech(tech);
				if (lst.isEmpty())
					return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
				else
					return new ResponseEntity<List<Employee>>(lst, HttpStatus.OK);
			} else if (salary > 0) {
				lst = repo.findBySalaryGreaterThan(salary);
				if (lst.isEmpty())
					return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
				else
					return new ResponseEntity<List<Employee>>(lst, HttpStatus.OK);
			} else
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// This updates Employee details only if the id is present else sends a no
	// content response back. Endpoint /emp/update
	@PutMapping("/update")
	public ResponseEntity<Employee> updateEmployeeDetails(@RequestBody Employee employee) {
		try {
			// Validating the data coming through
			if (employee.getId() > 0 && employee.getName() != null && employee.getSalary() > 0.0
					&& employee.getTech() != null) {
				Optional<Employee> emp = repo.findById(employee.getId());
				if (emp.isPresent()) {
					Employee res = repo.save(employee);
					return new ResponseEntity<Employee>(res, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Employee> deleteEmployeeDetails(@PathVariable long id) {
		try {
			Optional<Employee> emp = repo.findById(id);
			if (emp.isPresent()) {
				repo.delete(emp.get());
				return new ResponseEntity<Employee>(emp.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
