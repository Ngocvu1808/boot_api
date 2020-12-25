package hust.ngocvu.education.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hust.ngocvu.education.entity.User;
import hust.ngocvu.education.repository.UserRepository;

@RestController
@RequestMapping("api/user")
@CrossOrigin("*")
public class ApiUserController {
	@Autowired
	private UserRepository userRepository;

	
	@GetMapping("")
	public Object get() {
		List<User> users = userRepository.findAll();
		
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@PostMapping("")
	public Object post(@RequestBody User user) {
		User user2 = userRepository.save(user);
		return new ResponseEntity<User>(user2, HttpStatus.OK);
	}
	
	@GetMapping("search")
	public Object search(@RequestParam String keyword) {
		List<User> user = userRepository.findByDes(keyword);
		return new ResponseEntity<List<User>>(user, HttpStatus.OK);
	}
	
	@DeleteMapping("delete/{id}")
	public Object detele(@PathVariable int id) {
		userRepository.deleteById(id);
		return new ResponseEntity<String>("Deleted! ", HttpStatus.OK);
	}
	
	@PutMapping("{id}")
	public Object put(@PathVariable int id, @RequestBody User user) {
		try {
			if(userRepository.findById(id) == null || id != user.getId()) {
				return new ResponseEntity<String>("Id is not valid!", HttpStatus.BAD_REQUEST);
			}
			userRepository.save(user);
			return new ResponseEntity<String>("Update successed!", HttpStatus.OK);
		}
		catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<String>("Update failed! ", HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("{id}")
	public Object findById(@PathVariable int id) {
		Optional<User> user = userRepository.findById(id);
		User _user =user.get();
		return new ResponseEntity<User>(_user, HttpStatus.OK);
	}
	
	@PutMapping("password/{id}")
	public Object putPassword(@PathVariable int id, @RequestBody User user) {
		try {
			if(userRepository.findById(id) == null || id != user.getId()) {
				return new ResponseEntity<String>("Id is not valid!", HttpStatus.BAD_REQUEST);
			}
			else {
				String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
				user.setPassword(hashed);
				userRepository.updatepassword(user.getPassword(), user.getId());
				return new ResponseEntity<String>("Updated! ", HttpStatus.OK);
			}
		}catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<String>("Update failed!", HttpStatus.BAD_REQUEST);
		}
	}
	
	@CrossOrigin
	@GetMapping("email")
	public Object findByEmail(@RequestParam String email) {
		User user = userRepository.findByEmail(email);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}
