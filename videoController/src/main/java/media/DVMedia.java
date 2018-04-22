package media;

import util.Identifier;

import java.util.HashMap;
import java.util.Map;

public enum DVMedia {
    INSTANCE;

    private Map<Identifier, DVMedia> medias = new HashMap<>();

    DVMedia(){}

}
