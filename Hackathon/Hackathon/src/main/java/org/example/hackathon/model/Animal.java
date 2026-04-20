package org.example.hackathon.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Animal {

    private Long id;

    @NotBlank(message = "Tên loài thú không được để trống")
    @Size(min = 5, max = 150, message = "Tên loài thú phải từ 5 đến 150 ký tự")
    private String name;

    @NotBlank(message = "Chủng loài không được để trống")
    private String species;

    @NotNull(message = "Tuổi không được để trống")
    @Min(value = 0, message = "Tuổi phải lớn hơn hoặc bằng 0")
    private Integer age;

    private String animalImage;

    public Animal() {
    }

    public Animal(Long id, String name, String species, Integer age, String animalImage) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.age = age;
        this.animalImage = animalImage;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public Integer getAge() {
        return age;
    }

    public String getAnimalImage() {
        return animalImage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setAnimalImage(String animalImage) {
        this.animalImage = animalImage;
    }
}