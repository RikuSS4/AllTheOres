package com.rikuss4.alltheores.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.reference.Reference;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

public class Utils {
	public static Field getField(Class clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			Class superClass = clazz.getSuperclass();
			if (superClass == null) {
				return null;
			} else {
				return getField(superClass, fieldName);
			}
		}
	}

	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}

	public static String toHex(int hex) {
		return String.format("0x%08X", hex & 0xFFFFFFFF);
	}

	public static String toHex(String hex) {
		if (hex.indexOf("x") >= 0) {
			return String.format("0x%08X", hex.substring(hex.indexOf("x")));
		}
		return String.format("0x%08X", hex);
	}

	public static int intFromHex(String hex) {
		if (hex.indexOf("x") >= 0) {
			hex = hex.substring(hex.lastIndexOf("x"));
		}
		if (hex.length() > 6) {
			hex = hex.substring(2);
		}
		return Integer.parseInt(hex, 16);
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}

	public static Item getItem(String itemName) {
		if (itemName == null) {
			return null;
		}
		if (itemName.lastIndexOf(":") >= 0) {
			if (getItemStack(itemName, 1) != null) {
				return getItemStack(itemName, 1).getItem();
			}
		} else {
			if (getItemStack("minecraft:" + itemName, 1) != null) {
				return getItemStack("minecraft:" + itemName, 1).getItem();
			}
		}
		return null;
	}

	public static ItemStack getItemStack(String itemName) {
		return getItemStack(itemName, 1);
	}

	public static ItemStack getItemStack(String itemName, int stackSize) {
		if (itemName == null || Item.itemRegistry.getObject(itemName) == null) {
			return null;
		}
		return new ItemStack((Item) Item.itemRegistry.getObject(itemName), stackSize);
		/*int meta = 0;
		itemName = itemName.trim();
		String[] item = itemName.split(":", 3);

		if (item.length > 2 && Utils.isInteger(item[2])) {
			meta = Integer.parseInt(item[2]);
		} else if (item.length > 1 && Utils.isInteger(item[1])) {
			meta = Integer.parseInt(item[1]);
		}
		
		return new ItemStack((Item) Item.itemRegistry.getObject(itemName), stackSize, meta);
		/*
		 * } else if (item.length > 2) {
		 * if (item[2].lastIndexOf(":") >= 0 &&
		 * Utils.isInteger(item[2].substring(item[2].lastIndexOf(":") + 1))) {
		 * meta = Integer.parseInt(item[2].substring(item[2].lastIndexOf(":") +
		 * 1));
		 * item[1] = item[1] + ":" + item[2].substring(0,
		 * item[2].lastIndexOf(":"));
		 * } else {
		 * item[1] = item[1] + ":" + item[2];
		 * }
		 * }
		 * if (item.length > 1 && GameRegistry.findItem(item[0], item[1]) != null)
		 * {
		 * if (meta > 0) {
		 * return new ItemStack(GameRegistry.findItem(item[0], item[1]),
		 * stackSize, meta);
		 * } else {
		 * return new ItemStack(GameRegistry.findItem(item[0], item[1]),
		 * stackSize);
		 * }
		 * } else if (GameRegistry.findItem("minecraft", item[0]) != null) {
		 * return new ItemStack(GameRegistry.findItem("minecraft", item[0]),
		 * stackSize);
		 * }
		 * return null;
		 */
	}

	/*
	 * public static ItemStack getItemStack(String itemName, int stackSize, int
	 * meta) {
	 * if (itemName == null) {
	 * return null;
	 * }
	 * if (itemName.lastIndexOf(":") >= 0) {
	 * if (getItemStack(itemName) != null) {
	 * return new ItemStack(getItemStack(itemName).getItem(), stackSize, meta);
	 * }
	 * } else {
	 * if (getItemStack("minecraft:" + itemName) != null) {
	 * return new ItemStack(getItemStack("minecraft:" + itemName).getItem(),
	 * stackSize, meta);
	 * }
	 * }
	 * return null;
	 * }
	 */

	public static String capitalize(String s1) {
		if (s1.lastIndexOf("__") > 0) {
			s1 = s1.substring(0, s1.lastIndexOf("__"));
		}
		Pattern p = Pattern.compile("_([a-zA-Z])");
		Matcher m = p.matcher(Character.toString(s1.charAt(0)).toUpperCase() + s1.substring(1));
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, " " + m.group(1).toUpperCase());
		}
		m.appendTail(sb);
		return sb.toString();
	}

	public static int between(int min, int max) {
	    return min + (int)(Math.random() * ((max - min) + 1));
	}

	public static boolean isBetween(int num, int min, int max) {
		return min <= num && num <= max;
	}

	public static ItemStack getOreDict(String name) {
		ArrayList<ItemStack> oreDict = OreDictionary.getOres(name);
		for (ItemStack item : oreDict) {
			String Mod_ID = GameRegistry.findUniqueIdentifierFor(item.getItem()).modId.toLowerCase();
			LogHelper.mod_debug("***" + Mod_ID + " ***");
			for (String mod : Reference.PreferredOrder) {
				if (mod.equals(Mod_ID)) return item;
			}
		}
		if (oreDict.size() > 0) {
			return oreDict.get(0);
		} else {
			return null;
		}
	}

	public static String[] getItemInfo(ItemStack item) {
		if (item != null) {
			String[] info = { "", "", "", "" };
			UniqueIdentifier ui = GameRegistry.findUniqueIdentifierFor(item.getItem());
			if (ui != null) {
				info[0] = ui.modId;
				info[1] = ui.name;
				info[2] = Integer.toString(item.getItemDamage());
				info[3] = OreDictionary.getOreName(OreDictionary.getOreID(item)).replaceAll(" ", "");
				info[3] = info[3].equalsIgnoreCase("unknown") ? "" : info[3];
				return info;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static String removeEIORecipe(ItemStack input) {
		if (input == null || input.getItem() == null) return null;
		String[] inputItemInfo = getItemInfo(input);
		if (inputItemInfo == null) return null;
		String recipe = "<AlloySmelterRecipes><vanillaFurnaceRecipes><exclude>" + (!inputItemInfo[3].equals("") ? "<itemStack oreDictionary=\"" + inputItemInfo[3] + "\" />" : "<itemStack modID=\"" + inputItemInfo[0] + "\" itemName=\"" + inputItemInfo[1] + "\" itemMeta=\"" + inputItemInfo[2] + "\"/>");
		
		recipe = recipe + "</exclude></vanillaFurnaceRecipes>";
		recipe = recipe + "<recipeGroup name=\"" + Reference.MOD_ID + "\" enabled=\"false\">" + "<recipe name=\"" + inputItemInfo[1] + "\" energyCost=\"2400\">" + "<input>" + (!inputItemInfo[3].equals("") ? "<itemStack oreDictionary=\"" + inputItemInfo[3] + "\" />" : "<itemStack modID=\"" + inputItemInfo[0] + "\" itemName=\"" + inputItemInfo[1] + "\" itemMeta=\"" + inputItemInfo[2] + "\" number=\"" + input.stackSize + "\" />") + "</input>";
		recipe = recipe + "<output /></recipe></recipeGroup></AlloySmelterRecipes>";
		LogHelper.mod_debug(recipe);
		return recipe;
	}

	public static String getEIORecipe(ItemStack input, ItemStack... outputs) {
		if (input == null || input.getItem() == null || outputs == null) return null;
		String[] inputItemInfo = getItemInfo(input);
		if (inputItemInfo == null) return null;
		String recipe = "<AlloySmelterRecipes><recipeGroup name=\"" + Reference.MOD_ID + "\">" + "<recipe name=\"" + inputItemInfo[1] + "\" energyCost=\"2400\">" + "<input>" + (!inputItemInfo[3].equals("") ? "<itemStack oreDictionary=\"" + inputItemInfo[3] + "\" />" : "<itemStack modID=\"" + inputItemInfo[0] + "\" itemName=\"" + inputItemInfo[1] + "\" itemMeta=\"" + inputItemInfo[2] + "\" number=\"" + input.stackSize + "\" />") + "</input>" + "<output>";
		String items = "";
		
		// Create Outputs
		for (ItemStack output : outputs) {
			if (output != null && output.getItem() != null) {
				String[] outputItemInfo = getItemInfo(output);
				if (outputItemInfo != null) {
				items = items + "<itemStack modID=\"" + outputItemInfo[0] + "\" itemName=\"" + outputItemInfo[1] + "\" itemMeta=\"" + outputItemInfo[2] + "\" number=\"" + output.stackSize + "\" />";
				LogHelper.mod_debug("Recipe " + input.stackSize + "x" + input.getDisplayName() + " => " + output.stackSize + "x" + output.getDisplayName());
				}
			}
		}

		if (items.equals("")) return null;
		recipe = recipe + items + "</output></recipe></recipeGroup></AlloySmelterRecipes>";
		LogHelper.mod_debug(recipe);
		return recipe;
	}

	public static void removeCrafting() {
		// TODO: Remove Block Crafting
		// Remove conflicting item recipes
		LogHelper.mod_debug("*** Remove crafting ***");
		List recipes = CraftingManager.getInstance().getRecipeList();
		List newRecipes = new ArrayList();
		for (ATOOre ore : Reference.ORES_LIST) {
			LogHelper.mod_debug("*** Ore being checked " + new ItemStack(ore).getDisplayName() + " ***");

			for (Object recipe : recipes) {
				if (recipe != null) {
					ShapedOreRecipe iRecipe = null;
					try {
						iRecipe = (ShapedOreRecipe) recipe;
					} catch (ClassCastException e) {
					}
					if (!(iRecipe != null && ((ore.getIngot(1) != null && Reference.CONFIG_ADD_CRAFTING_INGOTS && iRecipe.getRecipeOutput().isItemEqual(ore.getIngot(1))) || (ore.getDust(1) != null && Reference.CONFIG_ADD_CRAFTING_DUSTS && iRecipe.getRecipeOutput().isItemEqual(ore.getDust(1))) || (ore.getNugget(1) != null && Reference.CONFIG_ADD_CRAFTING_NUGGETS && iRecipe.getRecipeOutput().isItemEqual(ore.getNugget(1))) || (ore.getDustTiny(1) != null && Reference.CONFIG_ADD_CRAFTING_DUSTSTINY && iRecipe.getRecipeOutput().isItemEqual(ore.getDustTiny(1))) || (ore.getIngot(1) != null && Reference.CONFIG_ADD_CRAFTING_BLOCKS && iRecipe.getRecipeOutput().isItemEqual(ore.getBlock(1))) || (ore.getIngot(1) != null && Reference.CONFIG_ADD_CRAFTING_BRICKS && iRecipe.getRecipeOutput().isItemEqual(ore.getBrick(1)))))) {
						newRecipes.add(recipe);
					}
				}
			}
		}
		if (newRecipes != null) {
			// CraftingManager.getInstance().getRecipeList().clear();
			CraftingManager.getInstance().getRecipeList().retainAll(newRecipes);
			newRecipes.clear();
		}
	}
}
