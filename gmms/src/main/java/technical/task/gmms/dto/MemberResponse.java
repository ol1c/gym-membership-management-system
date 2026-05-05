package technical.task.gmms.dto;

import technical.task.gmms.entities.Member;
import technical.task.gmms.entities.MembershipStatus;

import java.util.Objects;
import java.util.UUID;

public class MemberResponse {
    private UUID id;
    private String firstName;
    private String secondName;
    private String lastName;
    private String email;
    private String country;
    private String zipCode;
    private String city;
    private String address;
    private UUID membershipId;
    private String planName;
    private MembershipStatus status;
    private String gymName;

    public MemberResponse() {    }

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.firstName = member.getFirstName();
        this.secondName = member.getSecondName();
        this.lastName = member.getLastName();
        this.email = member.getEmail();
        this.country = member.getAddress().getCountry();
        this.zipCode = member.getAddress().getZipCode();
        this.city = member.getAddress().getCity();
        this.address = member.getAddress().getAddress();
        this.membershipId = member.getMembership().getId();
        this.planName = member.getMembership().getName();
        this.status = member.getStatus();
        this.gymName = member.getMembership().getGym().getName();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(UUID membershipId) {
        this.membershipId = membershipId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MemberResponse that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getFirstName(), that.getFirstName()) && Objects.equals(getSecondName(), that.getSecondName()) && Objects.equals(getLastName(), that.getLastName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getCountry(), that.getCountry()) && Objects.equals(getZipCode(), that.getZipCode()) && Objects.equals(getCity(), that.getCity()) && Objects.equals(getAddress(), that.getAddress()) && Objects.equals(getMembershipId(), that.getMembershipId()) && Objects.equals(getPlanName(), that.getPlanName()) && getStatus() == that.getStatus() && Objects.equals(getGymName(), that.getGymName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getSecondName(), getLastName(), getEmail(), getCountry(), getZipCode(), getCity(), getAddress(), getMembershipId(), getPlanName(), getStatus(), getGymName());
    }

    @Override
    public String toString() {
        return "MemberResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", membershipId=" + membershipId +
                ", planName='" + planName + '\'' +
                ", status=" + status +
                ", gymName='" + gymName + '\'' +
                '}';
    }
}
