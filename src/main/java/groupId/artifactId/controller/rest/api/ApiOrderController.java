package groupId.artifactId.controller.rest.api;

import groupId.artifactId.core.dto.input.OrderDtoInput;
import groupId.artifactId.core.dto.output.TicketDtoOutput;
import groupId.artifactId.core.dto.output.crud.TicketDtoCrudOutput;
import groupId.artifactId.manager.api.IOrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/order")
public class ApiOrderController {
    private final IOrderManager orderManager;

    @Autowired
    public ApiOrderController(IOrderManager orderManager) {
        this.orderManager = orderManager;
    }

    //Read POSITION
    //1) Read item need id param
    @GetMapping("/{id}")
    protected ResponseEntity<TicketDtoOutput> get(@PathVariable long id) {
        return ResponseEntity.ok(orderManager.getAllData(id));
    }

    //Read POSITION
    //1) Read list
    @GetMapping
    protected ResponseEntity<List<TicketDtoCrudOutput>> getList() {
        return ResponseEntity.ok(orderManager.get());
    }

    //CREATE POSITION
    //body json
    @PostMapping
    protected ResponseEntity<TicketDtoCrudOutput> post(@Valid @RequestBody OrderDtoInput orderDtoInput) {
        return ResponseEntity.ok(orderManager.save(orderDtoInput));
    }
}
