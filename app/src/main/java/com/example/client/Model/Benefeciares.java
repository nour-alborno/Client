
package com.example.client.Model;

public class Benefeciares {
    String name;
    String mobile;
    String region;
    String address;
    String age;
    String disabilityType;
    String organization;
    String gender;

    String documentId;

    public Benefeciares() {
    }

    public Benefeciares(String name, String mobile, String region, String address, String age, String disabilityType, String organization, String gender, String documentId) {
        this.name = name;
        this.mobile = mobile;
        this.region = region;
        this.address = address;
        this.age = age;
        this.disabilityType = disabilityType;
        this.organization = organization;
        this.gender = gender;
        this.documentId = documentId;
    }

    public Benefeciares(String name, String mobile, String region, String address, String age, String disabilityType, String organization, String gender) {
        this.name = name;
        this.mobile = mobile;
        this.region = region;
        this.address = address;
        this.age = age;
        this.disabilityType = disabilityType;
        this.organization = organization;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
