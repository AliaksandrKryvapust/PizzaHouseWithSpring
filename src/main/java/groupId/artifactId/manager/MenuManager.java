package groupId.artifactId.manager;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.core.mapper.MenuMapper;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.manager.api.IMenuManager;
import groupId.artifactId.service.api.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuManager implements IMenuManager {

    private final IMenuService menuService;
    private final MenuMapper menuMapper;

    @Autowired
    public MenuManager(IMenuService menuService, MenuMapper menuMapper) {
        this.menuService = menuService;
        this.menuMapper = menuMapper;
    }

    @Override
    public MenuDtoOutput getAllData(Long id) {
        Menu menu = this.menuService.get(id);
        return menuMapper.outputMapping(menu);
    }

    @Override
    public MenuDtoCrudOutput save(MenuDtoInput menuDtoInput) {
        Menu menu = this.menuService.save(menuMapper.inputMapping(menuDtoInput));
        return menuMapper.outputCrudMapping(menu);
    }

    @Override
    public List<MenuDtoCrudOutput> get() {
        return this.menuService.get().stream().map(menuMapper::outputCrudMapping).collect(Collectors.toList());
    }

    @Override
    public MenuDtoCrudOutput get(Long id) {
        Menu menu = this.menuService.get(id);
        return menuMapper.outputCrudMapping(menu);
    }

    @Override
    public void delete(Long id) {
        this.menuService.delete(id);
    }

    @Override
    public MenuDtoCrudOutput update(MenuDtoInput menuDtoInput, Long id, Integer version) {
        Menu menu = this.menuService.update(menuMapper.inputMapping(menuDtoInput), id, version);
        return menuMapper.outputCrudMapping(menu);
    }
}
