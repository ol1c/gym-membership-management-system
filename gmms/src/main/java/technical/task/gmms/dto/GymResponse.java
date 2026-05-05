package technical.task.gmms.dto;

import technical.task.gmms.entities.Gym;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GymResponse {
    private UUID id;
    private String name;
    private String phoneNumber;
    private String country;
    private String zipCode;
    private String city;
    private String address;
    private List<String> membershipPlans;

    public GymResponse() {    }


    public GymResponse(Gym gym) {
        this.id = gym.getId();
        this.name = gym.getName();
        this.phoneNumber = gym.getPhoneNumber();
        this.country = gym.getAddress().getCountry();
        this.zipCode = gym.getAddress().getZipCode();
        this.city = gym.getAddress().getCity();
        this.address = gym.getAddress().getAddress();
        this.membershipPlans = gym.getMembershipPlans().stream()
                .map(m -> m.getId() + ": " + m.getName()).toList();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public List<String> getMembershipPlans() {
        return membershipPlans;
    }

    public void setMembershipPlans(List<String> membershipPlans) {
        this.membershipPlans = membershipPlans;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof GymResponse that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getPhoneNumber(), that.getPhoneNumber()) && Objects.equals(getCountry(), that.getCountry()) && Objects.equals(getZipCode(), that.getZipCode()) && Objects.equals(getCity(), that.getCity()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getMembershipPlans(), that.getMembershipPlans());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPhoneNumber(), getCountry(), getZipCode(), getCity(), getAddress(), getMembershipPlans());
    }

    @Override
    public String toString() {
        return "GymResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", membershipPlans=" + membershipPlans +
                '}';
    }
}
