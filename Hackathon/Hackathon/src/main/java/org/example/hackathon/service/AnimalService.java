package org.example.hackathon.service;

import org.example.hackathon.model.Animal;
import org.example.hackathon.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<Animal> findAll() {
        return animalRepository.findAll();
    }

    public List<Animal> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return animalRepository.findAll();
        }

        String lowerKeyword = keyword.trim().toLowerCase();
        List<Animal> result = new ArrayList<>();

        for (Animal animal : animalRepository.findAll()) {
            String name = animal.getName() == null ? "" : animal.getName().toLowerCase();
            String species = animal.getSpecies() == null ? "" : animal.getSpecies().toLowerCase();

            if (name.contains(lowerKeyword) || species.contains(lowerKeyword)) {
                result.add(animal);
            }
        }

        return result;
    }

    public Animal findById(Long id) {
        return animalRepository.findById(id);
    }

    public void create(Animal animal) {
        animal.setId(animalRepository.getNextId());
        animalRepository.save(animal);
    }

    public void update(Animal animal) {
        animalRepository.save(animal);
    }

    public void deleteById(Long id) {
        animalRepository.deleteById(id);
    }
}