package dev.xfj.core.constants;

import static dev.xfj.core.constants.Global.DATA_PATH;

public enum DataPath {
    EXCEL_BIN_OUTPUT("\\ExcelBinOutput\\"),
    TEXT_MAP("\\TextMap\\"),
    AVATAR("\\BinOutput\\Avatar\\"),
    AVATAR_ABILITIES("\\BinOutput\\Ability\\Temp\\AvatarAbilities\\"),
    READABLE("\\Readable\\");

    public final String path;

    DataPath(String path) {
        this.path = DATA_PATH + path;
    }
}
