package com.raidrin.kanjiexplainer.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.raidrin.kanjiexplainer.services.KanjiDetails;
import com.raidrin.kanjiexplainer.services.KanjiDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private KanjiDetailsService kanjiDetailsService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/details")
    public String details(@RequestParam("kanji") String kanji, Model model) {
        KanjiDetails kanjiDetails = null;
        try {
            kanjiDetails = kanjiDetailsService.getDetails(kanji);
        } catch (NoKanjiFoundException e) {
            model.addAttribute("error", "No kanji found");
            return "error";
        } catch (JsonProcessingException e) {
            model.addAttribute("error", "An error occurred");
            return "error";
        }
        model.addAttribute("kanjiDetails", kanjiDetails);
        return "details";
    }
}
