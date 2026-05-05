package technical.task.gmms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class MemberRequest {
    @NotBlank
    private String firstName;

    @NotBlank
    private String secondName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String country;

    @NotBlank
    @Pattern(regexp = "^\\d{2}-\\d{3}$")
    private String zipCode;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    public MemberRequest() {    }

    public MemberRequest(String firstName, String secondName, String lastName, String email, String country, String zipCode, String city, String address) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MemberRequest that)) return false;
        return Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getSecondName(), that.getSecondName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getCountry(), that.getCountry()) && Objects.equals(getZipCode(), that.getZipCode()) && Objects.equals(getCity(), that.getCity()) && Objects.equals(getAddress(), that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getSecondName(), getLastName(), getEmail(), getCountry(), getZipCode(), getCity(), getAddress());
    }

    @Override
    public String toString() {
        return "MemberRequest{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
