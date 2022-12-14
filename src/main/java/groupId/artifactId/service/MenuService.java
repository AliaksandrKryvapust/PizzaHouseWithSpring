package groupId.artifactId.service;

import groupId.artifactId.dao.api.IMenuDao;
import groupId.artifactId.dao.entity.Menu;
import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.service.api.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.List;

@Service
public class MenuService implements IMenuService {
    private final IMenuDao dao;

    @Autowired
    public MenuService(IMenuDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Menu> get() {
        return this.dao.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Menu get(Long id) {
        return this.dao.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Menu update(Menu menu, Long id, Integer version) {
        if (menu.getId() != null || menu.getVersion() != null) {
            throw new IllegalStateException("Menu id & version should be empty");
        }
        Menu currentEntity = this.dao.findById(id).orElseThrow();
        if (!currentEntity.getVersion().equals(version)) {
            throw new OptimisticLockException();
        }
        currentEntity.setName(menu.getName());
        currentEntity.setEnable(menu.getEnable());
        if (menu.getItems() != null && !menu.getItems().isEmpty()) {
            currentEntity.setItems(menu.getItems());
        }
        return this.dao.save(currentEntity);
    }

    @Override
    public Menu updateItem(Menu menu, MenuItem menuItem) {
        List<MenuItem> items = menu.getItems();
        items.add(menuItem);
        menu.setItems(items);
        return this.dao.save(menu);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.dao.deleteById(id);
    }

    @Override
    @Transactional
    public Menu save(Menu menu) {
        if (menu.getId() != null || menu.getVersion() != null) {
            throw new IllegalStateException("Menu id & version should be empty");
        }
        return this.dao.save(menu);
    }
}
