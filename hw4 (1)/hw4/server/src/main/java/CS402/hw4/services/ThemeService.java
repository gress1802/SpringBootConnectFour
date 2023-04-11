package CS402.hw4.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import CS402.hw4.models.Theme;
import CS402.hw4.repositories.DefRepository;

@Service
public class ThemeService {
    private final DefRepository defRepository;

    @Autowired
    public ThemeService(DefRepository defRepository) {
        this.defRepository = defRepository;
    }

    public Theme getDefaultTheme() {
        return defRepository.findFirstBy();
    }
}


