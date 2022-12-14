package groupId.artifactId.core.mapper;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MenuMapper {
    private final MenuItemMapper menuItemMapper;

    @Autowired
    public MenuMapper(MenuItemMapper menuItemMapper) {
        this.menuItemMapper = menuItemMapper;
    }

    public Menu inputMapping(MenuDtoInput menuDtoInput) {
        if (menuDtoInput.getItems() == null || menuDtoInput.getItems().isEmpty()) {
            return Menu.builder()
                    .name(menuDtoInput.getName())
                    .enable(menuDtoInput.getEnable()).build();
        } else {
            List<MenuItem> menuItems = new ArrayList<>();
            for (MenuItemDtoInput dtoInput : menuDtoInput.getItems()) {
                MenuItem menuItem = menuItemMapper.inputMapping(dtoInput);
                menuItems.add(menuItem);
            }
            return Menu.builder()
                    .name(menuDtoInput.getName())
                    .enable(menuDtoInput.getEnable())
                    .items(menuItems).build();
        }

    }

    public MenuDtoCrudOutput outputCrudMapping(Menu menu) {
        return MenuDtoCrudOutput.builder()
                .id(menu.getId())
                .createdAt(menu.getCreationDate())
                .version(menu.getVersion())
                .name(menu.getName())
                .enable(menu.getEnable()).build();
    }

    public MenuDtoOutput outputMapping(Menu menu) {
        List<MenuItemDtoOutput> items = new ArrayList<>();
        for (MenuItem menuItem : menu.getItems()) {
            MenuItemDtoOutput item = menuItemMapper.outputMapping(menuItem);
            items.add(item);
        }
        return MenuDtoOutput.builder()
                .id(menu.getId())
                .createdAt(menu.getCreationDate())
                .version(menu.getVersion())
                .name(menu.getName())
                .enable(menu.getEnable())
                .items(items).build();
    }
}
