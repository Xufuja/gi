package dev.xfj.core.services;

import dev.xfj.core.dto.achievement.AchievementCodexDTO;
import dev.xfj.generated.achievementgoalexcelconfigdata.AchievementGoalExcelConfigDataJson;
import dev.xfj.generated.materialexcelconfigdata.MaterialExcelConfigDataJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AchievementService {
    private final DatabaseService databaseService;

    @Autowired
    public AchievementService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<AchievementCodexDTO> getAchievements() {
        return databaseService.achievementGoalConfig
                .stream()
                .sorted(Comparator.comparing(AchievementGoalExcelConfigDataJson::getOrderId))
                .map(entry -> new AchievementCodexDTO(
                        entry.getId(),
                        databaseService.getTranslation(entry.getNameTextMapHash()),
                        entry.getOrderId()
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
