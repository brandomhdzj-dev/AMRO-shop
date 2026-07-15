package com.brandom.mipaginaweb.repository;

import com.brandom.mipaginaweb.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, String> {
}
