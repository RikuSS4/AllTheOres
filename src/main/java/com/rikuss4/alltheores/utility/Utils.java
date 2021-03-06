package com.rikuss4.alltheores.utility;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.rikuss4.alltheores.blocks.ATOBlock;
import com.rikuss4.alltheores.blocks.ATOBrick;
import com.rikuss4.alltheores.blocks.ATOOre;
import com.rikuss4.alltheores.items.Resources.ATOCrushed;
import com.rikuss4.alltheores.items.Resources.ATOCrushedPurified;
import com.rikuss4.alltheores.items.Resources.ATODust;
import com.rikuss4.alltheores.items.Resources.ATODustTiny;
import com.rikuss4.alltheores.items.Resources.ATOIngot;
import com.rikuss4.alltheores.items.Resources.ATONugget;
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
			if (getItemStack(itemName) != null) {
				return getItemStack(itemName, 1).getItem();
			}
		} else {
			if (getItemStack("minecraft:" + itemName) != null) {
				return getItemStack("minecraft:" + itemName).getItem();
			}
		}
		return null;
	}

	public static ItemStack getItemStack(String itemName) {
		return getItemStack(itemName, 0);
	}

	public static ItemStack getItemStack(String itemName, int itemMeta) {
		if (itemMeta < 0)
			itemMeta = 0;
		return getItemStack(itemName, itemMeta, 1);
	}

	public static ItemStack getItemStack(String itemName, int itemMeta, int stackSize) {
		if (itemName == null)
			return null;

		itemName = itemName.trim();
		String[] item = itemName.split(":", 3);

		if (item.length > 2 && Utils.isInteger(item[item.length - 1])) {
			itemMeta = Integer.parseInt(item[item.length - 1]);
			for (int i = 1; i < item.length - 1; i++)
				item[0] = item[0] + ":" + item[i];
		} else if (item.length == 2 && Utils.isInteger(item[1])) {
			itemMeta = Integer.parseInt(item[1]);
		} else if (item.length > 1) {
			for (int i = 1; i < item.length; i++)
				item[0] = item[0] + ":" + item[i];
		}

		if (((Item) Item.itemRegistry.getObject(item[0])) != null) {
			LogHelper.mod_debug("getItemStack() {");
			LogHelper.mod_debug(" itemName: " + itemName);
			for (int i = 0; i < item.length; i++)
				LogHelper.mod_debug(" item[" + i + "]: " + item[i]);
			LogHelper.mod_debug(" itemMeta: " + itemMeta);
			LogHelper.mod_debug("}");
			return new ItemStack((Item) Item.itemRegistry.getObject(item[0]), stackSize, itemMeta);
		}
		return null;
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

	public static String getSpecialName(String name) {
		name = Character.toString(name.charAt(0)).toLowerCase() + name.substring(1);
		name.replaceAll(" ", "_");
		return name.replaceAll("([A-Z])", "_$1").toLowerCase();
	}

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
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static boolean isBetween(int num, int min, int max) {
		return num >= min && num <= max;
	}

	public static ItemStack getOreDict(String name) {
		LogHelper.mod_debug("oreDictName to check: " + name);
		ArrayList<ItemStack> oreDict = OreDictionary.getOres(name);
		for (ItemStack item : oreDict) {
			String Mod_ID = GameRegistry.findUniqueIdentifierFor(item.getItem()).modId.toLowerCase();
			LogHelper.mod_debug("oreDict:" + Mod_ID + ": " + item);
			for (String mod : Reference.PreferredOrder)
				if (mod.equalsIgnoreCase(Mod_ID)) {
					LogHelper.mod_debug("Using item: " + item);
					return item;
				}
		}
		if (oreDict.size() > 0) {
			LogHelper.mod_debug(oreDict.get(0).getUnlocalizedName());
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
				info[3] = OreDictionary.getOreName(OreDictionary.getOreID(item));
				info[3] = info[3].equalsIgnoreCase("unknown") ? "" : info[3];
				return info;
			}
		}
		return null;
	}

	public static String removeEIORecipe(ItemStack input) {
		if (input == null || input.getItem() == null)
			return null;
		String[] inputItemInfo = getItemInfo(input);
		if (inputItemInfo == null)
			return null;
		String recipe = "<AlloySmelterRecipes><vanillaFurnaceRecipes><exclude>" + (!inputItemInfo[3].equals("") ? "<itemStack oreDictionary=\"" + inputItemInfo[3] + "\" />" : "<itemStack modID=\"" + inputItemInfo[0] + "\" itemName=\"" + inputItemInfo[1] + "\" itemMeta=\"" + inputItemInfo[2] + "\"/>");

		recipe = recipe + "</exclude></vanillaFurnaceRecipes>";
		recipe = recipe + "<recipeGroup name=\"" + Reference.MOD_ID + "\" enabled=\"false\">" + "<recipe name=\"" + inputItemInfo[1] + "\" energyCost=\"2400\">" + "<input>" + (!inputItemInfo[3].equals("") ? "<itemStack oreDictionary=\"" + inputItemInfo[3] + "\" />" : "<itemStack modID=\"" + inputItemInfo[0] + "\" itemName=\"" + inputItemInfo[1] + "\" itemMeta=\"" + inputItemInfo[2] + "\" number=\"" + input.stackSize + "\" />") + "</input>";
		recipe = recipe + "<output /></recipe></recipeGroup></AlloySmelterRecipes>";
		LogHelper.mod_debug(recipe);
		return recipe;
	}

	public static String getEIORecipe(ItemStack input, ItemStack... outputs) {
		if (input == null || input.getItem() == null || outputs == null)
			return null;
		String[] inputItemInfo = getItemInfo(input);
		if (inputItemInfo == null)
			return null;
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

		if (items.equals(""))
			return null;
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

			LogHelper.mod_debug(Utils.capitalize(ore.baseName));
			for (Object recipe : recipes) {
				if (recipe != null) {
					ShapedOreRecipe iRecipe = null;
					try {
						iRecipe = (ShapedOreRecipe) recipe;
					} catch (ClassCastException e) {
					}
					if (iRecipe != null) {
						Boolean match = false;
						if (Reference.CONFIG_ADD_CRAFTING_BLOCKS && ore.blockRecipe) {
							ArrayList<ItemStack> oreDict = OreDictionary.getOres("block" + Utils.capitalize(ore.baseName));
							for (ItemStack item : oreDict) {
								if (item.isItemEqual(iRecipe.getRecipeOutput())) {
									LogHelper.mod_debug("Match Found for " + iRecipe.getRecipeOutput().getDisplayName());
									match = true;
									break;
								}
							}
							if (!match)
								newRecipes.add(recipe);
						} else
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

	public static LinkedList<ATOOre> sortOresList(LinkedList<ATOOre> ORES_LIST) {
		Collections.sort(ORES_LIST, new Comparator<ATOOre>() {
			@Override
			public int compare(ATOOre o1, ATOOre o2) {
				return o1.name.compareToIgnoreCase(o2.name);
			}
		});
		return ORES_LIST;
	}

	public static LinkedList<ATOIngot> sortIngotList(LinkedList<ATOIngot> LIST) {
		Collections.sort(LIST, new Comparator<ATOIngot>() {
			@Override
			public int compare(ATOIngot o1, ATOIngot o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return LIST;
	}

	public static LinkedList<ATONugget> sortNuggetList(LinkedList<ATONugget> LIST) {
		Collections.sort(LIST, new Comparator<ATONugget>() {
			@Override
			public int compare(ATONugget o1, ATONugget o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return LIST;
	}

	public static LinkedList<ATODust> sortDustList(LinkedList<ATODust> LIST) {
		Collections.sort(LIST, new Comparator<ATODust>() {
			@Override
			public int compare(ATODust o1, ATODust o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return LIST;
	}

	public static LinkedList<ATODustTiny> sortDustTinyList(LinkedList<ATODustTiny> LIST) {
		Collections.sort(LIST, new Comparator<ATODustTiny>() {
			@Override
			public int compare(ATODustTiny o1, ATODustTiny o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return LIST;
	}

	public static LinkedList<ATOCrushed> sortCrushedList(LinkedList<ATOCrushed> LIST) {
		Collections.sort(LIST, new Comparator<ATOCrushed>() {
			@Override
			public int compare(ATOCrushed o1, ATOCrushed o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return LIST;
	}

	public static LinkedList<ATOCrushedPurified> sortCrushedPurifiedList(LinkedList<ATOCrushedPurified> LIST) {
		Collections.sort(LIST, new Comparator<ATOCrushedPurified>() {
			@Override
			public int compare(ATOCrushedPurified o1, ATOCrushedPurified o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return LIST;
	}

	public static LinkedList<ATOBlock> sortBlockList(LinkedList<ATOBlock> LIST) {
		Collections.sort(LIST, new Comparator<ATOBlock>() {
			@Override
			public int compare(ATOBlock o1, ATOBlock o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return LIST;
	}

	public static LinkedList<ATOBrick> sortBrickList(LinkedList<ATOBrick> LIST) {
		Collections.sort(LIST, new Comparator<ATOBrick>() {
			@Override
			public int compare(ATOBrick o1, ATOBrick o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		return LIST;
	}

	public static ItemStack getDictName(String suffix, String subType, String type, String... names) {
		ItemStack item = null;
		for (String name : names) {
			item = Utils.getOreDict(name + suffix);
			if (item != null) {
				return item;
			}
		}
		if (suffix.toLowerCase().startsWith(subType)) {
			suffix = suffix.substring(subType.length());
			for (String name : names) {
				item = Utils.getOreDict(name + suffix);
				if (item != null) {
					return item;
				}
			}
		}
		if (suffix.toLowerCase().startsWith(type)) {
			suffix = suffix.substring(type.length());
			for (String name : names) {
				item = Utils.getOreDict(name + suffix);
				if (item != null) {
					return item;
				}
			}
		}
		return null;
	}

	public static boolean recipeExists(String type, String name) {
		if (!OreDictionary.getOres(type + Utils.capitalize(name)).isEmpty())
			return true;
		return false;
	}
}
