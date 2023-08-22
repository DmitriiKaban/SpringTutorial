package com.springtutorial.models;


import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // postgres generates
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Min(value = 0, message = "Age should be positive")
    @Column(name = "age")
    private int age;

    @Column(name = "email")
    @NotEmpty(message = "Email should be not empty")
    @Email
    private String email;

    @OneToMany(mappedBy = "owner") // field in the class Item that has properties of connection(JoinColumn)
    private List<Item> items;

    @Enumerated(EnumType.ORDINAL) // saves list number, so if we change them with places, DB will store wrong data
             // EnumType.String saves string (happy, sad, ...)
    private Mood mood;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Mood getMood() {
        return mood;
    }

    public void setMood(Mood mood) {
        this.mood = mood;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}