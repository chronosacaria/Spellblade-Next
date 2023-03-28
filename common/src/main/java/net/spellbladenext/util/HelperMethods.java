package net.spellbladenext.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class HelperMethods {

    @Nullable
    public static <T extends Entity> T spellbladenext$getNearestEntityAfterEntities(List<? extends T> list, @Nullable Entity livingEntity, double d, double e, double f) {
        double g = -1.0D;
        T livingEntity2 = null;
        Iterator<? extends T> iterator = list.iterator();

        while(true) {
            T livingEntity3;
            double h;
            do {
                do {
                    if (!iterator.hasNext()) {
                        return livingEntity2;
                    }

                    livingEntity3 = (T)iterator.next();
                } while(livingEntity == livingEntity3);

                h = livingEntity3.squaredDistanceTo(d, e, f);
            } while(g != -1.0D && !(h < g));

            g = h;
            livingEntity2 = livingEntity3;
        }
    }
}
