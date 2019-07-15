package com.example.android.facultypayment;

public class AllUsers {
private String email;
private String name;

public AllUsers(){}
public AllUsers(String email,String name)
{
    this.email = email;
    this.name = name;
}

    public String getEmail()
    {
        return this.email;
    }
    public String getName() {return this.name;}

}

