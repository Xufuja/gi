package dev.xfj.app.controllers;

import dev.xfj.core.dto.item.ItemCodexDTO;
import dev.xfj.core.dto.item.ItemProfileDTO;
import dev.xfj.core.dto.weapon.WeaponProfileDTO;
import dev.xfj.core.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ItemCodexDTO>> items() {
        return ResponseEntity.ok(itemService.getItems());
    }

    @GetMapping(
            path = "/{itemId}",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ItemProfileDTO> item(@PathVariable int itemId) {

        return ResponseEntity.ok(itemService.getItem(itemId));
    }
}
