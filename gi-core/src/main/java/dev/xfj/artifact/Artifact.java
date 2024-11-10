package dev.xfj.artifact;

import dev.xfj.constants.ArtifactType;
import dev.xfj.constants.ItemType;
import dev.xfj.constants.SalvageType;
import dev.xfj.database.Database;
import dev.xfj.item.Item;
import dev.xfj.jsonschema2pojo.reliquaryexcelconfigdata.ReliquaryExcelConfigDataJson;
import dev.xfj.weapon.SalvageReturnItems;

public class Artifact implements Item {
    private final ReliquaryExcelConfigDataJson data;

    public Artifact(ReliquaryExcelConfigDataJson data) {
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

    @Override
    public ItemType getItemType() {
        return ItemType.valueOf(data.getItemType());
    }

    public ArtifactType getArtifactType() {
        return ArtifactType.valueOf(data.getEquipType());
    }

    @Override
    public SalvageType getSalvageType() {
        return data.getDestroyRule() != null ? SalvageType.valueOf(data.getDestroyRule()) : SalvageType.NONE;
    }

    @Override
    public SalvageReturnItems getSalvagedItems() {
        return data.getDestroyReturnMaterialCount().size() > 0 ?
                new SalvageReturnItems(
                        data.getDestroyReturnMaterial().get(0),
                        data.getDestroyReturnMaterialCount().get(0)
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
