package groupId.artifactId.manager;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.core.mapper.MenuItemMapper;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.manager.api.IMenuItemManager;
import groupId.artifactId.service.api.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuItemManager implements IMenuItemManager {

    private final MenuItemMapper menuItemMapper;
    private final IMenuItemService menuItemService;

    @Autowired
    public MenuItemManager(MenuItemMapper menuItemMapper, IMenuItemService menuItemService) {
        this.menuItemMapper = menuItemMapper;
        this.menuItemService = menuItemService;
    }

    @Override
    public MenuItemDtoOutput save(MenuItemDtoInput menuItemDtoInput) {
        MenuItem menuItem = this.menuItemService.saveInTransaction(menuItemMapper.inputMapping(menuItemDtoInput),
                menuItemDtoInput.getMenuId());
        return menuItemMapper.outputMapping(menuItem);
    }

    @Override
    public List<MenuItemDtoOutput> get() {
        return this.menuItemService.get().stream().map(menuItemMapper::outputMapping).collect(Collectors.toList());
    }

    @Override
    public MenuItemDtoOutput get(Long id) {
        return menuItemMapper.outputMapping(this.menuItemService.get(id));
    }

    @Override
    public void delete(Long id) {
        this.menuItemService.delete(id);
    }

    @Override
    public MenuItemDtoOutput update(MenuItemDtoInput menuItemDtoInput, Long id, Integer version) {
        MenuItem menuItem = this.menuItemService.updateInTransaction(menuItemMapper.inputMapping(menuItemDtoInput),
                menuItemDtoInput.getMenuId(), id, version);
        return menuItemMapper.outputMapping(menuItem);
    }
}
