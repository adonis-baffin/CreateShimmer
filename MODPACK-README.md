## Tags
### Enchantment
* `create_enchantment_industry:blaze_enchanter/enchanting` contains all enchantments available in Blaze Enchanter regular enchanting, which includes `minecraft:in_enchanting_table`
* `create_enchantment_industry:blaze_enchanter/enchanting_exclusive` contains all enchantments exclusive to Blaze Enchanter regular enchanting
* `create_enchantment_industry:blaze_enchanter/super_enchanting` contains all enchantments available in Blaze Enchanter Super enchanting, which includes `create_enchantment_industry:blaze_enchanter/super_enchanting_exclusive` and `minecraft:in_enchanting_table` and excludes `create_enchantment_industry:blaze_enchanter/enchanting_exclusive`
* `create_enchantment_industry:blaze_enchanter/enchanting` contains all enchantments exclusive to Blaze Enchanter Super enchanting, which includes `minecraft:treasure` and excludes `minecraft:cures`. (Curse enchantments are still available when Blaze Forger is cursed)
* `create_enchantment_industry:printer/deny` contains all enchantment uncopiable/denied by Printer.

## Recipe
###  Printing
`Printing` of Printer supports custom printing recipe. It has similar format as the recipe of Create, with type `create_enchantment_industry:grinding`. Printing recipe requires extra `sound` field as Printing-Finish-Sound, for example `"sound": "item.book.page_turn"`.   
Template item is in 2nd position of input, printing material item is in 1st position of input.
```json
{
  "type": "create_enchantment_industry:printing",
  "ingredients": [
    {
      "item": "minecraft:wheat"
    },
    {
      "item": "minecraft:cookie"
    },
    {
      "type": "fluid_tag",
      "amount": 250,
      "fluid_tag": "c:milk"
    }
  ],
  "results": [
    {
      "id": "minecraft:cookie"
    }
  ],
  "sound": "entity.generic.eat"
}
```
### Grinding
`Grinding` of Mechanical Grindstone supports custom grinding recipe. It is the same format as the recipe of Create, with type `create_enchantment_industry:printing`.   
Grinding can have fluid as input. If recipe has fluid as input, corresponding fluid in Grindstone Drain will be consumed.

## Data Maps
### Item
* `experience_fuel`: Item can be fed to Blaze Experience Workstation as Experience. `"special": true` indicates Super Experience

### Fluid
* `printing/[type]/ingredient`: Configure ingredient & consumption of builtin printing type
* `printing/custom_name/style`: Configure ingredient of different Text-styles of custom name printing
* `unit/experience`: Configure conversion ratio between experience fluid of other mods and Liquid Experience

### Enchantment
* `forging/cost_multiplier`: Configure cost multiplier of Blaze Forger's forging for each enchantment. Default value is 1.
* `forging/split_enchantment_cost_multiplier`: Configure cost multiplier of Blaze Forger's Enchantment Splitting for each enchantment. Default value is 1.
* `super_enchanting/custom_level_extension`: Configure exceeded level of enchantment can be produced by Super Enchanting for each enchantment. Default value is set in config.
* `printing/enchanted_book/custom_cost`: Configure cost of Printer printing Enchanted Book for each enchantment. Example:
````json
{
  "values": {
    "minecraft:mending": 
    [
      {
        "level": 1,
        "value": 100
      }
    ]
  }
}
````