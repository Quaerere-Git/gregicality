package gregicadditions.machines.multi.steam;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.vec.Matrix4;
import gregicadditions.GAConfig;
import gregicadditions.capabilities.GregicAdditionsCapabilities;
import gregicadditions.capabilities.impl.RecipeMapSteamMultiblockController;
import gregicadditions.capabilities.impl.SteamMultiWorkable;
import gregicadditions.item.GAMetaBlocks;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntityHolder;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.metatileentity.multiblock.MultiblockAbility;
import gregtech.api.multiblock.BlockPattern;
import gregtech.api.multiblock.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.api.render.ICubeRenderer;
import gregtech.api.render.Textures;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import static gregtech.api.unification.material.Materials.Bronze;

public class MetaTileEntitySteamGrinder extends RecipeMapSteamMultiblockController {

    private static final double CONVERSION_RATE = GAConfig.multis.steamMultis.steamToEU;

    private static final MultiblockAbility<?>[] ALLOWED_ABILITIES = {
            GregicAdditionsCapabilities.STEAM_IMPORT_ITEMS, GregicAdditionsCapabilities.STEAM_EXPORT_ITEMS, GregicAdditionsCapabilities.STEAM
    };

    public MetaTileEntitySteamGrinder(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.MACERATOR_RECIPES, CONVERSION_RATE);
        this.recipeMapWorkable = new SteamMultiWorkable(this, CONVERSION_RATE, 8);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(MetaTileEntityHolder metaTileEntityHolder) {
        return new MetaTileEntitySteamGrinder(metaTileEntityId);
    }

    @Override
    protected BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                .aisle("XXX", "XXX", "XXX")
                .aisle("XXX", "X#X", "XXX")
                .aisle("XXX", "XSX", "XXX")
                .setAmountAtLeast('L', 14)
                .where('S', selfPredicate())
                .where('L', statePredicate(getCasingState()))
                .where('X', statePredicate(getCasingState()).or(abilityPartPredicate(ALLOWED_ABILITIES)))
                .where('#', isAirPredicate())
                .build();
    }

    public IBlockState getCasingState() {
        return GAMetaBlocks.getMetalCasingBlockState(Bronze);
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart sourcePart) {
        return GAMetaBlocks.METAL_CASING.get(Bronze);
    }

    @Override
    public void renderMetaTileEntity(CCRenderState renderState, Matrix4 translation, IVertexOperation[] pipeline) {
        super.renderMetaTileEntity(renderState, translation, pipeline);
        Textures.ROCK_CRUSHER_OVERLAY.renderSided(getFrontFacing(), renderState, translation, pipeline);
        if (recipeMapWorkable.isActive())
            Textures.ROCK_CRUSHER_ACTIVE_OVERLAY.renderSided(getFrontFacing(), renderState, translation, pipeline);
    }
}
