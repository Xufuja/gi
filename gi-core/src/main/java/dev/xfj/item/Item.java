package dev.xfj.item;

import dev.xfj.constants.ItemOperation;
import dev.xfj.constants.ItemType;
import dev.xfj.constants.MaterialType;
import dev.xfj.database.Database;
import dev.xfj.jsonschema2pojo.materialexcelconfigdata.MaterialExcelConfigDataJson;

import java.util.List;
import java.util.stream.Collectors;

public class Item {
    private final MaterialExcelConfigDataJson data;

    public Item(MaterialExcelConfigDataJson data) {
        this.data = data;
    }

    public int getId() {
        return data.getId();
    }

    public String getName() {
        return Database.getInstance().getTranslation(data.getNameTextMapHash());
    }

    public String getDescription() {
        return Database.getInstance().getTranslation(data.getDescTextMapHash());
    }

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
}
