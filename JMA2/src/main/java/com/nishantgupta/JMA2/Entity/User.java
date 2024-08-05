package com.nishantgupta.JMA2.Entity;


import java.time.Instant;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;


	@Column(name = "name",length =255)
    private String name;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "dob")
    private String dob;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "verification_status")
    private String verificationStatus;

    @Column(name = "date_created")
    private Instant dateCreated;

    @Column(name = "date_modified")
    private Instant dateModified;


    
    public User(Long userId, String name, String age, String gender, String dob, String nationality,
    		String verificationStatus, Instant dateCreated, Instant dateModified) {
    	super();
    	this.userId = userId;
    	this.name = name;
    	this.age = age;
    	this.gender = gender;
    	this.dob = dob;
    	this.nationality = nationality;
    	this.verificationStatus = verificationStatus;
    	this.dateCreated = dateCreated;
    	this.dateModified = dateModified;
    }
   

 
}

