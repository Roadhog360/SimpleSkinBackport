package roadhog360.simpleskinbackport.core;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class DefaultPlayerSkin {
    private static final PlayerSkin[] DEFAULT_SKINS = new PlayerSkin[] {
        new PlayerSkin(true, "alex"), new PlayerSkin(true, "ari"),
        new PlayerSkin(true, "efe"), new PlayerSkin(true, "kai"),
        new PlayerSkin(true, "makena"), new PlayerSkin(true, "noor"),
        new PlayerSkin(true, "steve"), new PlayerSkin(true, "sunny"),
        new PlayerSkin(true, "zuri"),

        new PlayerSkin(false, "alex"), new PlayerSkin(false, "ari"),
        new PlayerSkin(false, "efe"), new PlayerSkin(false, "kai"),
        new PlayerSkin(false, "makena"), new PlayerSkin(false, "noor"),
        new PlayerSkin(false, "steve"), new PlayerSkin(false, "sunny"),
        new PlayerSkin(false, "zuri")};

    public static final PlayerSkin ALEX = DEFAULT_SKINS[0];
    public static final PlayerSkin STEVE = DEFAULT_SKINS[15];

    public static final UUID NULL_UUID = UUID.randomUUID(); //Used if the UUID passed in is somehow null.

    public static final String NONE = "NONE";

    public enum Set {
        STEVE_ONLY("The only default skin will be Steve, the tried-and-true classic player model. His arms will always be wide.", STEVE),
        STEVE_ALEX("The default skins will either be wide-armed Steve or slim-armed Alex, as it was in 1.8 to 1.19.2.", STEVE, ALEX),
        ALL_DEFAULTS("Steve, Alex, and all of the new 1.19.3 default skins will be assigned. They all can appear in slim or wide armed variants.", DEFAULT_SKINS),
//        BONUS_CHARACTERS, //Some of the alternate skins from the old legacy console default skin packs. Currently not implemented
        ;

        private final List<PlayerSkin> skins = Lists.newLinkedList();
        private final String description;

        Set(String description, PlayerSkin... locs) {
            this.description = description;
            skins.addAll(Arrays.asList(locs));
        }

        public PlayerSkin getDefaultSkin(int index) {
            return getEntryFromIndex(index);
        }

        public PlayerSkin getDefaultSkin(UUID uuid) {
            return getDefaultSkin((uuid == null ? NULL_UUID : uuid).hashCode());
        }

        private PlayerSkin getEntryFromIndex(int index) {
            if(skins.isEmpty()) {
                throw new RuntimeException("Default skin set " + name() + " had empty skin list. What are you doing!?");
            }
            if(skins.size() == 1) {
                return skins.get(0);
            }
            if(skins.size() == 2) {
                return skins.get(index & 1);//1.8 skin choosing logic, returns Alex if UUID hash & 1 == 1
            }
            return skins.get(Math.floorMod(index, skins.size()));//1.19+ skin choosing logic. Uses Math.floorMod to roll the index of skin based on UUID hash
        }

        public String getDescription() {
            return description;
        }

        public static Set valueOfOrNull(String value) {
            return Arrays.stream(Set.values()).filter(skinSet -> skinSet.name().equals(value)).findFirst().orElse(null);
        }
    }
}
