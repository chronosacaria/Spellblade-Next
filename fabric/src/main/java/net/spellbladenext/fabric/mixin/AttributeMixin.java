package net.spellbladenext.fabric.mixin;

/*@Mixin(LivingEntity.class)
abstract  class AttributeMixin extends Entity {
    AttributeMixin(final EntityType<?> type, final Level world) {
        super(type, world);
    }

    @Inject(
            method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;",
            require = 1, allow = 1, at = @At("RETURN"))
    private static void addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> info) {
        for (var entry: SpellAttributes.all.entrySet()) {
            var attribute = entry.getValue().attribute;
            info.getReturnValue().add(attribute);
        }
    }
}*/
