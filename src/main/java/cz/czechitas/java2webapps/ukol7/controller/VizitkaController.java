package cz.czechitas.java2webapps.ukol7.controller;

import cz.czechitas.java2webapps.ukol7.entity.Vizitka;
import cz.czechitas.java2webapps.ukol7.repository.VizitkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class VizitkaController {

    private final VizitkaRepository repository;


    @Autowired
    public VizitkaController(VizitkaRepository repository) {
        this.repository = repository;
    }

    @InitBinder
    public void nullStringBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/")
    public Object zobrazeniStranky() {
        Iterable<Vizitka> all = repository.findAll();
        return new ModelAndView("seznam")
                .addObject("vizitky", all);
    }

    @GetMapping("/{id:[0-9]+}")
    public Object detail(@PathVariable int id) {
        Optional<Vizitka> vizitka = repository.findById(id);
        if (vizitka.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return new ModelAndView("vizitka")
                .addObject("vizitky", vizitka.get());
    }

    @GetMapping("/nova")
    public ModelAndView novaVizitka() {
        return new ModelAndView("formular")
                .addObject("vizitky", new Vizitka());

    }


    @PostMapping("/nova")
    public Object pridatVizitku(@ModelAttribute("vizitka") @Valid Vizitka vizitka, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formular";
        }
        repository.save(vizitka);
        return "redirect:/";
    }
}


