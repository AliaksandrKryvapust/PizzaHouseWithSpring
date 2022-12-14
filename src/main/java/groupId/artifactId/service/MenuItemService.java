package groupId.artifactId.service;

import groupId.artifactId.dao.api.IMenuItemDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.PizzaInfo;
import groupId.artifactId.service.api.IMenuItemService;
import groupId.artifactId.service.api.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Objects;

@Service
public class MenuItemService implements IMenuItemService {
    private final IMenuItemDao menuItemDao;
    private final IMenuService menuService;

    @Autowired
    public MenuItemService(IMenuItemDao menuItemDao, IMenuService menuService) {
        this.menuItemDao = menuItemDao;
        this.menuService = menuService;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        if (menuItem.getId() != null || menuItem.getVersion() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        return this.menuItemDao.save(menuItem);
    }

    @Override
    @Transactional
    public MenuItem saveInTransaction(MenuItem menuItem, Long menuId) {
        MenuItem savedMenuItem = this.save(menuItem);
        if (menuId != null) {
            Menu menu = menuService.get(menuId);
            List<MenuItem> items = menuService.updateItem(menu, savedMenuItem).getItems();
            return Objects.requireNonNull(items.stream()
                    .filter((i) -> i.getPizzaInfo().getName().equals(menuItem.getPizzaInfo().getName()))
                    .findFirst().orElse(new MenuItem()));
        } else {
            return savedMenuItem;
        }
    }

    @Override
    public MenuItem update(MenuItem menuItem, Long id, Integer version) {
        if (menuItem.getId() != null || menuItem.getVersion() != null) {
            throw new IllegalStateException("MenuItem id & version should be empty");
        }
        MenuItem currentEntity = this.menuItemDao.findById(id).orElseThrow();
        if (!currentEntity.getVersion().equals(version)) {
            throw new OptimisticLockException("menu_item table update failed, version does not match update denied");
        }
        PizzaInfo currentPizzaInfo = currentEntity.getPizzaInfo();
        currentPizzaInfo.setName(menuItem.getPizzaInfo().getName());
        currentPizzaInfo.setDescription(menuItem.getPizzaInfo().getDescription());
        currentPizzaInfo.setSize(menuItem.getPizzaInfo().getSize());
        currentEntity.setPrice(menuItem.getPrice());
        currentEntity.setPizzaInfo(currentPizzaInfo);
        return this.menuItemDao.save(currentEntity);
    }

    @Override
    @Transactional
    public MenuItem updateInTransaction(MenuItem menuItem, Long menuId, Long id, Integer version) {
        MenuItem savedMenuItem = this.update(menuItem, id, version);
        if (menuId != null) {
            Menu menu = menuService.get(menuId);
            List<MenuItem> items = menuService.updateItem(menu, savedMenuItem).getItems();
            return Objects.requireNonNull(items.stream()
                    .filter((i) -> i.getId().equals(id)).findFirst().orElseThrow());
        } else {
            return savedMenuItem;
        }
    }

    @Override
    public List<MenuItem> get() {
        return this.menuItemDao.findAll();
    }

    @Override
    public MenuItem get(Long id) {
        return this.menuItemDao.findById(id).orElseThrow();
    }

    @Override
    public List<MenuItem> getListById(List<Long> ids) {
        return this.menuItemDao.findAllById(ids);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.menuItemDao.deleteById(id);
    }
}
