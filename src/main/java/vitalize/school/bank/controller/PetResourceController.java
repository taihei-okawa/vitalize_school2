package vitalize.school.bank.controller;

import java.util.List;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;


@RestController
@RequestMapping("/pets")
public class PetResourceController {
    @GetMapping
    public List<Map<String, String>> pets() {
        return new ArrayList<>();
    }

    @GetMapping("{id}")
    public Map<String, String> pet(@PathVariable String id) {
        return new HashMap<>();
    }

    @PutMapping("{id}")
    public void updatePet(@PathVariable String id) {
        return;
    }

    @PostMapping
    public int insertPet() {
        return 1;
    }

    @DeleteMapping
    public void deletePet() {
        return;
    }
}
