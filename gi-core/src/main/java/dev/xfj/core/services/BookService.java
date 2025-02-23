package dev.xfj.core.services;

import dev.xfj.core.dto.codex.ItemCodexDTO;
import dev.xfj.generated.bookscodexexcelconfigdata.BooksCodexExcelConfigDataJson;
import dev.xfj.generated.materialexcelconfigdata.MaterialExcelConfigDataJson;
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
                .map(entry -> new ItemCodexDTO(
                        entry.getMaterialId(),
                        databaseService.getTranslation(getItem(entry.getMaterialId()).getNameTextMapHash()),
                        databaseService.getTranslation(getItem(entry.getMaterialId()).getDescTextMapHash()),
                        entry.getSortOrder()
                ))
                .collect(Collectors.toList());
    }

    private MaterialExcelConfigDataJson getItem(int materialId) {
        return databaseService.materialConfig
                .stream()
                .filter(item -> item.getId() == materialId)
                .findFirst()
                .orElse(null);
    }
}
