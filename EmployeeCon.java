package com.example.demo;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.Employee;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.exception.NoSuchEmployeeExistsException;
import com.example.demo.service.EmpService;

@RestController
@CrossOrigin(origins = "*") // allow Angular
public class EmployeeCon {
	
@Autowired
EmpService es;

@GetMapping("/findAll")
 // fetch details

public ResponseEntity<List<Employee>> getAllEmployee() { // controller class method name
	return new ResponseEntity<>(es.findAllEmployee(),HttpStatus.OK); // service layer method
}

//add

@PostMapping("/add") // post-add
public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee)
{
	Employee e1=es.addfunc(employee);
	return new ResponseEntity<>(e1,HttpStatus.CREATED);
	  
}



@GetMapping("/find/{id}")
public ResponseEntity <Employee> byId(@PathVariable int id) { // pass id parameter in path variable and int id 
Employee e2=es.addId(id);
if(e2!=null)
	return new ResponseEntity<>(e2,HttpStatus.ACCEPTED);
	else
		return new ResponseEntity<>(e2,HttpStatus.NOT_ACCEPTABLE);
}
@PutMapping("/update/{id}") 
public ResponseEntity<Employee> saveDb(@PathVariable Integer id,@RequestBody Employee emp)
{
	Employee e3=es.updateEmp(id,emp);
	return new ResponseEntity<>(e3,HttpStatus.OK);
}


@PatchMapping("/updatePartial/{id}")
public ResponseEntity<Employee> partialUpdate(@PathVariable Integer id,
                             @RequestBody Map<String, Object> updates) {
	Employee e4=es.partialUpdate(id, updates);
    return new ResponseEntity<>(e4,HttpStatus.OK);
}
@DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) { //here void coz no need to return value, just deletuing so
	Employee e5= es.addId(id);
	if(e5!=null)
	{
		es.deleteId(id);
	return new ResponseEntity<>("Deleted",HttpStatus.OK);
	}
		
	else {
		return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
    }
}

// adding exception handlers for employee already exists and no such employee
@ExceptionHandler(value=NoSuchEmployeeExistsException.class)
@ResponseStatus(HttpStatus.NOT_FOUND)
public ErrorResponse handleNoSuchEmployeeExistsException(NoSuchEmployeeExistsException ex)
{
    return new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
}

} 