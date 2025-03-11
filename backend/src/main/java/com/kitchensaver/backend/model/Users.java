package com.kitchensaver.backend.model;

import jakarta.persistence.*;


// This marks the class as a database table
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // The database automatically generates the ID
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String cell;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String office;

    @Enumerated(EnumType.STRING) // Store role as a string
    @Column(nullable = false)
    private Role role;


    @Column(nullable = false)
    private String password;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void removePassword() {
        this.password = "";
    }

    // This checks if two users are the same by comparing their IDs
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users user = (Users) o;
        return id.equals(user.id);
    }

    // This generates a unique number (hash code) for each user based on their ID
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
