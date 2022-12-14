package groupId.artifactId.controller.rest.api;

import groupId.artifactId.core.dto.input.MenuDtoInput;
import groupId.artifactId.core.dto.output.MenuDtoOutput;
import groupId.artifactId.core.dto.output.crud.MenuDtoCrudOutput;
import groupId.artifactId.manager.api.IMenuManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//CRUD controller
//IMenu
@RestController
@Validated
@RequestMapping("/api/menu")
public class ApiMenuController {
    private final IMenuManager menuManager;

    @Autowired
    public ApiMenuController(IMenuManager menuManager) {
        this.menuManager = menuManager;
    }

    //Read POSITION
    //1) Read item need id param  (id = 1)
    @GetMapping("/{id}")
    protected ResponseEntity<MenuDtoOutput> get(@PathVariable long id) {
        return ResponseEntity.ok(menuManager.getAllData(id));
    }

    //Read POSITION
    //1) Read list
    @GetMapping
    protected ResponseEntity<List<MenuDtoCrudOutput>> getList() {
        return ResponseEntity.ok(menuManager.get());
    }

    //CREATE POSITION
    //body json
    @PostMapping
    protected ResponseEntity<MenuDtoCrudOutput> post(@Valid @RequestBody MenuDtoInput menuDtoInput) {
        return ResponseEntity.ok(menuManager.save(menuDtoInput));
    }

    //UPDATE POSITION
    //need param id
    //need param version/date_update - optimistic lock
    //body json
    @PutMapping("/{id}/version/{version}")
    protected ResponseEntity<MenuDtoCrudOutput> put(@PathVariable long id, @PathVariable("version") int version,
                                                    @Valid @RequestBody MenuDtoInput menuDtoInput) {
        return ResponseEntity.ok(menuManager.update(menuDtoInput, id, version));
    }

    //DELETE POSITION
    //need param id
    @DeleteMapping("/{id}")
    protected ResponseEntity<Object> delete(@PathVariable long id) {
        menuManager.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
