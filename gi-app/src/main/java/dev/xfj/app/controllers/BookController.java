package dev.xfj.app.controllers;

import dev.xfj.core.dto.codex.ItemCodexDTO;
import dev.xfj.core.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping(
            path = "/v1/books",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )

    public ResponseEntity<List<ItemCodexDTO>> books() {
        return ResponseEntity.ok(bookService.getBooks());
    }
}
