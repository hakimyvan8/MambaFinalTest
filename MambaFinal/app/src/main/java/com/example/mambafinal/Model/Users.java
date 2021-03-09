package com.example.mambafinal.Model;

public class Users
{
   private String uid,Password,name,phone,image,address;

   public Users()
   {

   }

    public Users(String uid,String password, String name, String phone, String image, String address) {
        this.uid = uid;
        Password = password;
        this.name = name;
        this.phone = phone;
        this.image = image;
        this.address = address;
    }

    public String getPassword() {
        return Password;
    }

    public String getUid() {
        return uid;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
