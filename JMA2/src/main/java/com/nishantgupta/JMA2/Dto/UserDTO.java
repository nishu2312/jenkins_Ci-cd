
package com.nishantgupta.JMA2.Dto;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

      private String name;
        private String age;
        private String gender;
        private String dob;
        private String nationality;
        private String verificationStatus;
        private Instant dateCreated;
        private Instant dateModified;
		


        public UserDTO(String name, String age, String gender, String dob, String nationality,
                String verificationStatus, Instant dateCreated, Instant dateModified) {
            super();
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

