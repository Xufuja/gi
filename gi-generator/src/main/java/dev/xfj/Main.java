package dev.xfj;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //new ClassGenerator(
        //        "C:\\Dev\\AnimeGameData\\ExcelBinOutput\\",
        //        "C:\\Dev\\gi\\gi-core\\src\\generated\\"
        //)
        //        .createClasses();
        //new ClassGenerator(
        //        "C:\\Dev\\AnimeGameData\\BinOutput\\Avatar\\",
        //        "C:\\Dev\\gi\\gi-core\\src\\generated\\"
        //)
        //        .createClassesAvatarConfig();
        //new ClassGenerator(
        //        "C:\\Dev\\AnimeGameData\\BinOutput\\Ability\\Temp\\AvatarAbilities\\",
        //        "C:\\Dev\\gi\\gi-core\\src\\generated\\"
        //)
        //        .createClassesAvatarAbilities();
        //System.out.println("Class generation done!");
        OtherGenerator otherGenerator = new OtherGenerator("a", "a");
        String test = """
                {
                    "useType": "AVATAR_FORMAL",
                    "bodyType": "BODY_GIRL",
                    "scriptDataPathHash": 17851432353698653922,
                    "iconName": "UI_AvatarIcon_Chiori",
                    "sideIconName": "UI_AvatarIcon_Side_Chiori",
                    "qualityType": "QUALITY_ORANGE",
                    "chargeEfficiency": 1,
                    "combatConfigHash": 5458124704237262054,
                    "initialWeapon": 11101,
                    "weaponType": "WEAPON_SWORD_ONE_HAND",
                    "manekinPathHash": 5164384039825260980,
                    "imageName": "AvatarImage_Forward_Chiori",
                    "gachaCardNameHash": 7802871161484986121,
                    "gachaImageNameHash": 9002966079696239382,
                    "coopPicNameHash": 412797143627194640,
                    "cutsceneShow": "",
                    "skillDepotId": 9401,
                    "staminaRecoverSpeed": 25,
                    "candSkillDepotIds": [],
                    "manekinJsonConfigHash": 14021160637976492821,
                    "manekinMotionConfig": 103,
                    "descTextMapHash": 808184993,
                    "avatarIdentityType": "AVATAR_IDENTITY_NORMAL",
                    "avatarPromoteId": 94,
                    "avatarPromoteRewardLevelList": [
                      1,
                      3,
                      5
                    ],
                    "avatarPromoteRewardIdList": [
                      900811,
                      900813,
                      900815
                    ],
                    "featureTagGroupID": 10000094,
                    "infoDescTextMapHash": 808184993,
                    "animatorConfigPathHash": 12658623118900276254,
                    "HOGEDCCNOJC": 2089437210225248427,
                    "LIGCDNGHMNJ": 5458124704237262054,
                    "GNJHJGFINBA": 13802281408234894017,
                    "MIJMKGNIOPD": "FIGHT_PROP_CRITICAL_HURT",
                    "JJCDEPCEKJO": "FIGHT_PROP_CRITICAL",
                    "MALFNOPHKPA": "FIGHT_PROP_DEFENSE_PERCENT",
                    "hpBase": 890.4009,
                    "attackBase": 25.137,
                    "defenseBase": 74.1892,
                    "critical": 0.05,
                    "criticalHurt": 0.5,
                    "propGrowCurves": [
                      {
                        "type": "FIGHT_PROP_BASE_HP",
                        "growCurve": "GROW_CURVE_HP_S5"
                      },
                      {
                        "type": "FIGHT_PROP_BASE_ATTACK",
                        "growCurve": "GROW_CURVE_ATTACK_S5"
                      },
                      {
                        "type": "FIGHT_PROP_BASE_DEFENSE",
                        "growCurve": "GROW_CURVE_HP_S5"
                      }
                    ],
                    "prefabPathRagdollHash": 8097531138812417847,
                    "deformationMeshPathHash": 16843554909179268631,
                    "id": 10000094,
                    "nameTextMapHash": 1944606922,
                    "prefabPathHash": 16368485206459150869,
                    "prefabPathRemoteHash": 16455247317469568824,
                    "controllerPathHash": 6902168011742553513,
                    "controllerPathRemoteHash": 1749564528399333646,
                    "lodPatternName": "",
                    "dummy": {
                        "aaa": "FIGHT_PROP_BASE_DEFENSE",
                        "bbb": "GROW_CURVE_HP_S5"
                      }
                    }""";
        otherGenerator.createSchema(test);
    }
}