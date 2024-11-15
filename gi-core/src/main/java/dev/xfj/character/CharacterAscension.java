package dev.xfj.character;

import dev.xfj.constants.StatType;
import dev.xfj.item.ItemPair;
import dev.xfj.jsonschema2pojo.avatarpromoteexcelconfigdata.AvatarPromoteExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class CharacterAscension {
    private final AvatarPromoteExcelConfigDataJson data;

    public CharacterAscension(AvatarPromoteExcelConfigDataJson data) {
        this.data = data;
    }

    public int getAscensionId() {
        return data.getAvatarPromoteId();
    }

    public int getAscension() {
        return data.getPromoteLevel();
    }

    public int getAscensionCost() {
        return data.getScoinCost();
    }

    public List<ItemPair> getAscensionItems() {
        return data.getCostItems()
                .stream()
                .map(item -> new ItemPair(item.getId(), item.getCount()))
                .collect(Collectors.toList());
    }

    public int getMaxLevel() {
        return data.getUnlockMaxLevel();
    }

    public List<CharacterAscensionStatGrowth> getAscensionStatGrowth() {
        return data.getAddProps()
                .stream()
                .map(stat ->
                        new CharacterAscensionStatGrowth(StatType.valueOf(stat.getPropType()), stat.getValue()))
                .collect(Collectors.toList());
    }

    public int getRequiredAdventureRank() {
        return data.getRequiredPlayerLevel();
    }
}
