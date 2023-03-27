package net.spellbladenext.fabric.items;

import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class OrbRenderer extends GeoItemRenderer<Orb> {


    public OrbRenderer(AnimatedGeoModel<Orb> modelProvider) {
        super(new ArcaneOrbModel());
    }
    public OrbRenderer() {
        super(new ArcaneOrbModel());
    }
}
