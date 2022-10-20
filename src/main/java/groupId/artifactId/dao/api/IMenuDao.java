package groupId.artifactId.dao.api;

import groupId.artifactId.dao.entity.MenuItem;
import groupId.artifactId.dao.entity.api.IMenu;

import java.util.List;

public interface IMenuDao extends IDao<IMenu> {
    List<IMenu> get();
    void add(MenuItem menuItem, Long menuId);
    Boolean isIdExist(Long id);
    Boolean isDishExist(String name);
}
