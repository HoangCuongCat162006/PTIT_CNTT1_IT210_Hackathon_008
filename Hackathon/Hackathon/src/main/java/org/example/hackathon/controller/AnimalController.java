package org.example.hackathon.controller;

import jakarta.validation.Valid;
import org.example.hackathon.model.Animal;
import org.example.hackathon.service.AnimalService;
import org.example.hackathon.service.FileStorageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/animals")
public class AnimalController {

    private final AnimalService animalService;
    private final FileStorageService fileStorageService;

    public AnimalController(AnimalService animalService, FileStorageService fileStorageService) {
        this.animalService = animalService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String listAnimals(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("animals", animalService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "animal-list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("animal", new Animal());
        model.addAttribute("pageTitle", "Thêm mới loài thú");
        model.addAttribute("formAction", "/animals/create");
        return "animal-form";
    }

    @PostMapping("/create")
    public String createAnimal(@Valid @ModelAttribute("animal") Animal animal,
                               BindingResult bindingResult,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        validateImageFile(imageFile, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Thêm mới loài thú");
            model.addAttribute("formAction", "/animals/create");
            return "animal-form";
        }

        String fileName = fileStorageService.saveFile(imageFile);
        animal.setAnimalImage(fileName);

        animalService.create(animal);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm mới thành công");
        return "redirect:/animals";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Animal animal = animalService.findById(id);

        if (animal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy loài thú");
            return "redirect:/animals";
        }

        model.addAttribute("animal", animal);
        model.addAttribute("pageTitle", "Cập nhật loài thú");
        model.addAttribute("formAction", "/animals/edit/" + id);
        return "animal-form";
    }

    @PostMapping("/edit/{id}")
    public String updateAnimal(@PathVariable("id") Long id,
                               @Valid @ModelAttribute("animal") Animal animal,
                               BindingResult bindingResult,
                               @RequestParam("imageFile") MultipartFile imageFile,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        Animal oldAnimal = animalService.findById(id);

        if (oldAnimal == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy loài thú");
            return "redirect:/animals";
        }

        validateImageFile(imageFile, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Cập nhật loài thú");
            model.addAttribute("formAction", "/animals/edit/" + id);
            return "animal-form";
        }

        animal.setId(id);

        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = fileStorageService.saveFile(imageFile);
            animal.setAnimalImage(fileName);
        } else {
            animal.setAnimalImage(oldAnimal.getAnimalImage());
        }

        animalService.update(animal);
        redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thành công");
        return "redirect:/animals";
    }

    @PostMapping("/delete/{id}")
    public String deleteAnimal(@PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {
        animalService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Xóa thành công");
        return "redirect:/animals";
    }

    private void validateImageFile(MultipartFile imageFile, BindingResult bindingResult) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();

            if (fileName != null) {
                String lowerName = fileName.toLowerCase();
                if (!(lowerName.endsWith(".jpg")
                        || lowerName.endsWith(".jpeg")
                        || lowerName.endsWith(".png"))) {
                    bindingResult.rejectValue(
                            "animalImage",
                            "error.animalImage",
                            "Chỉ chấp nhận file .jpg, .jpeg, .png"
                    );
                }
            }
        }
    }
}