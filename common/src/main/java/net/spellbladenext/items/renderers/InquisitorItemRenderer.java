package net.spellbladenext.items.renderers;

import net.spellbladenext.items.armoritems.InquisitorSet;
import net.spellbladenext.items.armoritems.Robes;
import net.spellbladenext.items.models.InquisitorItemModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class InquisitorItemRenderer extends GeoItemRenderer<InquisitorSet> {


    public InquisitorItemRenderer(AnimatedGeoModel<Robes> modelProvider) {
        super(new InquisitorItemModel());
    }
    public InquisitorItemRenderer() {
        super(new InquisitorItemModel());
    }
}
