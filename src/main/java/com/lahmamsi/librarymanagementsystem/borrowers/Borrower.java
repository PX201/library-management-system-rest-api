package com.lahmamsi.librarymanagementsystem.borrowers;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import com.lahmamsi.librarymanagementsystem.utilities.MembershipNumberGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * 
 * @author Aiman
 *
 */
@Component
@Entity
@Table(name = "borrower")
public class Borrower {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "borrower_id")
	private long borrowerId;
	@NotBlank
	@Size(min = 2, max = 50, message = "Should be between 2 and 50 character")
	private String firstName;
	@NotBlank
	@Size(min = 2, max = 50, message = "Should be between 2 and 50 character")
	private String lastName;
	@NotBlank
	@Email()
	@Column(unique = true)
	private String email;
	@NotBlank
	@Pattern(regexp = "^[0-9]{10}$",message = "Phone should be 10 digits")
	@Column(unique = true)
	private String phone;
	@NotBlank
	@Size(min = 5)
	private String address;
	@DateTimeFormat	(pattern = "MM-dd-yyyy")
	private LocalDate birthDate;
	private MembershipStatus membershipStatus;
	private String borrowerNumber;
//	@JsonIgnoreProperties("Transaction")
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinColumn( name="transactionId")
//	private List<Transaction> transactions;

	
	public Borrower( ) {
	}
	public Borrower( String firstName, String lastName, String email, String phone, String address,
			LocalDate birthDate) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.birthDate = birthDate;
	}

	

	//@Override
	
	public long getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(long borrowerId) {
		this.borrowerId = borrowerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public LocalDate getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	public MembershipStatus getMembershipStatus() {
		return membershipStatus;
	}
	public void setMembershipStatus(MembershipStatus membershipStatus) {
		this.membershipStatus = membershipStatus;
	}
	public void setMembershipStatus(String membershipStatus) {
		this.membershipStatus = MembershipStatus.valueOf(membershipStatus);
	}
	public String getBorrowerNumber() {
		return borrowerNumber;
	}
	public void setBorrowerNumber(String borrowerNumber) {
		this.borrowerNumber = borrowerNumber;
	}
	

    /**
     * Generates the borrower number and sets the membership status to active before persistence.
     */
	@PrePersist
	private void generateBorrowerNumber() {
		this.borrowerNumber = MembershipNumberGenerator.generateMembershipNumber();
		this.membershipStatus = MembershipStatus.ACTIVE;
	}
	@Override
	public String toString() {
		return "Borrower [userId=" + borrowerId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + ", address=" + address + ", birthDate=" + birthDate + ", membershipStatus="
				+ membershipStatus + ", borrowerNumber=" + borrowerNumber + "]";
	}

}
