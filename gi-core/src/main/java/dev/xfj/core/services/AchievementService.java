package dev.xfj.core.services;

import dev.xfj.core.dto.achievement.AchievementCodexDTO;
import dev.xfj.core.dto.achievement.AchievementDTO;
import dev.xfj.core.utils.KeyValue;
import dev.xfj.generated.achievementexcelconfigdata.AchievementExcelConfigDataJson;
import dev.xfj.generated.achievementgoalexcelconfigdata.AchievementGoalExcelConfigDataJson;
import dev.xfj.generated.rewardexcelconfigdata.RewardItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
                        getAchievementsByCategory(entry.getId()),
                        entry.getOrderId()
                ))
                .collect(Collectors.toList());
    }

    private List<AchievementDTO> getAchievementsByCategory(int category) {
        return databaseService.achievementConfig
                .stream()
                .filter(achievement -> achievement.getGoalId() == category)
                .filter(entry -> entry.getOrderId() != 0)
                .sorted(Comparator.comparing(AchievementExcelConfigDataJson::getOrderId))
                .map(achievement -> {
                    String parameter = "{param0}";
                    String description = databaseService.getTranslation(achievement.getDescTextMapHash());

                    return new AchievementDTO(
                            achievement.getId(),
                            databaseService.getTranslation(achievement.getTitleTextMapHash()),
                            databaseService.getTranslation(achievement.getPs5TitleTextMapHash()),
                            description != null ?
                                    description.contains(parameter) ?
                                            description.replace(parameter, String.valueOf(achievement.getProgress())) :
                                            description :
                                    null,
                            getAchievementReward(achievement.getFinishRewardId()).entrySet()
                                    .stream()
                                    .map(entry -> new KeyValue(
                                            databaseService.getTranslation(
                                                    databaseService.getItem(entry.getKey()).getNameTextMapHash()
                                            ),
                                            entry.getValue())
                                    )
                                    .collect(Collectors.toList()),
                            achievement.getPreStageAchievementId() != 0 ? achievement.getPreStageAchievementId() : null,
                            achievement.getOrderId()
                    );
                })
                .collect(Collectors.toList());
    }

    private Map<Integer, Integer> getAchievementReward(int id) {
        return databaseService.rewardConfig
                .stream()
                .filter(reward -> reward.getRewardId() == id)
                .flatMap(item -> item.getRewardItemList()
                        .stream()
                        .filter(entry -> entry.getItemId() != 0)
                )
                .collect(Collectors.toMap(
                        RewardItem::getItemId,
                        RewardItem::getItemCount
                ));
    }
}
