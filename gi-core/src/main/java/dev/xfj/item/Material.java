package dev.xfj.item;

import dev.xfj.constants.*;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;
import dev.xfj.weapon.SalvageReturnItems;

import java.util.List;
import java.util.stream.Collectors;

public class Material implements Item {
    private final MaterialExcelConfigDataJson data;

    public Material(MaterialExcelConfigDataJson data) {
        this.data = data;
    }

    @Override
    public int getId() {
        return data.getId();
    }

    @Override
    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    @Override
    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

    @Override
    public int getRarity() {
        return data.getRankLevel();
    }

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

    @Override
    public ItemType getItemType() {
        return ItemType.valueOf(data.getItemType());
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


    @Override
    public SalvageType getSalvageType() {
        return data.getDestroyRule() != null ? SalvageType.valueOf(data.getDestroyRule()) : SalvageType.NONE;
    }

    @Override
    public SalvageReturnItems getSalvagedItems() {
        //There is never more than 1 item in either array
        return data.getDestroyReturnMaterialCount().size() > 0 ?
                new SalvageReturnItems(
                        (Integer) data.getDestroyReturnMaterial().get(0),
                        (Integer) data.getDestroyReturnMaterialCount().get(0)
                ) :
                null;
    }

    @Override
    public String getIconName() {
        return data.getIcon();
    }

    public String getEffectIconName() {
        return data.getEffectIcon();
    }

    @Override
    public Integer getWeight() {
        return data.getWeight();
    }

    @Override
    public Integer getRank() {
        return data.getRank();
    }

    @Override
    public Integer getGadgetId() {
        return data.getGadgetId();
    }
}
