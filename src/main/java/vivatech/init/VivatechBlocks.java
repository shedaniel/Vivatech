package vivatech.init;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import vivatech.Vivatech;
import vivatech.block.*;

public class VivatechBlocks implements Initializable {
    public static final Identifier MACHINE_CHASSIS_ID = new Identifier(Vivatech.MODID, "machine_chassis");

    public static final BaseBlock MACHINE_CHASSIS;
    public static final CoalGeneratorBlock COAL_GENERATOR;
    public static final CrusherBlock CRUSHER;
    public static final ElectricFurnaceBlock ELECTRIC_FURNACE;
    public static final EnergyBankBlock ENERGY_BANK;
    public static final PressBlock PRESS;

    static {
        MACHINE_CHASSIS = new BaseBlock(Vivatech.METALLIC_BLOCK_SETTINGS);
        COAL_GENERATOR = new CoalGeneratorBlock();
        CRUSHER = new CrusherBlock();
        ELECTRIC_FURNACE = new ElectricFurnaceBlock();
        ENERGY_BANK = new EnergyBankBlock();
        PRESS = new PressBlock();
    }

    @Override
    public void initialize() {
        Registry.register(Registry.BLOCK, MACHINE_CHASSIS_ID, MACHINE_CHASSIS);
        Registry.register(Registry.BLOCK, CoalGeneratorBlock.ID, COAL_GENERATOR);
        Registry.register(Registry.BLOCK, CrusherBlock.ID, CRUSHER);
        Registry.register(Registry.BLOCK, ElectricFurnaceBlock.ID, ELECTRIC_FURNACE);
        Registry.register(Registry.BLOCK, EnergyBankBlock.ID, ENERGY_BANK);
        Registry.register(Registry.BLOCK, PressBlock.ID, PRESS);
    }
}
