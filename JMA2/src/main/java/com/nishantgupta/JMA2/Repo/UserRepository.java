package com.nishantgupta.JMA2.Repo;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nishantgupta.JMA2.Entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	

}
