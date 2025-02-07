package dev.xfj.core.services;

import dev.xfj.core.codex.BookCodex;
import dev.xfj.core.dto.codex.ItemCodexDTO;
import dev.xfj.jsonschema2pojo.bookscodexexcelconfigdata.BooksCodexExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final DatabaseService databaseService;

    @Autowired
    public BookService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<ItemCodexDTO> getBooks() {
        return databaseService.booksCodexConfig
                .stream()
                .sorted(Comparator.comparing(BooksCodexExcelConfigDataJson::getSortOrder))
                .map(BookCodex::new)
                .toList()
                .stream()
                .map(entry -> new ItemCodexDTO(entry.getId(), entry.getName(), entry.getDescription(), entry.getSortFactor()))
                .collect(Collectors.toList());
    }
}
