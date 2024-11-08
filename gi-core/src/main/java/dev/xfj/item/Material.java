package dev.xfj.item;

import dev.xfj.constants.ItemOperation;
import dev.xfj.constants.ItemType;
import dev.xfj.constants.MaterialType;
import dev.xfj.constants.SalvageType;
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
    public ItemType getItemType() {
        return ItemType.valueOf(data.getItemType());
    }

    public MaterialType getMaterialType() {
        return data.getMaterialType() != null ? MaterialType.valueOf(data.getMaterialType()) : MaterialType.NONE;
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
