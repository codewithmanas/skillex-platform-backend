package com.codewithmanas.skillexplatformbackend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

//    @NotBlank
    @Column(unique = true)
    private String username;

    private String fullName;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    private String hashedPassword;

    private String profileImageUrl;

    // private String[] skills; // not fully supported by JPA
    // valid in java, but problematic in JPA
    @ElementCollection
    private List<String> skills;

    @ElementCollection
    private List<String> interests;

    @Enumerated
    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    private String bio;

    @Column(name = "rating", columnDefinition = "NUMERIC(2,1)")
    @DecimalMin("0.0")
    @DecimalMax("5.0")
//    @Digits(integer = 2, fraction = 1)
    private Double rating = 0.0;
//    private Double rating;

    private Integer ratingCount;

    private Float ratingAvg = 0f;

    private boolean isVerified = false;

    private boolean isActive = true;
    private String portfolioUrl;
    private String country;
    private String timezone = "UTC";

    @Column(unique = true)
    private String phoneNumber;

    private Instant emailVerifiedAt;
    // private String phoneVerifiedAt; // not needed for now
    private boolean twoFactorEnabled ;
    private String stripeCustomerId;
    private boolean isPaymentEnabled = false;

    @CurrentTimestamp
    @Column(updatable = false)
    private String createdAt;

    @UpdateTimestamp
    private String updatedAt;

    // Getter and Setter

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

//    public @NotBlank String getUsername() {
//        return username;
//    }
//
//    public void setUsername(@NotBlank String username) {
//        this.username = username;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(@NotBlank String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Float getRatingAvg() {
        return ratingAvg;
    }

    public void setRatingAvg(Float ratingAvg) {
        this.ratingAvg = ratingAvg;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Instant getEmailVerifiedAt() {
        return emailVerifiedAt;
    }

    public void setEmailVerifiedAt(Instant emailVerifiedAt) {
        this.emailVerifiedAt = emailVerifiedAt;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public boolean isPaymentEnabled() {
        return isPaymentEnabled;
    }

    public void setPaymentEnabled(boolean paymentEnabled) {
        isPaymentEnabled = paymentEnabled;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}


