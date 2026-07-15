package com.brandom.mipaginaweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.brandom.mipaginaweb.model.Settings;
import com.brandom.mipaginaweb.repository.SettingsRepository;

@ControllerAdvice
public class WebConfig {

    @Autowired
    private SettingsRepository settingsRepository;

    @ModelAttribute
    public void agregarLogo(Model model) {
        String logoUrl = settingsRepository.findById("logoUrl").map(Settings::getValue).orElse(null);
        model.addAttribute("logo", logoUrl);
    }
}
