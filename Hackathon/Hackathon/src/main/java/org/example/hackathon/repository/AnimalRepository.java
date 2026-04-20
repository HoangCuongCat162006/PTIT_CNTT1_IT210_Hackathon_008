package org.example.hackathon.repository;

import org.example.hackathon.model.Animal;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AnimalRepository {

    private final Map<Long, Animal> animalMap = new LinkedHashMap<>();

    public AnimalRepository() {
        animalMap.put(1L, new Animal(1L, "Hổ Bengal", "Thú ăn thịt", 5, null));
        animalMap.put(2L, new Animal(2L, "Vẹt Macaw", "Chim", 12, null));
        animalMap.put(3L, new Animal(3L, "Rùa Galapagos", "Bò sát", 150, null));
    }

    public List<Animal> findAll() {
        return new ArrayList<>(animalMap.values());
    }

    public Animal findById(Long id) {
        return animalMap.get(id);
    }

    public void save(Animal animal) {
        animalMap.put(animal.getId(), animal);
    }

    public void deleteById(Long id) {
        animalMap.remove(id);
    }

    public Long getNextId() {
        long max = 0;
        for (Long id : animalMap.keySet()) {
            if (id > max) {
                max = id;
            }
        }
        return max + 1;
    }
}