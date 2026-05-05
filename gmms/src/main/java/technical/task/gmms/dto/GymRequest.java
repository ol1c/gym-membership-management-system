package technical.task.gmms.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class GymRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String country;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String city;

    @NotBlank
    private String address;

    public GymRequest() {    }


    public GymRequest(String name, String phoneNumber, String country, String zipCode, String city, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
        if (!(o instanceof GymRequest that)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber()) && Objects.equals(getCountry(), that.getCountry()) && Objects.equals(getZipCode(), that.getZipCode()) && Objects.equals(getCity(), that.getCity()) && Objects.equals(getAddress(), that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPhoneNumber(), getCountry(), getZipCode(), getCity(), getAddress());
    }

    @Override
    public String toString() {
        return "GymRequest{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
