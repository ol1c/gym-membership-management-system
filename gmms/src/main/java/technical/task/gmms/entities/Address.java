package technical.task.gmms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Address {
    @Column(nullable = false)
    private String country;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    public Address() {    }

    public Address(String country, String zipCode, String city, String address) {
        this.country = country;
        this.zipCode = zipCode;
        this.city = city;
        this.address = address;
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
        if (!(o instanceof Address address1)) return false;
        return Objects.equals(getCountry(), address1.getCountry()) && Objects.equals(getZipCode(), address1.getZipCode()) && Objects.equals(getCity(), address1.getCity()) && Objects.equals(getAddress(), address1.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getZipCode(), getCity(), getAddress());
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
