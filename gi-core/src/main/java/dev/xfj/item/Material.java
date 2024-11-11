package dev.xfj.item;

import dev.xfj.constants.*;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public record Material(MaterialExcelConfigDataJson data) implements Item<MaterialExcelConfigDataJson> {

    public String getEffectName() {
        return data.getEffectName();
    }

    public String getEffectDescription() {
        return Database.getInstance().getTranslation(data.getEffectDescTextMapHash());
    }

    public String getSpecialDescription() {
        return Database.getInstance().getTranslation(data.getSpecialDescTextMapHash());
    }

    public String getTypeDescription() {
        return Database.getInstance().getTranslation(data.getTypeDescTextMapHash());
    }

    public MaterialType getMaterialType() {
        return data.getMaterialType() != null ? MaterialType.valueOf(data.getMaterialType()) : MaterialType.NONE;
    }

    public ItemUsageTarget getUsageTarget() {
        return ItemUsageTarget.valueOf(data.getUseTarget());
    }

    public List<ItemUsageDetail> getUsageParameters() {
        return data.getItemUse()
                .stream()
                .map(parameter -> new ItemUsageDetail(
                        parameter.getUseOp() != null ?
                                ItemOperation.valueOf(parameter.getUseOp()) :
                                ItemOperation.NONE, parameter.getUseParam()))
                .collect(Collectors.toList());
    }

    public int getMaxUseCount() {
        return data.getMaxUseCount();
    }

    public int getCooldownTime() {
        return data.getCdTime();
    }

    public int getCooldownGroup() {
        return data.getCdGroup();
    }

    public int getMaxStackCount() {
        return data.getStackLimit();
    }

    public FoodQuality getFoodQuality() {
        return data.getFoodQuality() != null ? FoodQuality.valueOf(data.getFoodQuality()) : FoodQuality.NONE;
    }

    public String getEffectIconName() {
        return data.getEffectIcon();
    }
}
