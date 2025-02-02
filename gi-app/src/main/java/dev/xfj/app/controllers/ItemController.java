package dev.xfj.app.controllers;

import dev.xfj.core.dto.ItemCodexDTO;
import dev.xfj.core.dto.WeaponCodexDTO;
import dev.xfj.core.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping(
            path = "/v1/items",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )

    public ResponseEntity<List<ItemCodexDTO>> weapons() {
        return ResponseEntity.ok(itemService.getItems());
    }
}
