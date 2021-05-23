package cz.czechitas.java2webapps.ukol7.controller;

import cz.czechitas.java2webapps.ukol7.entity.Vizitka;
import cz.czechitas.java2webapps.ukol7.repository.VizitkaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class VizitkaController {

    private final VizitkaRepository repository;

    private final Iterable<Vizitka> seznamVizitek = List.of(
            new Vizitka(1, "Dita (Přikrylová) Formánková", "Czechitas z. s.", "Václavské náměstí 837/11",
                    "Praha 1", "11000", "dita@czechitas.cs", "+420 800123456", "www.czechitas.cz")
    );

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

    //Do controlleru přidej metodu, která bude reagovat na GET požadavky na adrese /nova.
    // Metoda jen zobrazí šablonu formular.html. Uprav formulář tak, aby odesílal data metodou POST
    // na adresu /nova. Vyzkoušej v prohlížeči, že funguje odkaz na přidání vizitky na úvodní stránce.

    @PostMapping("/nova")
    public Object pridatVizitku(@ModelAttribute("vizitky") @Valid Vizitka vizitka) {
        repository.save(vizitka);
        return "redirect:/";
    }
    
    //Do controlleru přidej POST metodu, která bude reagovat na POST požadavky na adrese /nova.
    // Jako parametr bude přijímat entitu Vizitka, použijeme ji i pro přenos dat z formuláře.
    // Použij metodu save() repository pro uložení vizitky.
    // Po uložení vizitky přesměruj uživatele na úvodní stránku.
    // Vyzkoušej v prohlížeči, že funguje přidání vizitky.

}


