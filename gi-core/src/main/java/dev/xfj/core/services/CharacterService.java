package dev.xfj.core.services;

import dev.xfj.core.dto.CharacterDTO;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    public CharacterDTO test() {
        return new CharacterDTO("a");
    }
}
