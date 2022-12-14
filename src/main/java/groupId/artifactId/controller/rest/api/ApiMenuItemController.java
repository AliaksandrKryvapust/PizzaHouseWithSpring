package groupId.artifactId.controller.rest.api;

import groupId.artifactId.core.dto.input.MenuItemDtoInput;
import groupId.artifactId.core.dto.output.MenuItemDtoOutput;
import groupId.artifactId.manager.api.IMenuItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//CRUD controller
//IMenuItem
@RestController
@Validated
@RequestMapping("/api/menu_item")
public class ApiMenuItemController {
    private final IMenuItemManager menuItemManager;

    @Autowired
    public ApiMenuItemController(IMenuItemManager menuItemManager) {
        this.menuItemManager = menuItemManager;
    }

    //Read POSITION
    //1) Read item need id param
    @GetMapping("/{id}")
    protected ResponseEntity<MenuItemDtoOutput> get(@PathVariable long id) {
        return ResponseEntity.ok(menuItemManager.get(id));
    }

    //Read POSITION
    //1) Read list
    @GetMapping
    protected ResponseEntity<List<MenuItemDtoOutput>> getList() {
        return ResponseEntity.ok(menuItemManager.get());
    }

    //CREATE POSITION
    //body json
    @PostMapping
    protected ResponseEntity<MenuItemDtoOutput> post(@RequestBody @Valid MenuItemDtoInput dtoInput) {
        return ResponseEntity.ok(this.menuItemManager.save(dtoInput));
    }

    //UPDATE POSITION
    //need param id
    //need param version/date_update - optimistic lock
    //body json
    @PutMapping("/{id}/version/{version}")
    protected ResponseEntity<MenuItemDtoOutput> put(@PathVariable long id, @PathVariable("version") int version,
                                                    @Valid @RequestBody MenuItemDtoInput dtoInput) {
        return ResponseEntity.ok(this.menuItemManager.update(dtoInput, id, version));
    }

    //DELETE POSITION
    //need param id
    @DeleteMapping("/{id}")
    protected ResponseEntity<Object> delete(@PathVariable long id) {
        menuItemManager.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
